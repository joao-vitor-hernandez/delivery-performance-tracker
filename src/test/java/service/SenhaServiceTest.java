package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class SenhaServiceTest {
    
    @Test
    @DisplayName("Deve garantir que o hash gerado é validável pelo BCrypt")
    void deveGerarHashValido() {
        String senhaLimpa = "minhaSenha123";
        String hashGerado = SenhaService.gerarHash(senhaLimpa);

        // O BCrypt.checkpw verifica se a senha limpa corresponde ao hash
        assertTrue(BCrypt.checkpw(senhaLimpa, hashGerado), "O hash gerado não pôde ser validado pelo BCrypt!");
    }
    @Test
    @DisplayName("Deve validar o acesso quando a senha informada for correta")
    void deveValidarSenhaCorreta(){
        String senhaLimpa = "admin123";
        String hashDoBanco = SenhaService.gerarHash(senhaLimpa);

        boolean isValid = SenhaService.verificarSenha(senhaLimpa, hashDoBanco);
        assertTrue(isValid, "O sistema falhou ao validar uma senha correta.");
    }
    @Test
    @DisplayName("STRESS: Deve bloquear o acesso quando a senha for incorreta")
    void deveRejeitarSenhaIncorreta(){
        String senhaReal = "admin123";
        String senhaDigitadaNoLogin = "hacker321";
        String hashDoBanco = SenhaService.gerarHash(senhaReal);

        boolean isValid = SenhaService.verificarSenha(senhaDigitadaNoLogin, hashDoBanco);
        assertFalse(isValid, "FALHA DE SEGURANÇA: O sistema aceitou uma senha incorreta.");
    }
}
