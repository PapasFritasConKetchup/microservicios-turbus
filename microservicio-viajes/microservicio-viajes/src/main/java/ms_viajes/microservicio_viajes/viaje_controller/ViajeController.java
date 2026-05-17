package ms_viajes.microservicio_viajes.viaje_controller;

import jakarta.validation.Valid;
import ms_viajes.microservicio_viajes.viajeDTO.ViajeDTO;
import ms_viajes.microservicio_viajes.viaje_model.Viaje;
import ms_viajes.microservicio_viajes.viaje_service.ViajeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {
    private static final Logger logger = LoggerFactory.getLogger(ViajeController.class);

    @Autowired
    private ViajeService viajeService;

    @GetMapping
    public ResponseEntity<List<Viaje>> obtenerTodos() {
        logger.info("Solicitud GET para obtener todos los viajes");
        return ResponseEntity.ok(viajeService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        logger.info("Solicitud GET para obtener viaje con ID: {}", id);
        return ResponseEntity.ok(viajeService.obtenerPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Viaje>> buscarPorEstado(@PathVariable String estado) {
        logger.info("Solicitud GET para buscar viajes por estado: {}", estado);
        return ResponseEntity.ok(viajeService.buscarPorEstado(estado));
    }

    @GetMapping("/ruta/{rutaId}")
    public ResponseEntity<List<Viaje>> buscarPorRuta(@PathVariable Long rutaId) {
        logger.info("Solicitud GET para buscar viajes por ruta ID: {}", rutaId);
        return ResponseEntity.ok(viajeService.buscarPorRuta(rutaId));
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<Viaje>> buscarPorBus(@PathVariable Long busId) {
        logger.info("Solicitud GET para buscar viajes por bus ID: {}", busId);
        return ResponseEntity.ok(viajeService.buscarPorBus(busId));
    }

    @GetMapping("/trabajador/{trabajadorId}")
    public ResponseEntity<List<Viaje>> buscarPorTrabajador(@PathVariable Long trabajadorId) {
        logger.info("Solicitud GET para buscar viajes por trabajador ID: {}", trabajadorId);
        return ResponseEntity.ok(viajeService.buscarPorTrabajador(trabajadorId));
    }

    @GetMapping("/fecha/{fechaSalida}")
    public ResponseEntity<List<Viaje>> buscarPorFecha(@PathVariable LocalDate fechaSalida) {
        logger.info("Solicitud GET para buscar viajes por fecha: {}", fechaSalida);
        return ResponseEntity.ok(viajeService.buscarPorFecha(fechaSalida));
    }

    @PostMapping
    public ResponseEntity<Viaje> crear(@Valid @RequestBody ViajeDTO viajeDTO) {
        logger.info("Solicitud POST para crear viaje");
        Viaje viajeCreado = viajeService.crear(viajeDTO);
        return ResponseEntity.ok(viajeCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Viaje> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ViajeDTO viajeDTO) {

        logger.info("Solicitud PUT para actualizar viaje con ID: {}", id);
        Viaje viajeActualizado = viajeService.actualizar(id, viajeDTO);
        return ResponseEntity.ok(viajeActualizado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        logger.info("Solicitud PATCH para cambiar estado del viaje ID: {}", id);
        viajeService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar viaje con ID: {}", id);
        viajeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
