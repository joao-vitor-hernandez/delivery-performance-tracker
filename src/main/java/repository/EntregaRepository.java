package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Entrega;


public class EntregaRepository {
    //método para salvar no banco de dados
    public void salvar(Entrega entrega){
        //comando SQL com ? que servem como caixas vazias para proteger contra SQL Injection
        String sql = "INSERT INTO entregas (usuario_id, data, sucessos, falhas) VALUES (?,?,?,?)";

        //abrimos a conexão usando a classe ConexaoSQLite
        try(Connection conn = ConexaoSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            //preenchemos os ? com os dados da entrega antes de enviar pro banco de dados
            pstmt.setInt(1, entrega.getUsuarioId());
            pstmt.setString(2, entrega.getData().toString());
            pstmt.setInt(3, entrega.getSucessos());
            pstmt.setInt(4, entrega.getFalhas());

            pstmt.executeUpdate(); //executa o comando de inserção no banco de dados
            System.out.println("Dados gravados com sucesso no Banco de Dados!");
        }catch(SQLException e){
            System.err.println("ERRO: ao salvar no Banco de Dados: " + e.getMessage());
        }
    }

    //método para buscar todos os registros do banco de dados
    public List<Entrega> buscarPorUsuario(int usuarioId){
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT id, usuario_id, data, sucessos, falhas FROM entregas WHERE usuario_id = ?";

        try (Connection conn = ConexaoSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1, usuarioId);

                try(ResultSet rs = pstmt.executeQuery()){//ResultSet guarda as linhas que o banco de dados devolveu
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        int userId = rs.getInt("usuario_id");
                        LocalDate data = LocalDate.parse(rs.getString("data"));
                        int sucesso = rs.getInt("sucessos");
                        int falha = rs.getInt("falhas");

                        //transforma a linha do banco de dados em um objeto Entrega e adiciona na lista
                        lista.add(new Entrega(id, userId, data, sucesso, falha));
                    }
                }
            }catch (SQLException e){
                System.err.println("ERRO: ao buscar dados do banco: " + e.getMessage());
            }
        return lista;
    }
}