package ms_reservas.microservicio_reservas.reserva_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reservas", description = "API para la gestión de reservas de asientos en viajes Turbus")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaService reservaService;

    // ─── GET todos ────────────────────────────────────────────────────────────

    @Operation(
            summary = "Obtener todas las reservas",
            description = "Retorna la lista completa de reservas registradas en el sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de reservas obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Reserva.class),
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "pasajeroId": 10,
                        "viajeId": 20,
                        "asientoId": 30,
                        "descuentoId": null,
                        "fechaReserva": "2025-06-14T10:00:00",
                        "estado": "CONFIRMADA"
                      }
                    ]
                """)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        logger.info("Solicitud GET para obtener todas las reservas");
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    // ─── GET por ID ───────────────────────────────────────────────────────────

    @Operation(
            summary = "Obtener reserva por ID",
            description = "Busca y retorna una reserva específica según su ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Reserva.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "pasajeroId": 10,
                      "viajeId": 20,
                      "asientoId": 30,
                      "descuentoId": null,
                      "fechaReserva": "2025-06-14T10:00:00",
                      "estado": "CONFIRMADA"
                    }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    {
                      "error": "Reserva con ID 99 no encontrada"
                    }
                """)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(
            @Parameter(description = "ID de la reserva a buscar", example = "1")
            @PathVariable Long id) {
        logger.info("Solicitud GET para obtener reserva con ID: {}", id);
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    // ─── GET por pasajero ─────────────────────────────────────────────────────

    @Operation(
            summary = "Buscar reservas por pasajero",
            description = "Retorna todas las reservas asociadas a un pasajero específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservas del pasajero obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reserva.class))
            )
    })
    @GetMapping("/pasajero/{pasajeroId}")
    public ResponseEntity<List<Reserva>> buscarPorPasajero(
            @Parameter(description = "ID del pasajero", example = "10")
            @PathVariable Long pasajeroId) {
        logger.info("Solicitud GET para buscar reservas por pasajero ID: {}", pasajeroId);
        return ResponseEntity.ok(reservaService.buscarPorPasajero(pasajeroId));
    }

    // ─── GET por viaje ────────────────────────────────────────────────────────

    @Operation(
            summary = "Buscar reservas por viaje",
            description = "Retorna todas las reservas asociadas a un viaje específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservas del viaje obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reserva.class))
            )
    })
    @GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Reserva>> buscarPorViaje(
            @Parameter(description = "ID del viaje", example = "20")
            @PathVariable Long viajeId) {
        logger.info("Solicitud GET para buscar reservas por viaje ID: {}", viajeId);
        return ResponseEntity.ok(reservaService.buscarPorViaje(viajeId));
    }

    // ─── GET por estado ───────────────────────────────────────────────────────

    @Operation(
            summary = "Buscar reservas por estado",
            description = "Retorna reservas filtradas por estado: CONFIRMADA, CANCELADA o PENDIENTE"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservas filtradas por estado obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reserva.class))
            )
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reserva>> buscarPorEstado(
            @Parameter(description = "Estado de la reserva", example = "CONFIRMADA")
            @PathVariable String estado) {
        logger.info("Solicitud GET para buscar reservas por estado: {}", estado);
        return ResponseEntity.ok(reservaService.buscarPorEstado(estado));
    }

    // ─── POST crear ───────────────────────────────────────────────────────────

    @Operation(
            summary = "Crear una nueva reserva",
            description = "Crea una reserva validando que el pasajero esté activo, el viaje esté PROGRAMADO y el asiento esté disponible"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reserva creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Reserva.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "pasajeroId": 10,
                      "viajeId": 20,
                      "asientoId": 30,
                      "descuentoId": null,
                      "fechaReserva": "2025-06-14T10:00:00",
                      "estado": "CONFIRMADA"
                    }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o regla de negocio violada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    {
                      "error": "El asiento seleccionado no está disponible"
                    }
                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pasajero, viaje o asiento no encontrado",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<Reserva> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la reserva a crear",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                        {
                          "pasajeroId": 10,
                          "viajeId": 20,
                          "asientoId": 30,
                          "descuentoId": null
                        }
                    """)
                    )
            )
            @Valid @RequestBody ReservaDTO reservaDTO) {
        logger.info("Solicitud POST para crear reserva");
        Reserva reservaCreada = reservaService.crear(reservaDTO);
        return ResponseEntity.ok(reservaCreada);
    }

    // ─── PUT actualizar ───────────────────────────────────────────────────────

    @Operation(
            summary = "Actualizar una reserva existente",
            description = "Actualiza los datos de una reserva. El viaje debe seguir en estado PROGRAMADO"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reserva.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(
            @Parameter(description = "ID de la reserva a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO reservaDTO) {
        logger.info("Solicitud PUT para actualizar reserva con ID: {}", id);
        return ResponseEntity.ok(reservaService.actualizar(id, reservaDTO));
    }

    // ─── PATCH cancelar ───────────────────────────────────────────────────────

    @Operation(
            summary = "Cancelar una reserva",
            description = "Cambia el estado de la reserva a CANCELADA y libera el asiento en el bus"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID de la reserva a cancelar", example = "1")
            @PathVariable Long id) {
        logger.info("Solicitud PATCH para cancelar reserva con ID: {}", id);
        reservaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    // ─── DELETE eliminar ──────────────────────────────────────────────────────

    @Operation(
            summary = "Eliminar una reserva",
            description = "Elimina permanentemente una reserva del sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la reserva a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("Solicitud DELETE para eliminar reserva con ID: {}", id);
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
