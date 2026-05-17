package ms_pasajero.microservicios.pasajero_controller;

import jakarta.validation.Valid;
import ms_pasajero.microservicios.pasajeroDTO.PasajeroDTO;
import ms_pasajero.microservicios.pasajero_service.PasajeroService;
import ms_pasajero.microservicios.pasajeros_model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {
    private static final Logger logger = LoggerFactory.getLogger(PasajeroController.class);

    @Autowired
    private PasajeroService pasajeroService ;

    @GetMapping
    public ResponseEntity<List<Model>> obtenerTodos() {
        logger.info("Solicitud GET para obtener todos los pasajeros");
        List<Model> pasajeros = pasajeroService.obtenerTodos();
        return ResponseEntity.ok(pasajeros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> obtenerPorId(@PathVariable Long id) {
        logger.info("Solicitud GET para obtener pasajero con ID: {}", id);
        Model pasajero = pasajeroService.obtenerPorId(id);
        return ResponseEntity.ok(pasajero);
    }

    @PostMapping
    public ResponseEntity<Model> crear(@Valid @RequestBody PasajeroDTO pasajeroDTO) {
        logger.info("Solicitud POST para crear pasajero");
        Model nuevoPasajero = pasajeroService.crear(pasajeroDTO);
        return ResponseEntity.ok(nuevoPasajero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PasajeroDTO pasajeroDTO) {

        logger.info("Solicitud PUT para actualizar pasajero con ID: {}", id);
        Model pasajeroActualizado = pasajeroService.actualizar(id, pasajeroDTO);
        return ResponseEntity.ok(pasajeroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar pasajero con ID: {}", id);
        pasajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
