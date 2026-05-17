package ms_buses.Microservicio_buses.busDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusDTO {
    @NotBlank(message = "La patente es obligatoria")
    private String patente;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    @NotBlank(message = "El tipo de bus es obligatorio")
    private String tipoBus;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
