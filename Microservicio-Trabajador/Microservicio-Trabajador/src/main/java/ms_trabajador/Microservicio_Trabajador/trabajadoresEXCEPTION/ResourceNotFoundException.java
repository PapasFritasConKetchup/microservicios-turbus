package ms_trabajador.Microservicio_Trabajador.trabajadoresEXCEPTION;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
