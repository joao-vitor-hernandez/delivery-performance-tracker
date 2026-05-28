import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import model.Entrega;
import model.Usuario;
import repository.UsuarioRepository; // Manteremos até refatorar o usuário
import service.EntregaService;
import service.RelatorioPdfService;
import service.SenhaService;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "model")
public class Principal implements CommandLineRunner {

    // O Spring injeta automaticamente as dependências prontas aqui!
    private final EntregaService entregaService;

    public Principal(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    public static void main(String[] args) {
        // Liga o motor do Spring Boot
        SpringApplication.run(Principal.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Todo o fluxo principal do seu menu antigo entra aqui dentro!
        
        // DATA SEEDING: criação automática do usuário admin para testes
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        if (usuarioRepository.buscarPorUsername("admin") == null) {
            String senhaHashAdmin = SenhaService.gerarHash("admin");
            usuarioRepository.salvar(new Usuario("admin", senhaHashAdmin));
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

                    Usuario usuarioBanco = usuarioRepository.buscarPorUsername(user);
                    if (usuarioBanco != null && SenhaService.verificarSenha(senha, usuarioBanco.getSenhaHash())) {
                        usuarioLogado = usuarioBanco;
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

                    String senhaMascarada = SenhaService.gerarHash(novaSenha);
                    Usuario novoUsuario = new Usuario(novoUser, senhaMascarada);

                    if (usuarioRepository.salvar(novoUsuario)) {
                        System.out.println("Cadastro realizado! Use a opção 1 para entrar.");
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

                        // Criamos o objeto de entrega provisório
                        Entrega entregaLancada = new Entrega(usuarioLogado.getId(), dataFinal, suces, fal);
                        
                        System.out.println("Processando lançamento no banco de dados...");
                        // O serviço cuida de verificar se insere ou atualiza de forma transparente!
                        entregaService.salvarOuAtualizar(entregaLancada);
                        System.out.println("Dados salvos/atualizados com sucesso!");

                    } catch(DateTimeParseException e){
                        System.out.println("ERRO: Formato de data inválido! Use: DD/MM/AAAA");
                    } catch(InputMismatchException e){
                        System.out.println("ERRO: Digite apenas números para sucessos e falhas.");
                        teclado.next();
                    } catch(IllegalArgumentException e){
                        System.out.println("ERRO DE VALIDAÇÃO: " + e.getMessage());
                    }
                } else if (opcaoEntrega == 2) {
                    carregarERelatar(usuarioLogado, false);
                } else if (opcaoEntrega == 3) {
                    carregarERelatar(usuarioLogado, true);
                } else if (opcaoEntrega == 4) {
                    System.out.println("Desconectando motorista " + usuarioLogado.getUsername() + "...");
                    usuarioLogado = null;
                } else {
                    System.out.println("Opção inválida!");
                }
            }
        }
        teclado.close();
    }

    // Método auxiliar adaptado para usar o serviço injetado pelo Spring
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
                System.out.printf("ALERTA: Faltam %d entregas perfeitas para chegar em 98%%!\n", faltam);
            } else {
                System.out.println("PARABÉNS: Você está na meta Platina!");
            }
        }
    }
}