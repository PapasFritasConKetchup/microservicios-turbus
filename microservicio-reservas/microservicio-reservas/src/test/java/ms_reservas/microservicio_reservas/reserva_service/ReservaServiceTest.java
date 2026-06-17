package ms_reservas.microservicio_reservas.reserva_service;
import ms_reservas.microservicio_reservas.reservaDTO.AsientoResponse;
import ms_reservas.microservicio_reservas.reservaDTO.PasajeroResponse;
import ms_reservas.microservicio_reservas.reservaDTO.ReservaDTO;
import ms_reservas.microservicio_reservas.reservaDTO.ViajeResponse;
import ms_reservas.microservicio_reservas.reservaEXCEPTION.ResourceNotFoundException;
import ms_reservas.microservicio_reservas.reserva_model.Reserva;
import ms_reservas.microservicio_reservas.reserva_repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - ReservaService")
public class ReservaServiceTest {
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;

    @InjectMocks
    private ReservaService reservaService;

    // ─── Datos de prueba reutilizables ────────────────────────────────────────

    private Reserva reservaConfirmada;
    private ReservaDTO reservaDTO;
    private PasajeroResponse pasajeroActivo;
    private ViajeResponse viajeProgramado;
    private AsientoResponse asientoDisponible;

    @BeforeEach
    void setUp() {
        // Reserva ya persistida
        reservaConfirmada = new Reserva();
        reservaConfirmada.setId(1L);
        reservaConfirmada.setPasajeroId(10L);
        reservaConfirmada.setViajeId(20L);
        reservaConfirmada.setAsientoId(30L);
        reservaConfirmada.setFechaReserva(LocalDateTime.now());
        reservaConfirmada.setEstado("CONFIRMADA");

        // DTO de entrada para crear/actualizar
        reservaDTO = new ReservaDTO();
        reservaDTO.setPasajeroId(10L);
        reservaDTO.setViajeId(20L);
        reservaDTO.setAsientoId(30L);

        // Respuestas remotas felices
        pasajeroActivo = new PasajeroResponse();
        pasajeroActivo.setActivo(true);

        viajeProgramado = new ViajeResponse();
        viajeProgramado.setEstado("PROGRAMADO");
        viajeProgramado.setBusId(5L);

        asientoDisponible = new AsientoResponse();
        asientoDisponible.setId(30L);
        asientoDisponible.setDisponible(true);
    }

    // =========================================================================
    // obtenerTodas()
    // =========================================================================

