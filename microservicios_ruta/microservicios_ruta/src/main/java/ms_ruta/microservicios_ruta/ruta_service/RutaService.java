package ms_ruta.microservicios_ruta.ruta_service;

import jakarta.transaction.Transactional;
import ms_ruta.microservicios_ruta.RutaDTO.rutaDTO;
import ms_ruta.microservicios_ruta.ruta_repository.RutaRepository;
import ms_ruta.microservicios_ruta.rutasEXCEPTION.ResourceNotFoundException;
import ms_ruta.microservicios_ruta.rutas_model.Ruta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RutaService {
    private static final Logger logger = LoggerFactory.getLogger(RutaService.class);

    @Autowired
    private RutaRepository rutaRepository;

    public List<Ruta> obtenerTodas() {
        logger.info("Obteniendo todas las rutas");
        List<Ruta> rutas = rutaRepository.findAll();
        logger.info("Se encontraron {} rutas", rutas.size());
        return rutas;
    }

    public Ruta obtenerPorId(Long id) {
        logger.info("Buscando ruta con ID: {}", id);

        return rutaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Ruta con ID {} no encontrada", id);
                    return new ResourceNotFoundException("Ruta con ID " + id + " no encontrada");
                });
    }

    public List<Ruta> obtenerActivas() {
        logger.info("Obteniendo rutas activas");
        return rutaRepository.findByActiva(true);
    }

    public List<Ruta> buscarPorOrigen(String origen) {
        logger.info("Buscando rutas con origen: {}", origen);
        return rutaRepository.findByCiudadOrigenContainingIgnoreCase(origen);
    }

    public List<Ruta> buscarPorDestino(String destino) {
        logger.info("Buscando rutas con destino: {}", destino);
        return rutaRepository.findByCiudadDestinoContainingIgnoreCase(destino);
    }

    public List<Ruta> buscarPorOrigenYDestino(String origen, String destino) {
        logger.info("Buscando rutas desde {} hacia {}", origen, destino);
        return rutaRepository.findByCiudadOrigenContainingIgnoreCaseAndCiudadDestinoContainingIgnoreCase(origen, destino);
    }

    public Ruta crear(rutaDTO RutaDTO) {
        logger.info("Creando nueva ruta desde {} hacia {}", RutaDTO.getCiudadOrigen(), RutaDTO.getCiudadDestino());

        if (RutaDTO.getCiudadOrigen().equalsIgnoreCase(RutaDTO.getCiudadDestino())) {
            logger.error("La ciudad de origen y destino no pueden ser iguales");
            throw new RuntimeException("La ciudad de origen y destino no pueden ser iguales");
        }

        Ruta ruta = new Ruta();
        ruta.setCiudadOrigen(RutaDTO.getCiudadOrigen());
        ruta.setCiudadDestino(RutaDTO.getCiudadDestino());
        ruta.setDistanciaKm(RutaDTO.getDistanciaKm());
        ruta.setDuracionEstimada(RutaDTO.getDuracionEstimada());
        ruta.setPrecioBase(RutaDTO.getPrecioBase());
        ruta.setActiva(true);

        Ruta rutaGuardada = rutaRepository.save(ruta);

        logger.info("Ruta creada exitosamente con ID: {}", rutaGuardada.getId());
        return rutaGuardada;

    }

    public Ruta actualizar(Long id, rutaDTO RutaDTO) {
        logger.info("Actualizando ruta con ID: {}", id);

        Ruta rutaExistente = obtenerPorId(id);

        if (RutaDTO.getCiudadOrigen().equalsIgnoreCase(RutaDTO.getCiudadDestino())) {
            logger.error("La ciudad de origen y destino no pueden ser iguales");
            throw new RuntimeException("La ciudad de origen y destino no pueden ser iguales");
        }

        rutaExistente.setCiudadOrigen(RutaDTO.getCiudadOrigen());
        rutaExistente.setCiudadDestino(RutaDTO.getCiudadDestino());
        rutaExistente.setDistanciaKm(RutaDTO.getDistanciaKm());
        rutaExistente.setDuracionEstimada(RutaDTO.getDuracionEstimada());
        rutaExistente.setPrecioBase(RutaDTO.getPrecioBase());

        Ruta rutaActualizada = rutaRepository.save(rutaExistente);

        logger.info("Ruta actualizada exitosamente con ID: {}", rutaActualizada.getId());
        return rutaActualizada;
    }

    public void desactivar(Long id) {
        logger.info("Desactivando ruta con ID: {}", id);

        Ruta ruta = obtenerPorId(id);
        ruta.setActiva(false);
        rutaRepository.save(ruta);

        logger.info("Ruta desactivada exitosamente");
    }

    public void eliminar(Long id) {
        logger.info("Eliminando ruta con ID: {}", id);

        Ruta ruta = obtenerPorId(id);
        rutaRepository.delete(ruta);

        logger.info("Ruta eliminada exitosamente");
    }
}
