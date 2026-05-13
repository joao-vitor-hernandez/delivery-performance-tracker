import java.time.LocalDate; //Para que o java consiga ver a data do aparelho
import java.util.Scanner;
import java.time.format.DateTimeFormatter; //tradutor de datas
import java.time.format.DateTimeParseException; //para erro de datas
import java.util.InputMismatchException; //para erro de números
import model.Entrega;
import repository.EntregaRepository;
import service.EntregaService;

import java.util.List;

public class Principal {
    public static void carregarERelatar(EntregaRepository repository){
        EntregaService service = new EntregaService(repository);
        List<Entrega> entregaDoMes = service.obterEntregasDoMesAtual();

        if (entregaDoMes.isEmpty()) {
            System.out.println("Nenhum dado disponível para o mês atual.");
            return;
        }
            double taxa = service.calcularTaxaSucesso(entregaDoMes);
            int totalGeral = entregaDoMes.stream().mapToInt(e -> e.getSucessos() + e.getFalhas()).sum();

            System.out.println("\n--- STATUS ACUMULADO DO MÊS ---");
            System.out.println("Total de pacotes: " + totalGeral);
            System.out.printf("Taxa de Sucesso: %.2f%%\n",taxa);

            //lógica dos 98%
            if (taxa < 98) {
                int faltam = service.calcularProjecaoPlatina(entregaDoMes, 0.98);
                System.out.printf("ALERTA: Faltam %d entregas perfeitas para chegar em 98%%!\n", (int) Math.ceil(faltam));
            } else {
                System.out.println("PARABÉNS: Você está na meta Platina!");
            }
    }
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        EntregaRepository repository = new EntregaRepository();
        int opcao = 0;

        while (opcao != 3) {
            System.out.println("\n--- MONITOR MERCADO ENVIO (META 98%) ---");
            System.out.println("1. Lançar entregas");
            System.out.println("2. Ver relatório e quanto falta para Platina");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            try{
                opcao = teclado.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números para o menu.");
                teclado.next();
                continue;
            }

            if (opcao == 1) {
                try{
                    System.out.println("\n[Lançamento de Entrega]");
                    System.out.print("Data (DD/MM/AAAA) ou 'hoje': ");
                    String dataInput = teclado.next();
                
                    LocalDate dataFinal;
                    if (dataInput.equalsIgnoreCase("hoje")) {
                        dataFinal = LocalDate.now();
                    } else {
                        //usar o formato brasileiro de data ao invés do AAAA-MM-DD
                        DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        //aqui o java transforma o texto da data em uma data real
                        dataFinal = LocalDate.parse(dataInput, formatoBR);
                    }
                    System.out.println("Quantos pacotes entregues com SUCESSO? ");
                    int suces = teclado.nextInt();
                    System.out.println("Quantos pacotes FALHOS/DEVOLVIDOS? ");
                    int fal = teclado.nextInt();

                    Entrega entregaDeHoje = new Entrega(dataFinal, suces, fal);
                    System.out.println("Gravando entrega do dia: " + entregaDeHoje.getData());
                    repository.salvar(entregaDeHoje);

                } catch(DateTimeParseException e) {
                    System.out.println("ERRO: Formato de data inválido! Use: DD/MM/AAAA");
                } catch(InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números para sucessos e falhas.");
                    teclado.next();
                } catch(IllegalArgumentException e){
                    System.out.println("ERRO DE VALIDAÇÃO: " + e.getMessage());
                }
            } else if (opcao == 2) {
                System.out.println("\n[Relatório Mensal]");
                carregarERelatar(repository);
            } else if (opcao == 3) {
                System.out.println("Saindo... Boa rota amanhã!");
            } else {
                System.out.println("Opção Inválida.");
            }
        }
        teclado.close();
    }
}
