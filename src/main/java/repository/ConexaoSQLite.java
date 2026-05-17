package repository;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLite {
    //define o nome do arquivo que vai guardar os dados e será criado na raiz do projeto
    private static final String URL = "jdbc:sqlite:banco_entregas.db";

    //método que "abre a porta" do banco de dados
    public static Connection conectar(){
        try{
            return DriverManager.getConnection(URL);
        } catch (SQLException e){
            System.err.println("ERRO: ao conectar com o banco de dados: " + e.getMessage());
            return null;
        }
    }

    //método que cria a tabela caso seja a primeira vez rodando o programa
    public static void criarTabelaSeNaoExistir() {
        String sqlEntrega = "CREATE TABLE IF NOT EXISTS entregas ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "data TEXT NOT NULL,"
                   + "sucessos INTEGER NOT NULL,"
                   + "falhas INTEGER NOT NULL"
                   + ");";
        
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                           + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                           + "username TEXT NOT NULL UNIQUE," // UNIQUE impede dois usuários com o mesmo nome
                           + "senha_hash TEXT NOT NULL"
                           + ");";
        
        try (Connection conn = conectar();
            Statement stmt = conn.createStatement()) {

            stmt.execute(sqlEntrega); //cria a tabela Entrega
            stmt.execute(sqlUsuarios); //cria a tabela Usuarios
        } catch (SQLException e){
            System.err.println("ERRO: ao criar as tabelas: " + e.getMessage());
        }
    }
}
