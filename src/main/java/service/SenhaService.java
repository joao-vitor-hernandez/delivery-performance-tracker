package service;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaService {
    //transforma a senha simples em hash
    public static String gerarHash(String senhaLimpa) {
        //gensalt() gera o "sal" aleatório que muda o resultado do hash
        return BCrypt.hashpw(senhaLimpa, BCrypt.gensalt());
    }

    //verifica se a senha que o usuário digitou bate com o hash do banco de dados
    public static boolean verificarSenha(String senhaLimpa, String hashDoBanco){
        try{
            return BCrypt.checkpw(senhaLimpa, hashDoBanco);
        } catch (Exception e){
            System.err.println("ERRO: ao validar criptografia da senha: " + e.getMessage());
            return false;
        }
    }
}
