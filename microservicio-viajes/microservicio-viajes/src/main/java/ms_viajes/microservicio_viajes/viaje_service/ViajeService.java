package ms_viajes.microservicio_viajes.viaje_service;

import jakarta.transaction.Transactional;
import ms_viajes.microservicio_viajes.viajeDTO.BusResponse;
import ms_viajes.microservicio_viajes.viajeDTO.RutaResponse;
import ms_viajes.microservicio_viajes.viajeDTO.TrabajadorResponse;
import ms_viajes.microservicio_viajes.viajeDTO.ViajeDTO;
import ms_viajes.microservicio_viajes.viajeEXCEPTION.ResourceNotFoundException;
import ms_viajes.microservicio_viajes.viaje_model.Viaje;
import ms_viajes.microservicio_viajes.viaje_repository.ViajeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ViajeService {
    private static final Logger logger = LoggerFactory.getLogger(ViajeService.class);

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String RUTAS_URL = "http://localhost:8083/api/rutas";
    private static final String BUSES_URL = "http://localhost:8084/api/buses";
    private static final String TRABAJADORES_URL = "http://localhost:8082/api/trabajadores";

    public List<Viaje> obtenerTodos() {
        logger.info("Obteniendo todos los viajes");
        return viajeRepository.findAll();
    }

    public Viaje obtenerPorId(Long id) {
        logger.info("Buscando viaje con ID: {}", id);

        return viajeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Viaje con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Viaje con ID " + id + " no encontrado");
                });
    }

    public List<Viaje> buscarPorEstado(String estado) {
        logger.info("Buscando viajes con estado: {}", estado);
        return viajeRepository.findByEstadoContainingIgnoreCase(estado);
    }

    public List<Viaje> buscarPorRuta(Long rutaId) {
        logger.info("Buscando viajes para ruta ID: {}", rutaId);
        return viajeRepository.findByRutaId(rutaId);
    }

    public List<Viaje> buscarPorBus(Long busId) {
        logger.info("Buscando viajes para bus ID: {}", busId);
        return viajeRepository.findByBusId(busId);
    }

    public List<Viaje> buscarPorTrabajador(Long trabajadorId) {
        logger.info("Buscando viajes para trabajador ID: {}", trabajadorId);
        return viajeRepository.findByTrabajadorId(trabajadorId);
    }

    public List<Viaje> buscarPorFecha(LocalDate fechaSalida) {
        logger.info("Buscando viajes para fecha: {}", fechaSalida);
        return viajeRepository.findByFechaSalida(fechaSalida);
    }

    public Viaje crear(ViajeDTO viajeDTO) {
        logger.info("Creando viaje con ruta {}, bus {} y trabajador {}",
                viajeDTO.getRutaId(),
                viajeDTO.getBusId(),
                viajeDTO.getTrabajadorId());

        RutaResponse ruta = obtenerRutaRemota(viajeDTO.getRutaId());
        BusResponse bus = obtenerBusRemoto(viajeDTO.getBusId());
        TrabajadorResponse trabajador = obtenerTrabajadorRemoto(viajeDTO.getTrabajadorId());

        if (ruta.getActiva() != null && !ruta.getActiva()) {
            throw new RuntimeException("La ruta seleccionada no está activa");
        }

        if (!"DISPONIBLE".equalsIgnoreCase(bus.getEstado())) {
            throw new RuntimeException("El bus seleccionado no está disponible");
        }

        if (trabajador.getActivo() != null && !trabajador.getActivo()) {
            throw new RuntimeException("El trabajador seleccionado no está activo");
        }

        Viaje viaje = new Viaje();
        viaje.setRutaId(viajeDTO.getRutaId());
        viaje.setBusId(viajeDTO.getBusId());
        viaje.setTrabajadorId(viajeDTO.getTrabajadorId());
        viaje.setFechaSalida(viajeDTO.getFechaSalida());
        viaje.setHoraSalida(viajeDTO.getHoraSalida());
        viaje.setPrecio(viajeDTO.getPrecio());
        viaje.setEstado("PROGRAMADO");

        Viaje viajeGuardado = viajeRepository.save(viaje);

        logger.info("Viaje creado correctamente con ID: {}", viajeGuardado.getId());
        return viajeGuardado;
    }

    public Viaje actualizar(Long id, ViajeDTO viajeDTO) {
        logger.info("Actualizando viaje con ID: {}", id);

        Viaje viajeExistente = obtenerPorId(id);

        RutaResponse ruta = obtenerRutaRemota(viajeDTO.getRutaId());
        BusResponse bus = obtenerBusRemoto(viajeDTO.getBusId());
        TrabajadorResponse trabajador = obtenerTrabajadorRemoto(viajeDTO.getTrabajadorId());

        if (ruta.getActiva() != null && !ruta.getActiva()) {
            throw new RuntimeException("La ruta seleccionada no está activa");
        }

        if (!"DISPONIBLE".equalsIgnoreCase(bus.getEstado())) {
            throw new RuntimeException("El bus seleccionado no está disponible");
        }

        if (trabajador.getActivo() != null && !trabajador.getActivo()) {
            throw new RuntimeException("El trabajador seleccionado no está activo");
        }

        viajeExistente.setRutaId(viajeDTO.getRutaId());
        viajeExistente.setBusId(viajeDTO.getBusId());
        viajeExistente.setTrabajadorId(viajeDTO.getTrabajadorId());
        viajeExistente.setFechaSalida(viajeDTO.getFechaSalida());
        viajeExistente.setHoraSalida(viajeDTO.getHoraSalida());
        viajeExistente.setPrecio(viajeDTO.getPrecio());

        Viaje viajeActualizado = viajeRepository.save(viajeExistente);

        logger.info("Viaje actualizado correctamente con ID: {}", viajeActualizado.getId());
        return viajeActualizado;
    }

    public void cambiarEstado(Long id, String estado) {
        logger.info("Cambiando estado del viaje ID {} a {}", id, estado);

        Viaje viaje = obtenerPorId(id);
        viaje.setEstado(estado);
        viajeRepository.save(viaje);

        logger.info("Estado del viaje actualizado correctamente");
    }

    public void eliminar(Long id) {
        logger.info("Eliminando viaje con ID: {}", id);

        Viaje viaje = obtenerPorId(id);
        viajeRepository.delete(viaje);

        logger.info("Viaje eliminado correctamente");
    }

    private RutaResponse obtenerRutaRemota(Long rutaId) {
        logger.info("Consultando rutas-service para ruta ID: {}", rutaId);

        return webClientBuilder.build()
                .get()
                .uri(RUTAS_URL + "/" + rutaId)
                .retrieve()
                .bodyToMono(RutaResponse.class)
                .block();
    }

    private BusResponse obtenerBusRemoto(Long busId) {
        logger.info("Consultando buses-service para bus ID: {}", busId);

        return webClientBuilder.build()
                .get()
                .uri(BUSES_URL + "/" + busId)
                .retrieve()
                .bodyToMono(BusResponse.class)
                .block();
    }

    private TrabajadorResponse obtenerTrabajadorRemoto(Long trabajadorId) {
        logger.info("Consultando trabajadores-service para trabajador ID: {}", trabajadorId);

        return webClientBuilder.build()
                .get()
                .uri(TRABAJADORES_URL + "/" + trabajadorId)
                .retrieve()
                .bodyToMono(TrabajadorResponse.class)
                .block();
    }
}
