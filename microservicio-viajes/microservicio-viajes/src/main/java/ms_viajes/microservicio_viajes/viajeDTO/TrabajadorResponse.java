package ms_viajes.microservicio_viajes.viajeDTO;

import lombok.Data;

@Data
public class TrabajadorResponse {
    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String cargo;
    private String telefono;
    private Boolean activo;
}
