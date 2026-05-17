package ms_pasajero.microservicios.pasajerosEXCEPTION;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
