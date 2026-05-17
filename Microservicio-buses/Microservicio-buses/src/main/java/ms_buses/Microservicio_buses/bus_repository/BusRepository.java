package ms_buses.Microservicio_buses.bus_repository;

import ms_buses.Microservicio_buses.buses_model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByPatente(String patente);

    boolean existsByPatente(String patente);

    List<Bus> findByEstadoContainingIgnoreCase(String estado);

    List<Bus> findByTipoBusContainingIgnoreCase(String tipoBus);
}
