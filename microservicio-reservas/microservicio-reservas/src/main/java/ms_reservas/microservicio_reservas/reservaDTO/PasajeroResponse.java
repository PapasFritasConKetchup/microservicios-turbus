package ms_reservas.microservicio_reservas.reservaDTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PasajeroResponse {
    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private Boolean activo;
}
