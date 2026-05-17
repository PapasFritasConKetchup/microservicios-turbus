package ms_reservas.microservicio_reservas.reserva_controller;

import jakarta.validation.Valid;
import ms_reservas.microservicio_reservas.reservaDTO.ReservaDTO;
import ms_reservas.microservicio_reservas.reserva_model.Reserva;
import ms_reservas.microservicio_reservas.reserva_service.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reservas")
public class ReservaController {
    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        logger.info("Solicitud GET para obtener todas las reservas");
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        logger.info("Solicitud GET para obtener reserva con ID: {}", id);
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @GetMapping("/pasajero/{pasajeroId}")
    public ResponseEntity<List<Reserva>> buscarPorPasajero(@PathVariable Long pasajeroId) {
        logger.info("Solicitud GET para buscar reservas por pasajero ID: {}", pasajeroId);
        return ResponseEntity.ok(reservaService.buscarPorPasajero(pasajeroId));
    }

    @GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Reserva>> buscarPorViaje(@PathVariable Long viajeId) {
        logger.info("Solicitud GET para buscar reservas por viaje ID: {}", viajeId);
        return ResponseEntity.ok(reservaService.buscarPorViaje(viajeId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reserva>> buscarPorEstado(@PathVariable String estado) {
        logger.info("Solicitud GET para buscar reservas por estado: {}", estado);
        return ResponseEntity.ok(reservaService.buscarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@Valid @RequestBody ReservaDTO reservaDTO) {
        logger.info("Solicitud POST para crear reserva");
        Reserva reservaCreada = reservaService.crear(reservaDTO);
        return ResponseEntity.ok(reservaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO reservaDTO) {

        logger.info("Solicitud PUT para actualizar reserva con ID: {}", id);
        Reserva reservaActualizada = reservaService.actualizar(id, reservaDTO);
        return ResponseEntity.ok(reservaActualizada);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        logger.info("Solicitud PATCH para cancelar reserva con ID: {}", id);
        reservaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar reserva con ID: {}", id);
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
