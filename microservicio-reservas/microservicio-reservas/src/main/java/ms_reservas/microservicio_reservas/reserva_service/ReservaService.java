package ms_reservas.microservicio_reservas.reserva_service;

import jakarta.transaction.Transactional;
import ms_reservas.microservicio_reservas.reservaDTO.AsientoResponse;
import ms_reservas.microservicio_reservas.reservaDTO.PasajeroResponse;
import ms_reservas.microservicio_reservas.reservaDTO.ReservaDTO;
import ms_reservas.microservicio_reservas.reservaDTO.ViajeResponse;
import ms_reservas.microservicio_reservas.reservaEXCEPTION.ResourceNotFoundException;
import ms_reservas.microservicio_reservas.reserva_model.Reserva;
import ms_reservas.microservicio_reservas.reserva_repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservaService {
    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String PASAJEROS_URL = "http://localhost:8081/api/pasajeros";
    private static final String VIAJES_URL = "http://localhost:8085/api/viajes";
    private static final String BUSES_URL = "http://localhost:8084/api/buses";

    public List<Reserva> obtenerTodas() {
        logger.info("Obteniendo todas las reservas");
        List<Reserva> reservas = reservaRepository.findAll();
        logger.info("Se encontraron {} reservas", reservas.size());
        return reservas;
    }

    public Reserva obtenerPorId(Long id) {
        logger.info("Buscando reserva con ID: {}", id);

        return reservaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reserva con ID {} no encontrada", id);
                    return new ResourceNotFoundException("Reserva con ID " + id + " no encontrada");
                });
    }

    public List<Reserva> buscarPorPasajero(Long pasajeroId) {
        logger.info("Buscando reservas del pasajero ID: {}", pasajeroId);
        return reservaRepository.findByPasajeroId(pasajeroId);
    }

    public List<Reserva> buscarPorViaje(Long viajeId) {
        logger.info("Buscando reservas del viaje ID: {}", viajeId);
        return reservaRepository.findByViajeId(viajeId);
    }

    public List<Reserva> buscarPorEstado(String estado) {
        logger.info("Buscando reservas con estado: {}", estado);
        return reservaRepository.findByEstadoContainingIgnoreCase(estado);
    }

    public Reserva crear(ReservaDTO reservaDTO) {
        logger.info("Creando reserva para pasajero {}, viaje {} y asiento {}",
                reservaDTO.getPasajeroId(),
                reservaDTO.getViajeId(),
                reservaDTO.getAsientoId());

        PasajeroResponse pasajero = obtenerPasajeroRemoto(reservaDTO.getPasajeroId());
        ViajeResponse viaje = obtenerViajeRemoto(reservaDTO.getViajeId());
        AsientoResponse asiento = obtenerAsientoRemoto(viaje.getBusId(), reservaDTO.getAsientoId());

        if (pasajero.getActivo() != null && !pasajero.getActivo()) {
            throw new RuntimeException("El pasajero seleccionado no está activo");
        }

        if (!"PROGRAMADO".equalsIgnoreCase(viaje.getEstado())) {
            throw new RuntimeException("Solo se pueden reservar asientos en viajes programados");
        }

        if (asiento.getDisponible() != null && !asiento.getDisponible()) {
            throw new RuntimeException("El asiento seleccionado no está disponible");
        }

        boolean yaReservado = reservaRepository.existsByViajeIdAndAsientoIdAndEstado(
                reservaDTO.getViajeId(),
                reservaDTO.getAsientoId(),
                "CONFIRMADA"
        );

        if (yaReservado) {
            throw new RuntimeException("El asiento ya está reservado para este viaje");
        }

        Reserva reserva = new Reserva();
        reserva.setPasajeroId(reservaDTO.getPasajeroId());
        reserva.setViajeId(reservaDTO.getViajeId());
        reserva.setAsientoId(reservaDTO.getAsientoId());
        reserva.setDescuentoId(reservaDTO.getDescuentoId());
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(reserva);

        bloquearAsientoRemoto(reservaDTO.getAsientoId());

        logger.info("Reserva creada correctamente con ID: {}", reservaGuardada.getId());
        return reservaGuardada;
    }

    public Reserva actualizar(Long id, ReservaDTO reservaDTO) {
        logger.info("Actualizando reserva con ID: {}", id);

        Reserva reservaExistente = obtenerPorId(id);

        PasajeroResponse pasajero = obtenerPasajeroRemoto(reservaDTO.getPasajeroId());
        ViajeResponse viaje = obtenerViajeRemoto(reservaDTO.getViajeId());
        AsientoResponse asiento = obtenerAsientoRemoto(viaje.getBusId(), reservaDTO.getAsientoId());

        if (pasajero.getActivo() != null && !pasajero.getActivo()) {
            throw new RuntimeException("El pasajero seleccionado no está activo");
        }

        if (!"PROGRAMADO".equalsIgnoreCase(viaje.getEstado())) {
            throw new RuntimeException("Solo se pueden actualizar reservas de viajes programados");
        }

        if (asiento.getDisponible() != null && !asiento.getDisponible()
                && !reservaExistente.getAsientoId().equals(reservaDTO.getAsientoId())) {
            throw new RuntimeException("El asiento seleccionado no está disponible");
        }

        reservaExistente.setPasajeroId(reservaDTO.getPasajeroId());
        reservaExistente.setViajeId(reservaDTO.getViajeId());
        reservaExistente.setAsientoId(reservaDTO.getAsientoId());
        reservaExistente.setDescuentoId(reservaDTO.getDescuentoId());

        Reserva reservaActualizada = reservaRepository.save(reservaExistente);

        logger.info("Reserva actualizada correctamente con ID: {}", reservaActualizada.getId());
        return reservaActualizada;
    }

    public void cancelar(Long id) {
        logger.info("Cancelando reserva con ID: {}", id);

        Reserva reserva = obtenerPorId(id);
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);

        liberarAsientoRemoto(reserva.getAsientoId());

        logger.info("Reserva cancelada correctamente");
    }

    public void eliminar(Long id) {
        logger.info("Eliminando reserva con ID: {}", id);

        Reserva reserva = obtenerPorId(id);
        reservaRepository.delete(reserva);

        logger.info("Reserva eliminada correctamente");
    }

    private PasajeroResponse obtenerPasajeroRemoto(Long pasajeroId) {
        logger.info("Consultando pasajeros-service para pasajero ID: {}", pasajeroId);

        return webClientBuilder.build()
                .get()
                .uri(PASAJEROS_URL + "/" + pasajeroId)
                .retrieve()
                .bodyToMono(PasajeroResponse.class)
                .block();
    }

    private ViajeResponse obtenerViajeRemoto(Long viajeId) {
        logger.info("Consultando viajes-service para viaje ID: {}", viajeId);

        return webClientBuilder.build()
                .get()
                .uri(VIAJES_URL + "/" + viajeId)
                .retrieve()
                .bodyToMono(ViajeResponse.class)
                .block();
    }

    private AsientoResponse obtenerAsientoRemoto(Long busId, Long asientoId) {
        logger.info("Consultando buses-service para asiento ID: {} del bus ID: {}", asientoId, busId);

        List<AsientoResponse> asientos = webClientBuilder.build()
                .get()
                .uri(BUSES_URL + "/" + busId + "/asientos")
                .retrieve()
                .bodyToFlux(AsientoResponse.class)
                .collectList()
                .block();

        if (asientos == null) {
            throw new RuntimeException("No se pudieron obtener los asientos del bus");
        }

        return asientos.stream()
                .filter(asiento -> asiento.getId().equals(asientoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Asiento con ID " + asientoId + " no encontrado en el bus " + busId));
    }

    private void bloquearAsientoRemoto(Long asientoId) {
        logger.info("Bloqueando asiento ID: {}", asientoId);

        webClientBuilder.build()
                .patch()
                .uri(BUSES_URL + "/asientos/" + asientoId + "/disponibilidad?disponible=false")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private void liberarAsientoRemoto(Long asientoId) {
        logger.info("Liberando asiento ID: {}", asientoId);

        webClientBuilder.build()
                .patch()
                .uri(BUSES_URL + "/asientos/" + asientoId + "/disponibilidad?disponible=true")
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
