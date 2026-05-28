package repository;

import java.time.LocalDate;
import java.util.List;
import model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    
    List<Entrega> findByUsuarioId(Long usuarioId);

    Entrega findByUsuarioIdAndData(Long usuarioId, LocalDate data);

    List<Entrega> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
}