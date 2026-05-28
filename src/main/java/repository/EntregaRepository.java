package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.Entrega;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    // O Spring gera o SQL automático: SELECT * FROM tb_entrega WHERE usuario_id = ? AND data = ?
    Entrega findByUsuarioIdAndData(int usuarioId, LocalDate data);

    // O Spring gera o SQL automático para filtrar por usuário
    List<Entrega> findByUsuarioId(int usuarioId);
}