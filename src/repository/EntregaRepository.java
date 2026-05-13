package repository;

import model.Entrega;
import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntregaRepository {
    private static final String CAMINHO_ARQUIVO = "entregas.csv";

    public void salvar (Entrega entrega){
        try (FileWriter fw = new FileWriter(CAMINHO_ARQUIVO, true);
        PrintWriter pw = new PrintWriter(fw)) {

            pw.println(entrega.getData() + "," + entrega.getSucessos() + "," + entrega.getFalhas());
            System.out.println("Dados gravados com sucesso!");
        } catch (IOException e) {
            System.out.println("ERRO ao salvar no CSV: " + e.getMessage());
        }
    }

    public List<Entrega> buscarTodas(){
        List<Entrega> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))){
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;            
                try{
                    String[] dados = linha.split(",");
                    //conversão dos dados em segurança
                    LocalDate data = LocalDate.parse(dados[0].trim());
                    int sucesso = Integer.parseInt(dados[1].trim());
                    int falha = Integer.parseInt(dados[2].trim());                   
                    
                    lista.add(new Entrega(data, sucesso, falha));
                } catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException e){
                    //se der algum erro em uma linha, ele cai aqui, avisa e vai pra próxima linha
                    System.err.println("AVISO: Linha inválida ignorada nos CSV -> " + linha);
                }
            }
        } catch (IOException e) {
            // Se o arquivo não existir, retornamos a lista vazia sem crashar
        }
        return lista;
    }
}
