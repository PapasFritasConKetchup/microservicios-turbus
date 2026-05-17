package ms_pasajero.microservicios.pasajero_service;

import jakarta.transaction.Transactional;
import ms_pasajero.microservicios.pasajeroDTO.PasajeroDTO;
import ms_pasajero.microservicios.pasajero_repository.PasajeroRepository;
import ms_pasajero.microservicios.pasajerosEXCEPTION.ResourceNotFoundException;
import ms_pasajero.microservicios.pasajeros_model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PasajeroService {
    private static final Logger logger = LoggerFactory.getLogger(PasajeroService.class);

    @Autowired
    private PasajeroRepository pasajeroRepository ;

    public List<Model> obtenerTodos() {
        logger.info("Obteniendo lista de todos los pasajeros");
        List<Model> model = pasajeroRepository.findAll();
        logger.info("Se encontraron {} pasajeros", model.size());
        return model;
    }

    public Model obtenerPorId(Long id) {
        logger.info("Buscando pasajero con ID: {}", id);
        Model pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pasajero con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Pasajero con ID " + id + " no encontrado");
                });
        logger.info("Pasajero encontrado: {} {}", pasajero.getNombre(), pasajero.getApellido());
        return pasajero;
    }

    public Model obtenerPorRut(String rut) {
        logger.info("Buscando pasajero con RUT: {}", rut);
        Model pasajero = pasajeroRepository.findByRut(rut)
                .orElseThrow(() -> {
                    logger.error("Pasajero con RUT {} no encontrado", rut);
                    return new ResourceNotFoundException("Pasajero con RUT " + rut + " no encontrado");
                });
        logger.info("Pasajero encontrado: {} {}", pasajero.getNombre(), pasajero.getApellido());
        return pasajero;
    }

    public List<Model> buscarPorNombre(String nombre) {
        logger.info("Buscando pasajeros con nombre que contenga: {}", nombre);
        List<Model> m = pasajeroRepository.findByNombreContainingIgnoreCase(nombre);
        logger.info("Se encontraron {} pasajeros", m.size());
        return m;
    }

    public List<Model> obtenerActivos() {
        logger.info("Obteniendo pasajeros activos");
        List<Model> pasajeros = pasajeroRepository.findByActivo(true);
        logger.info("Se encontraron {} pasajeros activos", pasajeros.size());
        return pasajeros;
    }

    public Model crear(PasajeroDTO pasajeroDTO) {
        logger.info("Creando nuevo pasajero con RUT: {}", pasajeroDTO.getRut());

        // Validar que el RUT no exista
        if (pasajeroRepository.existsByRut(pasajeroDTO.getRut())) {
            logger.error("Ya existe un pasajero con RUT: {}", pasajeroDTO.getRut());
            throw new RuntimeException("Ya existe un pasajero con el RUT: " + pasajeroDTO.getRut());
        }

        // Validar que el email no exista
        if (pasajeroRepository.existsByEmail(pasajeroDTO.getEmail())) {
            logger.error("Ya existe un pasajero con email: {}", pasajeroDTO.getEmail());
            throw new RuntimeException("Ya existe un pasajero con el email: " + pasajeroDTO.getEmail());
        }

        // Crear entidad desde DTO
        Model pasajero = new Model();
        pasajero.setRut(pasajeroDTO.getRut());
        pasajero.setNombre(pasajeroDTO.getNombre());
        pasajero.setApellido(pasajeroDTO.getApellido());
        pasajero.setEmail(pasajeroDTO.getEmail());
        pasajero.setTelefono(pasajeroDTO.getTelefono());
        pasajero.setFechaNacimiento(pasajeroDTO.getFechaNacimiento());

        Model pasajeroGuardado = pasajeroRepository.save(pasajero);
        logger.info("Pasajero creado exitosamente con ID: {}", pasajeroGuardado.getId());
        return pasajeroGuardado;
    }

    public Model actualizar(Long id, PasajeroDTO pasajeroDTO) {
        logger.info("Actualizando pasajero con ID: {}", id);

        Model pasajeroExistente = obtenerPorId(id);

        // Validar que el RUT no exista en otro pasajero
        if (!pasajeroExistente.getRut().equals(pasajeroDTO.getRut()) &&
                pasajeroRepository.existsByRut(pasajeroDTO.getRut())) {
            logger.error("Ya existe otro pasajero con RUT: {}", pasajeroDTO.getRut());
            throw new RuntimeException("Ya existe otro pasajero con el RUT: " + pasajeroDTO.getRut());
        }

        // Validar que el email no exista en otro pasajero
        if (!pasajeroExistente.getEmail().equals(pasajeroDTO.getEmail()) &&
                pasajeroRepository.existsByEmail(pasajeroDTO.getEmail())) {
            logger.error("Ya existe otro pasajero con email: {}", pasajeroDTO.getEmail());
            throw new RuntimeException("Ya existe otro pasajero con el email: " + pasajeroDTO.getEmail());
        }

        // Actualizar datos
        pasajeroExistente.setRut(pasajeroDTO.getRut());
        pasajeroExistente.setNombre(pasajeroDTO.getNombre());
        pasajeroExistente.setApellido(pasajeroDTO.getApellido());
        pasajeroExistente.setEmail(pasajeroDTO.getEmail());
        pasajeroExistente.setTelefono(pasajeroDTO.getTelefono());
        pasajeroExistente.setFechaNacimiento(pasajeroDTO.getFechaNacimiento());

        Model pasajeroActualizado = pasajeroRepository.save(pasajeroExistente);
        logger.info("Pasajero actualizado exitosamente");
        return pasajeroActualizado;
    }

    public void desactivar(Long id) {
        logger.info("Desactivando pasajero con ID: {}", id);
        Model pasajero = obtenerPorId(id);
        pasajero.setActivo(false);
        pasajeroRepository.save(pasajero);
        logger.info("Pasajero desactivado exitosamente");
    }

    public void eliminar(Long id) {
        logger.info("Eliminando pasajero con ID: {}", id);
        Model pasajero = obtenerPorId(id);
        pasajeroRepository.delete(pasajero);
        logger.info("Pasajero eliminado exitosamente");
    }



}
