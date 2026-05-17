package ms_reservas.microservicio_reservas.reservaEXCEPTION;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
