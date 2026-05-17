package ms_buses.Microservicio_buses.asiento_repository;

import ms_buses.Microservicio_buses.buses_model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Long> {
    List<Asiento> findByBusId(Long busId);

    List<Asiento> findByBusIdAndDisponible(Long busId, Boolean disponible);
}
