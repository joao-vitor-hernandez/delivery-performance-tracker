package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Derived Query: O Spring cria o SQL automático: 
    // SELECT * FROM tb_usuario WHERE LOWER(username) = LOWER(?)
    Optional<Usuario> findByUsername(String username);
}