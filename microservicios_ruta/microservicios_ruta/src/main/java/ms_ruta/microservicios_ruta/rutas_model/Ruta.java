package ms_ruta.microservicios_ruta.rutas_model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "rutas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ciudad_origen", nullable = false, length = 100)
    private String ciudadOrigen;

    @Column(name = "ciudad_destino", nullable = false, length = 100)
    private String ciudadDestino;

    @Column(name = "distancia_km", nullable = false, precision = 9, scale = 2)
    private BigDecimal distanciaKm;

    @Column(name = "duracion_estimada", length = 50)
    private String duracionEstimada;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(nullable = false)
    private Boolean activa = true;
}
