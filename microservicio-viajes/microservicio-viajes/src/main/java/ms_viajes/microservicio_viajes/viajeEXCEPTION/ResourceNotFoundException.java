package ms_viajes.microservicio_viajes.viajeEXCEPTION;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje) ;
    }
}
