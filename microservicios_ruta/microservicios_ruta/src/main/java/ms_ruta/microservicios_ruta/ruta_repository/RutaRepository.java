package ms_ruta.microservicios_ruta.ruta_repository;

import ms_ruta.microservicios_ruta.rutas_model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    List<Ruta> findByActiva(Boolean activa);

    List<Ruta> findByCiudadOrigenContainingIgnoreCase(String ciudadOrigen);

    List<Ruta> findByCiudadDestinoContainingIgnoreCase(String ciudadDestino);

    List<Ruta> findByCiudadOrigenContainingIgnoreCaseAndCiudadDestinoContainingIgnoreCase(
            String ciudadOrigen,
            String ciudadDestino
    );
}
