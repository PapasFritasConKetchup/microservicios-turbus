package ms_ruta.microservicios_ruta.RutaDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class rutaDTO {
    @NotBlank(message = "La ciudad de origen es obligatoria")
    private String ciudadOrigen;

    @NotBlank(message = "La ciudad de destino es obligatoria")
    private String ciudadDestino;

    @NotNull(message = "La distancia en kilómetros es obligatoria")
    @DecimalMin(value = "1.0", message = "La distancia debe ser mayor a 0")
    private BigDecimal distanciaKm;

    @NotBlank(message = "La duración estimada es obligatoria")
    private String duracionEstimada;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "1.0", message = "El precio base debe ser mayor a 0")
    private BigDecimal precioBase;
}
