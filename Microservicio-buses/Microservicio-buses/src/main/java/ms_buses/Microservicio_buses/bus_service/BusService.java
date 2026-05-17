package ms_buses.Microservicio_buses.bus_service;

import jakarta.transaction.Transactional;
import ms_buses.Microservicio_buses.busDTO.BusDTO;
import ms_buses.Microservicio_buses.asiento_repository.AsientoRepository;
import ms_buses.Microservicio_buses.bus_repository.BusRepository;
import ms_buses.Microservicio_buses.busesEXCEPTION.ResourceNotFoundException;
import ms_buses.Microservicio_buses.buses_model.Asiento;
import ms_buses.Microservicio_buses.buses_model.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BusService {
    private static final Logger logger = LoggerFactory.getLogger(BusService.class);

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private AsientoRepository asientoRepository;

    public List<Bus> obtenerTodos() {
        logger.info("Obteniendo lista de todos los buses");
        List<Bus> buses = busRepository.findAll();
        logger.info("Se encontraron {} buses", buses.size());
        return buses;
    }

    public Bus obtenerPorId(Long id) {
        logger.info("Buscando bus con ID: {}", id);

        return busRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Bus con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Bus con ID " + id + " no encontrado");
                });
    }

    public Bus obtenerPorPatente(String patente) {
        logger.info("Buscando bus con patente: {}", patente);

        return busRepository.findByPatente(patente)
                .orElseThrow(() -> {
                    logger.error("Bus con patente {} no encontrado", patente);
                    return new ResourceNotFoundException("Bus con patente " + patente + " no encontrado");
                });
    }

    public List<Bus> buscarPorEstado(String estado) {
        logger.info("Buscando buses con estado: {}", estado);
        return busRepository.findByEstadoContainingIgnoreCase(estado);
    }

    public List<Bus> buscarPorTipoBus(String tipoBus) {
        logger.info("Buscando buses por tipo: {}", tipoBus);
        return busRepository.findByTipoBusContainingIgnoreCase(tipoBus);
    }

    public List<Asiento> obtenerAsientosPorBus(Long busId) {
        logger.info("Obteniendo asientos del bus con ID: {}", busId);

        obtenerPorId(busId);

        return asientoRepository.findByBusId(busId);
    }

    public List<Asiento> obtenerAsientosDisponibles(Long busId) {
        logger.info("Obteniendo asientos disponibles del bus con ID: {}", busId);

        obtenerPorId(busId);

        return asientoRepository.findByBusIdAndDisponible(busId, true);
    }

    public Bus crear(BusDTO busDTO) {
        logger.info("Creando nuevo bus con patente: {}", busDTO.getPatente());

        if (busRepository.existsByPatente(busDTO.getPatente())) {
            logger.error("Ya existe un bus con patente: {}", busDTO.getPatente());
            throw new RuntimeException("Ya existe un bus con la patente: " + busDTO.getPatente());
        }

        Bus bus = new Bus();
        bus.setPatente(busDTO.getPatente());
        bus.setCapacidad(busDTO.getCapacidad());
        bus.setTipoBus(busDTO.getTipoBus());
        bus.setEstado(busDTO.getEstado());

        List<Asiento> asientos = new ArrayList<>();

        for (int i = 1; i <= busDTO.getCapacidad(); i++) {
            Asiento asiento = new Asiento();
            asiento.setNumeroAsiento(i);
            asiento.setDisponible(true);

            if (i % 2 == 0) {
                asiento.setTipoAsiento("PASILLO");
            } else {
                asiento.setTipoAsiento("VENTANA");
            }

            asiento.setBus(bus);
            asientos.add(asiento);
        }

        bus.setAsientos(asientos);

        Bus busGuardado = busRepository.save(bus);

        logger.info("Bus creado exitosamente con ID: {} y {} asientos", busGuardado.getId(), busGuardado.getCapacidad());
        return busGuardado;
    }

    public Bus actualizar(Long id, BusDTO busDTO) {
        logger.info("Actualizando bus con ID: {}", id);

        Bus busExistente = obtenerPorId(id);

        if (!busExistente.getPatente().equalsIgnoreCase(busDTO.getPatente())
                && busRepository.existsByPatente(busDTO.getPatente())) {
            logger.error("Ya existe otro bus con patente: {}", busDTO.getPatente());
            throw new RuntimeException("Ya existe otro bus con la patente: " + busDTO.getPatente());
        }

        busExistente.setPatente(busDTO.getPatente());
        busExistente.setTipoBus(busDTO.getTipoBus());
        busExistente.setEstado(busDTO.getEstado());

        if (!busExistente.getCapacidad().equals(busDTO.getCapacidad())) {
            logger.info("Actualizando capacidad del bus. Se regenerarán los asientos");

            busExistente.getAsientos().clear();

            for (int i = 1; i <= busDTO.getCapacidad(); i++) {
                Asiento asiento = new Asiento();
                asiento.setNumeroAsiento(i);
                asiento.setDisponible(true);

                if (i % 2 == 0) {
                    asiento.setTipoAsiento("PASILLO");
                } else {
                    asiento.setTipoAsiento("VENTANA");
                }

                asiento.setBus(busExistente);
                busExistente.getAsientos().add(asiento);
            }

            busExistente.setCapacidad(busDTO.getCapacidad());
        }

        Bus busActualizado = busRepository.save(busExistente);

        logger.info("Bus actualizado exitosamente con ID: {}", busActualizado.getId());
        return busActualizado;
    }

    public Asiento cambiarDisponibilidadAsiento(Long asientoId, Boolean disponible) {
        logger.info("Cambiando disponibilidad del asiento ID {} a {}", asientoId, disponible);

        Asiento asiento = asientoRepository.findById(asientoId)
                .orElseThrow(() -> {
                    logger.error("Asiento con ID {} no encontrado", asientoId);
                    return new ResourceNotFoundException("Asiento con ID " + asientoId + " no encontrado");
                });

        asiento.setDisponible(disponible);

        Asiento asientoActualizado = asientoRepository.save(asiento);

        logger.info("Disponibilidad de asiento actualizada correctamente");
        return asientoActualizado;
    }

    public void cambiarEstadoBus(Long id, String estado) {
        logger.info("Cambiando estado del bus ID {} a {}", id, estado);

        Bus bus = obtenerPorId(id);
        bus.setEstado(estado);
        busRepository.save(bus);

        logger.info("Estado del bus actualizado correctamente");
    }

    public void eliminar(Long id) {
        logger.info("Eliminando bus con ID: {}", id);

        Bus bus = obtenerPorId(id);
        busRepository.delete(bus);

        logger.info("Bus eliminado exitosamente");
    }
}
