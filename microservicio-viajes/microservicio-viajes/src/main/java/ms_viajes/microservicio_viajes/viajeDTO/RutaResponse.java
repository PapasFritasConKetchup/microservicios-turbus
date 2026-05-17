package ms_viajes.microservicio_viajes.viajeDTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RutaResponse {
    private Long id;
    private String ciudadOrigen;
    private String ciudadDestino;
    private BigDecimal distanciaKm;
    private String duracionEstimada;
    private BigDecimal precioBase;
    private Boolean activa;
}
