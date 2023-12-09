package co.edu.iudigital.app.exceptions;

// Importaciones de Lombok para simplificar el código
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Importación de clases de Java para manejar tipos como LocalDateTime y Serializable
import java.io.Serializable;
import java.time.LocalDateTime;

// Clase que representa un objeto de transferencia de errores (ErrorDto)
@Getter // Anotación Lombok para generar automáticamente métodos getter
@Setter // Anotación Lombok para generar automáticamente métodos setter
@Builder // Anotación Lombok para generar un constructor de tipo "builder"
public class ErrorDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String error; // Nombre del error HTTP

    private String message; // Mensaje personalizado del error HTTP

    private int status; // Código del error HTTP

    private LocalDateTime date; // Fecha y hora en que ocurrió el error

}
