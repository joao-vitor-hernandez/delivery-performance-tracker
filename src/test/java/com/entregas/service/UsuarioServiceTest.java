package com.entregas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.entregas.model.Usuario;
import com.entregas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;
    private String senhaLimpa = "admin123";

    @BeforeEach
    void setUp() {
        // Simulamos o estado do banco de dados: um usuário com a senha já protegida em Hash
        String hash = BCrypt.hashpw(senhaLimpa, BCrypt.gensalt());
        usuarioMock = new Usuario("admin", hash);
    }

    @Test
    @DisplayName("Deve criptografar a senha do usuário antes de salvar no cadastro")
    void deveCriptografarSenhaNoCadastro() {
        Usuario novoUsuario = new Usuario("joao", "senha123");
        
        // Simulando que o usuário ainda não existe no sistema
        when(usuarioRepository.findByUsername("joao")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuario);

        usuarioService.cadastrarUsuario(novoUsuario);

        // Validando se o Service aplicou a criptografia no objeto antes de mandar pro Repository
        assertNotEquals("senha123", novoUsuario.getSenhaHash(), "A senha não foi criptografada!");
        assertTrue(BCrypt.checkpw("senha123", novoUsuario.getSenhaHash()), "O hash gerado pelo BCrypt é inválido.");
    }

    @Test
    @DisplayName("Deve impedir cadastro de usuário duplicado")
    void deveImpedirCadastroDuplicado() {
        Usuario usuarioDuplicado = new Usuario("admin", "senha123");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuarioMock));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrarUsuario(usuarioDuplicado));
        assertEquals("Este usuário já está cadastrado no sistema!", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}