package ms_viajes.microservicio_viajes.viaje_repository;

import ms_viajes.microservicio_viajes.viaje_model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByEstadoContainingIgnoreCase(String estado);

    List<Viaje> findByRutaId(Long rutaId);

    List<Viaje> findByBusId(Long busId);

    List<Viaje> findByTrabajadorId(Long trabajadorId);

    List<Viaje> findByFechaSalida(LocalDate fechaSalida);
}
