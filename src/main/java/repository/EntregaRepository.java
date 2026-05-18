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
    public List<Entrega> buscarPorUsuario(int usuarioId, int mes, int ano){
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT id, usuario_id, data, sucessos, falhas FROM entregas WHERE usuario_id = ? AND data LIKE ?";

        try (Connection conn = ConexaoSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setInt(1, usuarioId);
                String anoMesPesquisa = String.format("%04d-%02d-%%", ano, mes);
                pstmt.setString(2, anoMesPesquisa);

                try(ResultSet rs = pstmt.executeQuery()){//ResultSet guarda as linhas que o banco de dados devolveu
                    while (rs.next()) {lista.add(new Entrega(rs.getInt("id"), rs.getInt("usuario_id"), LocalDate.parse(rs.getString("data")), rs.getInt("sucessos"), rs.getInt("falhas")));
                    }
                }
            }catch (SQLException e){
                System.err.println("ERRO: ao buscar dados do banco: " + e.getMessage());
            }
        return lista;
    }
    public Entrega buscarPorDataEUsuario(int usuarioId, LocalDate data){
        String sql = "SELECT id, usuario_id, data, sucessos, falhas FROM entregas WHERE usuario_id = ? AND data = ?";
        try (Connection conn = ConexaoSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setInt(1, usuarioId);
            pstmt.setString(2, data.toString());
            try(ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    return new Entrega(rs.getInt("id"), rs.getInt("usuario_id"), LocalDate.parse(rs.getString("data")), rs.getInt("sucessos"), rs.getInt("falhas"));
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
        return null;
    }
    public void atualizar(Entrega entrega){
        String sql = "UPDATE entregas SET sucessos = ?, falhas = ? WHERE id = ?";
        try(Connection conn = ConexaoSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, entrega.getSucessos());
            pstmt.setInt(2, entrega.getFalhas());
            pstmt.setInt(3, entrega.getId());
            pstmt.executeUpdate();
            System.out.println("Dados ATUALIZADOS com sucesso no Banco de Dados!");
        } catch (SQLException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
}