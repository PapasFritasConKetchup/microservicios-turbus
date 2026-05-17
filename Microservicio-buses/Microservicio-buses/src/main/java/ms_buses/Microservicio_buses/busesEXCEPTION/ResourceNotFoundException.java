package ms_buses.Microservicio_buses.busesEXCEPTION;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
