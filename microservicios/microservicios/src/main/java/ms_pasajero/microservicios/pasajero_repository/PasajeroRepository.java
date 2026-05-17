package ms_pasajero.microservicios.pasajero_repository;

import ms_pasajero.microservicios.pasajeros_model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasajeroRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByRut(String rut);

    Optional<Model> findByEmail(String email);

    List<Model> findByNombreContainingIgnoreCase(String nombre);

    List<Model> findByActivo(Boolean activo);

    boolean existsByRut(String rut);

    boolean existsByEmail(String email);


}
