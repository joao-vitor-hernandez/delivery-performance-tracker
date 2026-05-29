package com.entregas;
import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.entregas.model.Entrega;
import com.entregas.model.Usuario;
import com.entregas.repository.UsuarioRepository;
import com.entregas.service.EntregaService;
import com.entregas.service.RelatorioPdfService;
import com.entregas.service.UsuarioService;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.entregas.repository")
@EntityScan(basePackages = "com.entregas.model")
public class Principal implements CommandLineRunner {

    private final EntregaService entregaService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService; // INJEÇÃO DA NOVA SERVICE

    // Construtor unificado para injeção de dependências do Spring
    public Principal(EntregaService entregaService, UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.entregaService = entregaService;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    public static void main(String[] args) {
        // Liga o motor do Spring Boot
        SpringApplication.run(Principal.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
        // DATA SEEDING: Uso inteligente do Optional
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            // O próprio UsuarioService já aplica o BCrypt internamente no momento do cadastro!
            usuarioService.cadastrarUsuario(new Usuario("admin", "admin"));
        }

        Scanner teclado = new Scanner(System.in);
        boolean fecharSistemaCompleto = false;

        // LOOP PRINCIPAL
        while (!fecharSistemaCompleto) {
            Usuario usuarioLogado = null;

            // SUB-LOOP de Autenticação
            while (usuarioLogado == null && !fecharSistemaCompleto) {
                System.out.println("\n=== ACESSO AO SISTEMA ===");
                System.out.println("1. Fazer login");
                System.out.println("2. Cadastrar Novo Motorista");
                System.out.println("3. Sair do Programa");
                System.out.print("Escolha uma opção: ");

                int opcaoAuth = 0;
                try {
                    opcaoAuth = teclado.nextInt();
                } catch(InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números para o acesso.");
                    teclado.next();
                    continue;
                }

                if (opcaoAuth == 1) {
                    System.out.print("Usuário: ");
                    String user = teclado.next();
                    System.out.print("Senha: ");
                    String senha = teclado.next();

                    // Utilizamos o método de login limpo do UsuarioService, resolvendo o erro do Optional!
                    Optional<Usuario> usuarioOpt = usuarioService.login(user, senha);

                    if (usuarioOpt.isPresent()) {
                        usuarioLogado = usuarioOpt.get(); // Retiramos da caixa protetora com sucesso
                        System.out.println("\nLogin efetuado com sucesso.");
                    } else {
                        System.out.println("\n ERRO: Usuário ou senha inválidos.");
                    }

                } else if(opcaoAuth == 2) {
                    System.out.println("\n[Cadastro de Novo Motorista]");
                    System.out.print("Digite o usuário desejado: ");
                    String novoUser = teclado.next();
                    System.out.print("Digite a senha: ");
                    String novaSenha = teclado.next();

                    // A senha vai limpa para o Usuario, o Service mascara ela antes de salvar!
                    Usuario novoUsuario = new Usuario(novoUser, novaSenha);
                    
                    try {
                        usuarioService.cadastrarUsuario(novoUsuario);
                        System.out.println("Cadastro realizado! Use a opção 1 para entrar.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERRO ao cadastrar motorista: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("ERRO ao cadastrar motorista: dados inválidos.");
                    }
                    
                } else if (opcaoAuth == 3) {
                    System.out.println("Encerrando o monitor... Até a próxima rota!");
                    fecharSistemaCompleto = true;
                } else {
                    System.out.println("Opção inválida.");
                }
            }

            // SUB-LOOP do Motorista = só roda se o usuário estiver logado
            int opcaoEntrega = 0;
            while (usuarioLogado != null && opcaoEntrega != 4) {
                System.out.println("\n--- MONITOR ENVIOS EXTRA ---");
                System.out.println("Motorista ativo: " + usuarioLogado.getUsername());
                System.out.println("1. Lançar entregas");
                System.out.println("2. Ver relatório e quanto falta para Platina");
                System.out.println("3. Exportar relatório do mês em PDF");
                System.out.println("4. Desconectar");
                System.out.print("Escolha uma opção: ");

                try {
                    opcaoEntrega = teclado.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números para o menu.");
                    teclado.next();
                    continue;
                }

                if (opcaoEntrega == 1) {
                    try {
                        System.out.println("\n[Lançamento da Entrega]");
                        System.out.print("Data (DD/MM/AAAA) ou 'hoje': ");
                        teclado.nextLine();
                        String dataInput = teclado.nextLine();

                        DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate dataFinal;

                        if (dataInput.trim().equalsIgnoreCase("hoje")) {
                            dataFinal = LocalDate.now();
                        } else {
                            dataFinal = LocalDate.parse(dataInput, formatoBR);
                        }

                        System.out.print("Quantos pacotes entregues com sucesso? ");
                        int suces = teclado.nextInt();

                        System.out.print("Quantos pacotes falhos/devolvidos: ");
                        int fal = teclado.nextInt();

                        Entrega entregaLancada = new Entrega(usuarioLogado.getId(), dataFinal, suces, fal);
                        System.out.println("Processando lançamento no banco de dados...");
                        entregaService.salvarOuAtualizar(entregaLancada);
                        System.out.println("Dados salvos/atualizados com sucesso!");
                        
                    } catch(DateTimeParseException e){
                        System.out.println("ERRO: Formato de data inválido! Use: DD/MM/AAAA");
                    } catch(InputMismatchException e){
                        System.out.println("ERRO: Digite apenas números para sucessos e falhas.");
                        teclado.next();
                    }
                } else if (opcaoEntrega == 2) {
                    carregarERelatar(usuarioLogado, false);
                } else if (opcaoEntrega == 3) {
                    carregarERelatar(usuarioLogado, true);
                } else if (opcaoEntrega == 4) {
                    System.out.println("\nUsuário " + usuarioLogado.getUsername() + " desconectado.");
                    usuarioLogado = null;
                } else {
                    System.out.println("Opção inválida!");
                }
            }
        }
        teclado.close();
    }

    private void carregarERelatar(Usuario usuarioLogado, boolean exportarParaPdf) {
        List<Entrega> entregaDoMes = entregaService.obterEntregasDoMesAtual(usuarioLogado.getId());
        
        if (entregaDoMes.isEmpty()) {
            System.out.println("Nenhum dado disponível para o mês atual.");
            return;
        }
        
        double taxa = entregaService.calcularTaxaSucesso(entregaDoMes);
        int totalGeral = entregaService.getTotalPacotes(entregaDoMes);
        int faltam = (taxa < 98) ? entregaService.calcularProjecaoPlatina(entregaDoMes, 0.98) : 0;
        
        if (exportarParaPdf) {
            RelatorioPdfService pdfService = new RelatorioPdfService();
            pdfService.gerarRelatorioMensal(entregaDoMes, taxa, totalGeral, faltam, usuarioLogado.getUsername());
        } else {
            System.out.println("\n--- STATUS ACUMULADO DO MÊS ---");
            System.out.println("Total de pacotes: " + totalGeral);
            System.out.printf("Taxa de Sucesso: %.2f%%\n", taxa);
            
            if (taxa < 98) {
                System.out.println("Alerta: Você está ABAIXO da meta Platina (98%).");
                System.out.println("Faltam aproximadamente " + faltam + " entregas 100% limpas para recuperar seu nível.");
            } else {
                System.out.println("Parabéns! Você está Mantendo a Meta Corporativa de Ouro/Platina.");
            }
        }
    }
}