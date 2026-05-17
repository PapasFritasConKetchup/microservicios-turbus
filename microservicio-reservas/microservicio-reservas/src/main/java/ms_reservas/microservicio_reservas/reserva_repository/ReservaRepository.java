package ms_reservas.microservicio_reservas.reserva_repository;

import ms_reservas.microservicio_reservas.reserva_model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByPasajeroId(Long pasajeroId);

    List<Reserva> findByViajeId(Long viajeId);

    List<Reserva> findByEstadoContainingIgnoreCase(String estado);

    boolean existsByViajeIdAndAsientoIdAndEstado(Long viajeId, Long asientoId, String estado);
}
