package ms_reservas.microservicio_reservas.reservaDTO;

import lombok.Data;

@Data
public class AsientoResponse {
    private Long id;
    private Integer numeroAsiento;
    private String tipoAsiento;
    private Boolean disponible;
}
