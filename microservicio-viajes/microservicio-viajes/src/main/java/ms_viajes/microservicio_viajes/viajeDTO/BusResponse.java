package ms_viajes.microservicio_viajes.viajeDTO;

import lombok.Data;

@Data
public class BusResponse {
    private Long id;
    private String patente;
    private Integer capacidad;
    private String tipoBus;
    private String estado;
}