    @Test
    @DisplayName("obtenerTodas - retorna lista con todas las reservas")
    void obtenerTodas_retornaListaCompleta() {
        // GIVEN
        List<Reserva> lista = List.of(reservaConfirmada, new Reserva());
        when(reservaRepository.findAll()).thenReturn(lista);

        // WHEN
        List<Reserva> resultado = reservaService.obtenerTodas();

        // THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas - retorna lista vacía cuando no hay reservas")
    void obtenerTodas_retornaListaVacia() {
        // GIVEN
        when(reservaRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<Reserva> resultado = reservaService.obtenerTodas();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // =========================================================================
    // obtenerPorId()
    // =========================================================================

    @Test
    @DisplayName("obtenerPorId - retorna reserva cuando existe el ID")
    void obtenerPorId_existeId_retornaReserva() {
        // GIVEN
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaConfirmada));

        // WHEN
        Reserva resultado = reservaService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(reservaRepository).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza ResourceNotFoundException cuando el ID no existe")
    void obtenerPorId_noExisteId_lanzaExcepcion() {
        // GIVEN
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> reservaService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    // =========================================================================
    // buscarPorPasajero()
    // =========================================================================

    @Test
    @DisplayName("buscarPorPasajero - retorna reservas del pasajero indicado")
    void buscarPorPasajero_retornaReservasDelPasajero() {
        // GIVEN
        when(reservaRepository.findByPasajeroId(10L)).thenReturn(List.of(reservaConfirmada));

        // WHEN
        List<Reserva> resultado = reservaService.buscarPorPasajero(10L);

        // THEN
        assertFalse(resultado.isEmpty());
        assertEquals(10L, resultado.get(0).getPasajeroId());
    }

    // =========================================================================
    // buscarPorEstado()
    // =========================================================================

    @Test
    @DisplayName("buscarPorEstado - retorna reservas que coinciden con el estado")
    void buscarPorEstado_retorvaReservasConEstado() {
        // GIVEN
        when(reservaRepository.findByEstadoContainingIgnoreCase("CONFIRMADA"))
                .thenReturn(List.of(reservaConfirmada));

        // WHEN
        List<Reserva> resultado = reservaService.buscarPorEstado("CONFIRMADA");

        // THEN
        assertEquals(1, resultado.size());
        assertEquals("CONFIRMADA", resultado.get(0).getEstado());
    }

    // =========================================================================
    // crear() — reglas de negocio
    // =========================================================================

    @Test
    @DisplayName("crear - lanza excepción cuando el pasajero está inactivo")
    void crear_pasajeroInactivo_lanzaExcepcion() {
        // GIVEN
        pasajeroActivo.setActivo(false);
        configurarWebClientGet(pasajeroActivo, viajeProgramado, List.of(asientoDisponible));

        // WHEN / THEN
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> reservaService.crear(reservaDTO)
        );
        assertEquals("El pasajero seleccionado no está activo", ex.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear - lanza excepción cuando el viaje no está programado")
    void crear_viajeNoProgramado_lanzaExcepcion() {
        // GIVEN
        viajeProgramado.setEstado("CANCELADO");
        configurarWebClientGet(pasajeroActivo, viajeProgramado, List.of(asientoDisponible));

        // WHEN / THEN
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> reservaService.crear(reservaDTO)
        );
        assertEquals("Solo se pueden reservar asientos en viajes programados", ex.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear - lanza excepción cuando el asiento no está disponible")
    void crear_asientoNoDisponible_lanzaExcepcion() {
        // GIVEN
        asientoDisponible.setDisponible(false);
        configurarWebClientGet(pasajeroActivo, viajeProgramado, List.of(asientoDisponible));

        // WHEN / THEN
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> reservaService.crear(reservaDTO)
        );
        assertEquals("El asiento seleccionado no está disponible", ex.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear - lanza excepción cuando el asiento ya está reservado para ese viaje")
    void crear_asientoYaReservado_lanzaExcepcion() {
        // GIVEN
        configurarWebClientGet(pasajeroActivo, viajeProgramado, List.of(asientoDisponible));
        when(reservaRepository.existsByViajeIdAndAsientoIdAndEstado(20L, 30L, "CONFIRMADA"))
                .thenReturn(true);

        // WHEN / THEN
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> reservaService.crear(reservaDTO)
        );
        assertEquals("El asiento ya está reservado para este viaje", ex.getMessage());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear - guarda reserva y bloquea asiento cuando todo es válido")
    void crear_datosValidos_guardaReservaYBloqueaAsiento() {
        // GIVEN
        configurarWebClientGet(pasajeroActivo, viajeProgramado, List.of(asientoDisponible));
        when(reservaRepository.existsByViajeIdAndAsientoIdAndEstado(20L, 30L, "CONFIRMADA"))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaConfirmada);
        configurarWebClientPatch();

        // WHEN
        Reserva resultado = reservaService.crear(reservaDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    // =========================================================================
    // cancelar()
    // =========================================================================

    @Test
    @DisplayName("cancelar - cambia estado a CANCELADA y libera el asiento")
    void cancelar_reservaExistente_cambiaEstadoYLiberaAsiento() {
        // GIVEN
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaConfirmada));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaConfirmada);
        configurarWebClientPatch();

        // WHEN
        reservaService.cancelar(1L);

        // THEN
        assertEquals("CANCELADA", reservaConfirmada.getEstado());
        verify(reservaRepository).save(reservaConfirmada);
    }

    @Test
    @DisplayName("cancelar - lanza excepción si la reserva no existe")
    void cancelar_reservaNoExiste_lanzaExcepcion() {
        // GIVEN
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.cancelar(99L));
        verify(reservaRepository, never()).save(any());
    }

    // =========================================================================
    // eliminar()
    // =========================================================================

    @Test
    @DisplayName("eliminar - elimina la reserva cuando el ID existe")
    void eliminar_reservaExistente_eliminaCorrectamente() {
        // GIVEN
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaConfirmada));
        doNothing().when(reservaRepository).delete(reservaConfirmada);

        // WHEN
        reservaService.eliminar(1L);

        // THEN
        verify(reservaRepository).delete(reservaConfirmada);
    }

    @Test
    @DisplayName("eliminar - lanza excepción si la reserva no existe")
    void eliminar_reservaNoExiste_lanzaExcepcion() {
        // GIVEN
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.eliminar(99L));
        verify(reservaRepository, never()).delete(any());
    }

    // =========================================================================
// Helpers privados para mockear WebClient
// =========================================================================

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void configurarWebClientGet(PasajeroResponse pasajero,
                                        ViajeResponse viaje,
                                        List<AsientoResponse> asientos) {
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec respSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(respSpec);

        when(respSpec.bodyToMono(PasajeroResponse.class)).thenReturn(Mono.just(pasajero));
        when(respSpec.bodyToMono(ViajeResponse.class)).thenReturn(Mono.just(viaje));
        when(respSpec.bodyToFlux(AsientoResponse.class)).thenReturn(Flux.fromIterable(asientos));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void configurarWebClientPatch() {
        WebClient.RequestBodyUriSpec bodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.ResponseSpec respSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.patch()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(respSpec);
        when(respSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());
    }
}
