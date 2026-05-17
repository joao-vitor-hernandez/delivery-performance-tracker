package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Usuario;

public class UsuarioRepository {
    //método salvar um novo usuário
    public boolean salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, senha_hash) VALUES (?,?)";

        try(Connection conn = ConexaoSQLite.conectar();
            PreparedStatement pstmt =conn.prepareStatement(sql)){

            pstmt.setString(1, usuario.getUsername().toLowerCase().trim());
            pstmt.setString(2, usuario.getSenhaHash());

            pstmt.executeUpdate();
            return true; //cadastro feito com sucesso
        } catch (SQLException e){
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("\nERRO: Este nome de usuário já está cadastrado!");
            } else {
                System.err.println("ERRO: ao cadastrar usuário: " + e.getMessage());
            }
            return false;
        }
    }

    //método par buscar um usuário pelo nome
    public Usuario buscarPorUsername(String username){
        String sql = "SELECT id, username, senha_hash FROM usuarios WHERE username = ?";

        try(Connection conn = ConexaoSQLite.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, username.toLowerCase().trim());

            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    //se encontrar o usuário no banco de dados, monta o objeto e devolve
                    return new Usuario(rs.getInt("id"), rs.getString("username"), rs.getString("senha_hash"));
                }
            }
        } catch (SQLException e){
            System.err.println("ERRO: ao buscar usuário: " + e.getMessage());
        }

        return null;
    }
}
