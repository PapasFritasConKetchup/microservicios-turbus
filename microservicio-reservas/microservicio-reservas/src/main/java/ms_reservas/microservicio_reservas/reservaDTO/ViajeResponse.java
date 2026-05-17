package ms_reservas.microservicio_reservas.reservaDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ViajeResponse {
    private Long id;
    private Long rutaId;
    private Long busId;
    private Long trabajadorId;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private BigDecimal precio;
    private String estado;
}
