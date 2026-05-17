package ms_buses.Microservicio_buses.bus_controller;

import jakarta.validation.Valid;
import ms_buses.Microservicio_buses.busDTO.BusDTO;
import ms_buses.Microservicio_buses.bus_service.BusService;
import ms_buses.Microservicio_buses.buses_model.Asiento;
import ms_buses.Microservicio_buses.buses_model.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {
    private static final Logger logger = LoggerFactory.getLogger(BusController.class);

    @Autowired
    private BusService busService ;

    @GetMapping
    public ResponseEntity<List<Bus>> obtenerTodos() {
        logger.info("Solicitud GET para obtener todos los buses");
        return ResponseEntity.ok(busService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bus> obtenerPorId(@PathVariable Long id) {
        logger.info("Solicitud GET para obtener bus con ID: {}", id);
        return ResponseEntity.ok(busService.obtenerPorId(id));
    }

    @GetMapping("/patente/{patente}")
    public ResponseEntity<Bus> obtenerPorPatente(@PathVariable String patente) {
        logger.info("Solicitud GET para obtener bus con patente: {}", patente);
        return ResponseEntity.ok(busService.obtenerPorPatente(patente));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Bus>> buscarPorEstado(@PathVariable String estado) {
        logger.info("Solicitud GET para buscar buses por estado: {}", estado);
        return ResponseEntity.ok(busService.buscarPorEstado(estado));
    }

    @GetMapping("/tipo/{tipoBus}")
    public ResponseEntity<List<Bus>> buscarPorTipoBus(@PathVariable String tipoBus) {
        logger.info("Solicitud GET para buscar buses por tipo: {}", tipoBus);
        return ResponseEntity.ok(busService.buscarPorTipoBus(tipoBus));
    }

    @GetMapping("/{busId}/asientos")
    public ResponseEntity<List<Asiento>> obtenerAsientosPorBus(@PathVariable Long busId) {
        logger.info("Solicitud GET para obtener asientos del bus ID: {}", busId);
        return ResponseEntity.ok(busService.obtenerAsientosPorBus(busId));
    }

    @GetMapping("/{busId}/asientos/disponibles")
    public ResponseEntity<List<Asiento>> obtenerAsientosDisponibles(@PathVariable Long busId) {
        logger.info("Solicitud GET para obtener asientos disponibles del bus ID: {}", busId);
        return ResponseEntity.ok(busService.obtenerAsientosDisponibles(busId));
    }

    @PostMapping
    public ResponseEntity<Bus> crear(@Valid @RequestBody BusDTO busDTO) {
        logger.info("Solicitud POST para crear bus");
        Bus busCreado = busService.crear(busDTO);
        return ResponseEntity.ok(busCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody BusDTO busDTO) {

        logger.info("Solicitud PUT para actualizar bus con ID: {}", id);
        Bus busActualizado = busService.actualizar(id, busDTO);
        return ResponseEntity.ok(busActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoBus(
            @PathVariable Long id,
            @RequestParam String estado) {

        logger.info("Solicitud PATCH para cambiar estado del bus ID: {}", id);
        busService.cambiarEstadoBus(id, estado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/asientos/{asientoId}/disponibilidad")
    public ResponseEntity<Asiento> cambiarDisponibilidadAsiento(
            @PathVariable Long asientoId,
            @RequestParam Boolean disponible) {

        logger.info("Solicitud PATCH para cambiar disponibilidad del asiento ID: {}", asientoId);
        return ResponseEntity.ok(busService.cambiarDisponibilidadAsiento(asientoId, disponible));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar bus con ID: {}", id);
        busService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
