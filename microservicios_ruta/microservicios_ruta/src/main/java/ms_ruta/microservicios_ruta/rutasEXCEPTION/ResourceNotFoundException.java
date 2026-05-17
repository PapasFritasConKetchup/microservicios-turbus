package ms_ruta.microservicios_ruta.rutasEXCEPTION;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
