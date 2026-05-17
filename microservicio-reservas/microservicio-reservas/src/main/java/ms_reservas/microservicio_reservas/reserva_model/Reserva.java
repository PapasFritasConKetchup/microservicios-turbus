package ms_reservas.microservicio_reservas.reserva_model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pasajero_id", nullable = false)
    private Long pasajeroId;

    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    @Column(name = "asiento_id", nullable = false)
    private Long asientoId;

    @Column(name = "descuento_id")
    private Long descuentoId;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE";
}
