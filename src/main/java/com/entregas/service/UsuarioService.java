package com.entregas.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entregas.model.Usuario;
import com.entregas.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario) {
        // Regra de Negócio: Impede a duplicação de e-mail/username no sistema
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Este usuário já está cadastrado no sistema!");
        }

        String senhaCriptografada = BCrypt.hashpw(usuario.getSenhaHash(), BCrypt.gensalt());
        usuario.setSenhaHash(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    //Autentica o motorista comparando a senha digitada com o hash do banco.
    public Optional<Usuario> login(String username, String senhaPura) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            try {
                if (BCrypt.checkpw(senhaPura, usuario.getSenhaHash())) {
                    return Optional.of(usuario);
                }
            } catch (Exception e) {
                System.err.println("ERRO: ao validar criptografia da senha: " + e.getMessage());
            }
        }
        
        return Optional.empty(); // Retorna vazio caso o login ou a senha falhem
    }
}