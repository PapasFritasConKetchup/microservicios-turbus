package ms_reservas.microservicio_reservas.reservaDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaDTO {
    @NotNull(message = "El ID del pasajero es obligatorio")
    private Long pasajeroId;

    @NotNull(message = "El ID del viaje es obligatorio")
    private Long viajeId;

    @NotNull(message = "El ID del asiento es obligatorio")
    private Long asientoId;

    private Long descuentoId;
}
