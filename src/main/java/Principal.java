import java.time.LocalDate; //Para que o java consiga ver a data do aparelho
import java.util.Scanner;
import java.time.format.DateTimeFormatter; //tradutor de datas
import java.time.format.DateTimeParseException; //para erro de datas
import java.util.InputMismatchException; //para erro de números
import model.Entrega;
import model.Usuario;
import repository.ConexaoSQLite;
import repository.EntregaRepository;
import repository.UsuarioRepository;
import service.EntregaService;
import service.RelatorioPdfService;
import service.SenhaService;

import java.util.List;

public class Principal {
    public static void carregarERelatar(EntregaRepository repository, int usuarioId, boolean exportarParaPdf){
        EntregaService service = new EntregaService(repository);
        List<Entrega> entregaDoMes = service.obterEntregasDoMesAtual(usuarioId);

        if (entregaDoMes.isEmpty()) {
            System.out.println("Nenhum dado disponível para o mês atual.");
            return;
        }
            double taxa = service.calcularTaxaSucesso(entregaDoMes);
            int totalGeral = service.getTotalPacotes(entregaDoMes);
            int faltam = (taxa < 98) ? service.calcularProjecaoPlatina(entregaDoMes, 0.98):0;

            if (exportarParaPdf) {
                RelatorioPdfService pdfService = new RelatorioPdfService();
                pdfService.gerarRelatorioMensal(entregaDoMes, taxa, totalGeral, faltam);
            } else {
                System.out.println("\n--- STATUS ACUMULADO DO MÊS ---");
                System.out.println("Total de pacotes: " + totalGeral);
                System.out.printf("Taxa de Sucesso: %.2f%%\n",taxa);

                //lógica dos 98%
                if (taxa < 98) {
                    System.out.printf("ALERTA: Faltam %d entregas perfeitas para chegar em 98%%!\n", faltam);
                } else {
                    System.out.println("PARABÉNS: Você está na meta Platina!");
                }
            }
    }
    public static void main(String[] args) {
        ConexaoSQLite.criarTabelaSeNaoExistir(); //inicialização do banco de dados

        //DATA SEEDING: criação automatica do usuario admin para testes
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        if (usuarioRepository.buscarPorUsername("admin") == null) {
            String senhaHashAdmin = SenhaService.gerarHash("admin");
            usuarioRepository.salvar(new Usuario("admin", senhaHashAdmin));
        }
        Scanner teclado = new Scanner(System.in);
        EntregaRepository entregaRepository = new EntregaRepository();

        boolean fecharSistemaCompleto = false;

        //LOOP PRINCIPAL
        while (!fecharSistemaCompleto) {
            Usuario usuarioLogado = null;

            //SUB-LOOP
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

                    //buscar usuário no banco de dados
                    Usuario usuarioBanco = usuarioRepository.buscarPorUsername(user);

                    //valida se o usuário existe e se a senha bate com o Hash
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

                    //aplica o hash do BCrypt antes de mandar para o repositório
                    String senhaMascarada = SenhaService.gerarHash(novaSenha);
                    Usuario novoUsuario = new Usuario(novoUser, senhaMascarada);

                    if (usuarioRepository.salvar(novoUsuario)) {
                        System.out.println("Cadastro realizado! Uso a opção 1 para entrar.");
                    }
                } else if (opcaoAuth == 3) {
                    System.out.println("Encerrando o monitor... Até a próxima rota!");
                    fecharSistemaCompleto = true;
                } else {
                    System.out.println("Opção inválida.");
                }
            }

            //SUB-LOOP = só roda se o usuário estiver logado
            int opcaoEntrega = 0;
            while (usuarioLogado != null && opcaoEntrega != 4) {
                System.out.println("\n--- MONITOR ENVIOS EXTRA ---");
                System.out.println("Motorista ativo: " + usuarioLogado.getUsername());
                System.out.println("1. Lançar entregas");
                System.out.println("2. Ver relatório e quanto falta para Plantina");
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
                    try{
                        System.out.println("\n[Lançamento da Entrega]");
                        System.out.print("Data (DD/MM/AAAA) ou 'hoje': ");
                        teclado.nextLine();
                        String dataInput = teclado.nextLine();

                        LocalDate dataFinal;
                        if (dataInput.equalsIgnoreCase("hoje")) {
                            dataFinal = LocalDate.now();
                        } else {
                            DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            dataFinal = LocalDate.parse(dataInput, formatoBR);
                        }
                        System.out.print("Quantos pacotes entregues com sucesso? ");
                        int suces = teclado.nextInt();
                        System.out.print("Quantos pacotes falhos/devolvidos: ");
                        int fal = teclado.nextInt();

                        Entrega entregaDeHoje = new Entrega(usuarioLogado.getId(), dataFinal, suces, fal);
                        System.out.println("Gravando entrega no banco de dados...");
                        entregaRepository.salvar(entregaDeHoje);
                    } catch(DateTimeParseException e){
                        System.out.println("ERRO: Formato de data inválido! Use: DD/MM/AAAA");
                    } catch(InputMismatchException e){
                        System.out.println("ERRO: Digite apenas números para sucessos e falhas.");
                        teclado.next();
                    } catch(IllegalArgumentException e){
                        System.out.println("ERRO DE VALIDAÇÃO: " + e.getMessage());
                    }
                }else if (opcaoEntrega == 2) {
                    carregarERelatar(entregaRepository, usuarioLogado.getId(), false);
                }else if (opcaoEntrega == 3) {
                    carregarERelatar(entregaRepository, usuarioLogado.getId(), true);
                }else if (opcaoEntrega == 4) {
                    System.out.println("Desconectando motorista " + usuarioLogado.getUsername() + "...");
                    usuarioLogado = null; //destrói a sessão e força retorno para o login
                }else {
                    System.out.println("Opção inválida!");
                }
            }
        }
        teclado.close();
    }
}