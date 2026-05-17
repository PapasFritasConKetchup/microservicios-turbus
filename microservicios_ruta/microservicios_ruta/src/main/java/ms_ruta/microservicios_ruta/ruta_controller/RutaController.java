package ms_ruta.microservicios_ruta.ruta_controller;

import jakarta.validation.Valid;
import ms_ruta.microservicios_ruta.RutaDTO.rutaDTO;
import ms_ruta.microservicios_ruta.ruta_service.RutaService;
import ms_ruta.microservicios_ruta.rutas_model.Ruta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {
    private static final Logger logger = LoggerFactory.getLogger(RutaController.class);

    @Autowired
    private RutaService rutaService;

    @GetMapping
    public ResponseEntity<List<Ruta>> obtenerTodas() {
        logger.info("Solicitud GET para obtener todas las rutas");
        return ResponseEntity.ok(rutaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ruta> obtenerPorId(@PathVariable Long id) {
        logger.info("Solicitud GET para obtener ruta con ID: {}", id);
        return ResponseEntity.ok(rutaService.obtenerPorId(id));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Ruta>> obtenerActivas() {
        logger.info("Solicitud GET para obtener rutas activas");
        return ResponseEntity.ok(rutaService.obtenerActivas());
    }

    @GetMapping("/origen/{origen}")
    public ResponseEntity<List<Ruta>> buscarPorOrigen(@PathVariable String origen) {
        logger.info("Solicitud GET para buscar rutas por origen: {}", origen);
        return ResponseEntity.ok(rutaService.buscarPorOrigen(origen));
    }

    @GetMapping("/destino/{destino}")
    public ResponseEntity<List<Ruta>> buscarPorDestino(@PathVariable String destino) {
        logger.info("Solicitud GET para buscar rutas por destino: {}", destino);
        return ResponseEntity.ok(rutaService.buscarPorDestino(destino));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Ruta>> buscarPorOrigenYDestino(
            @RequestParam String origen,
            @RequestParam String destino) {

        logger.info("Solicitud GET para buscar ruta desde {} hacia {}", origen, destino);
        return ResponseEntity.ok(rutaService.buscarPorOrigenYDestino(origen, destino));
    }

    @PostMapping
    public ResponseEntity<Ruta> crear(@Valid @RequestBody rutaDTO RutaDTO) {
        logger.info("Solicitud POST para crear ruta");
        Ruta rutaCreada = rutaService.crear(RutaDTO);
        return ResponseEntity.ok(rutaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ruta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody rutaDTO RutaDTO) {

        logger.info("Solicitud PUT para actualizar ruta con ID: {}", id);
        Ruta rutaActualizada = rutaService.actualizar(id, RutaDTO);
        return ResponseEntity.ok(rutaActualizada);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        logger.info("Solicitud PATCH para desactivar ruta con ID: {}", id);
        rutaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar ruta con ID: {}", id);
        rutaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
