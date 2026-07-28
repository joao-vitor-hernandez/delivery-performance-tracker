package com.entregas.security;

import com.entregas.model.Usuario;
import com.entregas.repository.UsuarioRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private static final String DEFAULT_ROLE = "ROLE_USER";
    
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado."));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getSenhaHash())
                .authorities(List.of(new SimpleGrantedAuthority(DEFAULT_ROLE)))
                .build();
    }
}