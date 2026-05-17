package ms_trabajador.Microservicio_Trabajador.trabajador_controller;

import jakarta.validation.Valid;
import ms_trabajador.Microservicio_Trabajador.Modelo_Trabajador.Trabajador;
import ms_trabajador.Microservicio_Trabajador.trabajadorDTO.TrabajadorDTO;
import ms_trabajador.Microservicio_Trabajador.trabajador_service.TrabajadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {
    private static final Logger logger = LoggerFactory.getLogger(TrabajadorController.class);

    @Autowired
    private TrabajadorService trabajadorService;

    @GetMapping
    public ResponseEntity<List<Trabajador>> obtenerTodos() {
        logger.info("Solicitud GET para obtener todos los trabajadores");
        return ResponseEntity.ok(trabajadorService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trabajador> obtenerPorId(@PathVariable Long id) {
        logger.info("Solicitud GET para obtener trabajador con ID: {}", id);
        return ResponseEntity.ok(trabajadorService.obtenerPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<Trabajador> obtenerPorRut(@PathVariable String rut) {
        logger.info("Solicitud GET para obtener trabajador con RUT: {}", rut);
        return ResponseEntity.ok(trabajadorService.obtenerPorRut(rut));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Trabajador>> obtenerActivos() {
        logger.info("Solicitud GET para obtener trabajadores activos");
        return ResponseEntity.ok(trabajadorService.obtenerActivos());
    }

    @GetMapping("/cargo/{cargo}")
    public ResponseEntity<List<Trabajador>> buscarPorCargo(@PathVariable String cargo) {
        logger.info("Solicitud GET para buscar trabajadores por cargo: {}", cargo);
        return ResponseEntity.ok(trabajadorService.buscarPorCargo(cargo));
    }

    @PostMapping
    public ResponseEntity<Trabajador> crear(@Valid @RequestBody TrabajadorDTO trabajadorDTO) {
        logger.info("Solicitud POST para crear trabajador");
        Trabajador trabajadorCreado = trabajadorService.crear(trabajadorDTO);
        return ResponseEntity.ok(trabajadorCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trabajador> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TrabajadorDTO trabajadorDTO) {

        logger.info("Solicitud PUT para actualizar trabajador con ID: {}", id);
        Trabajador trabajadorActualizado = trabajadorService.actualizar(id, trabajadorDTO);
        return ResponseEntity.ok(trabajadorActualizado);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        logger.info("Solicitud PATCH para desactivar trabajador con ID: {}", id);
        trabajadorService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar trabajador con ID: {}", id);
        trabajadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
