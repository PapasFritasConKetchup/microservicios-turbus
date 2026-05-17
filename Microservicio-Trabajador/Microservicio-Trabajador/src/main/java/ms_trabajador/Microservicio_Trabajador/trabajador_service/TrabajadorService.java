package ms_trabajador.Microservicio_Trabajador.trabajador_service;


import jakarta.transaction.Transactional;
import ms_trabajador.Microservicio_Trabajador.Modelo_Trabajador.Trabajador;
import ms_trabajador.Microservicio_Trabajador.TrabajadorRepository.trabajador_repository;
import ms_trabajador.Microservicio_Trabajador.trabajadorDTO.TrabajadorDTO;
import ms_trabajador.Microservicio_Trabajador.trabajadoresEXCEPTION.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TrabajadorService {
    private static final Logger logger = LoggerFactory.getLogger(TrabajadorService.class);
    private trabajador_repository trabajador_repository;

    @Autowired
    public void setTrabajador_repository(trabajador_repository trabajadorRepository) {
        this.trabajador_repository = trabajadorRepository;
    }
    public List<Trabajador> obtenerTodos() {
        logger.info("Obteniendo lista de todos los trabajadores");
        List<Trabajador> trabajadores = trabajador_repository.findAll();
        logger.info("Se encontraron {} trabajadores", trabajadores.size());
        return trabajadores;
    }

    public Trabajador obtenerPorId(Long id) {
        logger.info("Buscando trabajador con ID: {}", id);

        return trabajador_repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Trabajador con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Trabajador con ID " + id + " no encontrado");
                });
    }

    public Trabajador obtenerPorRut(String rut) {
        logger.info("Buscando trabajador con RUT: {}", rut);

        return trabajador_repository.findByRut(rut)
                .orElseThrow(() -> {
                    logger.error("Trabajador con RUT {} no encontrado", rut);
                    return new ResourceNotFoundException("Trabajador con RUT " + rut + " no encontrado");
                });
    }

    public List<Trabajador> obtenerActivos() {
        logger.info("Obteniendo trabajadores activos");
        return trabajador_repository.findByActivo(true);
    }

    public List<Trabajador> buscarPorCargo(String cargo) {
        logger.info("Buscando trabajadores con cargo: {}", cargo);
        return trabajador_repository.findByCargoContainingIgnoreCase(cargo);
    }

    public Trabajador crear(TrabajadorDTO trabajadorDTO) {
        logger.info("Creando nuevo trabajador con RUT: {}", trabajadorDTO.getRut());

        if (trabajador_repository.existsByRut(trabajadorDTO.getRut())) {
            logger.error("Ya existe un trabajador con RUT: {}", trabajadorDTO.getRut());
            throw new RuntimeException("Ya existe un trabajador con el RUT: " + trabajadorDTO.getRut());
        }

        if (trabajador_repository.existsByEmail(trabajadorDTO.getEmail())) {
            logger.error("Ya existe un trabajador con email: {}", trabajadorDTO.getEmail());
            throw new RuntimeException("Ya existe un trabajador con el email: " + trabajadorDTO.getEmail());
        }

        Trabajador trabajador = new Trabajador();
        trabajador.setRut(trabajadorDTO.getRut());
        trabajador.setNombre(trabajadorDTO.getNombre());
        trabajador.setApellido(trabajadorDTO.getApellido());
        trabajador.setEmail(trabajadorDTO.getEmail());
        trabajador.setCargo(trabajadorDTO.getCargo());
        trabajador.setTelefono(trabajadorDTO.getTelefono());
        trabajador.setActivo(true);

        Trabajador trabajadorGuardado = trabajador_repository.save(trabajador);

        logger.info("Trabajador creado exitosamente con ID: {}", trabajadorGuardado.getId());
        return trabajadorGuardado;
    }

    public Trabajador actualizar(Long id, TrabajadorDTO trabajadorDTO) {
        logger.info("Actualizando trabajador con ID: {}", id);

        Trabajador trabajadorExistente = obtenerPorId(id);

        if (!trabajadorExistente.getRut().equals(trabajadorDTO.getRut()) &&
                trabajador_repository.existsByRut(trabajadorDTO.getRut())) {
            logger.error("Ya existe otro trabajador con RUT: {}", trabajadorDTO.getRut());
            throw new RuntimeException("Ya existe otro trabajador con el RUT: " + trabajadorDTO.getRut());
        }

        if (!trabajadorExistente.getEmail().equals(trabajadorDTO.getEmail()) &&
                trabajador_repository.existsByEmail(trabajadorDTO.getEmail())) {
            logger.error("Ya existe otro trabajador con email: {}", trabajadorDTO.getEmail());
            throw new RuntimeException("Ya existe otro trabajador con el email: " + trabajadorDTO.getEmail());
        }

        trabajadorExistente.setRut(trabajadorDTO.getRut());
        trabajadorExistente.setNombre(trabajadorDTO.getNombre());
        trabajadorExistente.setApellido(trabajadorDTO.getApellido());
        trabajadorExistente.setEmail(trabajadorDTO.getEmail());
        trabajadorExistente.setCargo(trabajadorDTO.getCargo());
        trabajadorExistente.setTelefono(trabajadorDTO.getTelefono());

        Trabajador trabajadorActualizado = trabajador_repository.save(trabajadorExistente);

        logger.info("Trabajador actualizado exitosamente con ID: {}", trabajadorActualizado.getId());
        return trabajadorActualizado;
    }

    public void desactivar(Long id) {
        logger.info("Desactivando trabajador con ID: {}", id);

        Trabajador trabajador = obtenerPorId(id);
        trabajador.setActivo(false);
        trabajador_repository.save(trabajador);

        logger.info("Trabajador desactivado exitosamente");
    }

    public void eliminar(Long id) {
        logger.info("Eliminando trabajador con ID: {}", id);

        Trabajador trabajador = obtenerPorId(id);
        trabajador_repository.delete(trabajador);

        logger.info("Trabajador eliminado exitosamente");
    }


}
