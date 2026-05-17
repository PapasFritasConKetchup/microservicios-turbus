package ms_trabajador.Microservicio_Trabajador.TrabajadorRepository;

import ms_trabajador.Microservicio_Trabajador.Modelo_Trabajador.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface trabajador_repository extends JpaRepository<Trabajador, Long> {

    Optional<Trabajador> findByRut(String rut);

    boolean existsByRut(String rut);

    boolean existsByEmail(String email);

    List<Trabajador> findByActivo(Boolean activo);

    List<Trabajador> findByCargoContainingIgnoreCase(String cargo);
}
