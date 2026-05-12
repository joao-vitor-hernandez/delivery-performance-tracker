import java.time.LocalDate; //Para que o java consiga ver a data do aparelho
import java.util.Scanner;
import java.io.FileWriter; //Para escrever arquivos
import java.io.PrintWriter; //Para formatar o texto no arquivo
import java.io.IOException; //Para lidar com erros no arquivo
import java.io.BufferedReader; //Para ler o arquivo
import java.io.FileReader; //Para abrir o arquivo de leitura
import java.time.format.DateTimeFormatter; //tradutor de datas
import java.time.format.DateTimeParseException; //para erro de datas
import java.util.InputMismatchException; //para erro de números

public class Principal {
    public static void salvarNoArquivo(Entrega entrega){
        //try catch é para possíveis erros que o usuário digitar, pegar somente oque é certo
        try (FileWriter fw = new FileWriter("entregas.csv", true);PrintWriter pw = new PrintWriter(fw)){

        pw.println(entrega.getData() + "," + entrega.getSucessos() + "," + entrega.getFalhas());
        System.out.println("Dados salvos no arquivo entregas.csv");
    } catch (IOException e){
        System.out.println("Erro ao salvar os dados: " + e.getMessage());
    }
    }

    public static void carregarERelatar(){
        int totalSucesso = 0;
        int totalFalha = 0;

        LocalDate dataAtual = LocalDate.now();
        int mesAtual = dataAtual.getMonthValue();
        int anoAtual =  dataAtual.getYear();

        try (BufferedReader br = new BufferedReader(new FileReader("entregas.csv"))){
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linha.split(",");
                if (partes.length < 3) {
                    System.out.println("AVISO: Pulando linha mal formatada no arquivo.");
                    continue;
                }
                try{
                    //transformando o texto data em objeto data
                    LocalDate dataRegistro = LocalDate.parse(partes[0]);

                    // O índice [1] é sucesso e [2] é falha
                    //só soma se for o mês/ano atual
                    if (dataRegistro.getMonthValue() == mesAtual && dataRegistro.getYear() == anoAtual) {
                        totalSucesso += Integer.parseInt(partes[1]);
                        totalFalha += Integer.parseInt(partes[2]);
                    }
                } catch (DateTimeParseException | NumberFormatException e){
                    //se a data estiver em um formato diferente ou os números não forem números
                    System.out.println("AVISO: Erro de conversão em uma linha do arquivo. Pulando...");
                }
            }

            int totalGeral = totalSucesso + totalFalha;

            if (totalGeral == 0) {
                System.out.println("Nenhum dado disponível para o mês atual.");
                return;
            }
            double taxa = ((double) totalSucesso/totalGeral)*100;

            System.out.println("\n--- STATUS ACUMULADO DO MÊS ---");
            System.out.println("Total de pacotes: " + totalGeral);
            System.out.printf("Taxa de Sucesso: %.2f%%\n",taxa);

            //lógica dos 98%
            if (taxa < 98) {
                double meta = 0.98;
                double faltam = (meta*totalGeral - totalSucesso) / (1-meta);
                System.out.printf("ALERTA: Faltam %d entregas perfeitas para chegar em 98%%!\n", (int) Math.ceil(faltam));
            } else {
                System.out.println("PARABÉNS: Você está na meta Platina!");
            }
        } catch (IOException e) {
            System.out.println("Ainda não existem dados salvos para gerar o relatório.");
        }
    }
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 3) {
            System.out.println("\n--- MONITOR MERCADO ENVIO (META 98%) ---");
            System.out.println("1. Lançar entregas");
            System.out.println("2. Ver relatório e quanto falta para Platina");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = teclado.nextInt();

            if (opcao == 1) {
                try{
                    System.out.println("\n[Lançamento de Entrega]");
                    System.out.println("Data (DD/MM/AAAA) ou 'hoje': ");
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

                    if (suces < 0 || fal < 0) {
                        System.out.println("ERRO: Os valores não podem ser negativos.");
                    }

                    Entrega entregaDeHoje = new  Entrega(dataFinal, suces, fal);
                    System.out.println("Gravando entrega do dia: " + entregaDeHoje.getData());
                    salvarNoArquivo(entregaDeHoje);
                } catch(DateTimeParseException e) {
                    System.out.println("ERRO: Formato de data inválido! Use: DD/MM/AAAA");
                } catch(InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números para sucessos e falhas.");
                    teclado.next();
                }
            } else if (opcao == 2) {
                System.out.println("\n[Relatório Mensal]");
                carregarERelatar();
            } else if (opcao == 3) {
                System.out.println("Saindo... Boa rota amanhã!");
            } else {
                System.out.println("Opção Inválida.");
            }
        }
        teclado.close();
    }
}
