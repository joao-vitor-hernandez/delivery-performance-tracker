package repository;

import model.Entrega;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaRepository {
    private final String CAMINHO_ARQUIVO = "entregas.csv";

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
                
                String[] partes = linha.split(",");
                if (partes.length < 3) continue;                   
                
                //transformação da linha do CSV de volta em Objeto Entrega
                Entrega e = new Entrega(java.time.LocalDate.parse(partes[0]), Integer.parseInt(partes[1]), Integer.parseInt(partes[2]));
                lista.add(e);
            }
        } catch (IOException e) {
            // Se o arquivo não existir, retornamos a lista vazia sem crashar
        }
        return lista;
    }
}
