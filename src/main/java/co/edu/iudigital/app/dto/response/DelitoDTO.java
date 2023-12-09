package co.edu.iudigital.app.dto.response;

// Importaciones de las anotaciones de Lombok para simplificar el código
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

// Declaración de la clase DelitoDTO
@Data // Anotación Lombok que genera automáticamente métodos toString, equals, hashCode, getter y setter
@Builder // Anotación Lombok que genera un constructor de tipo "builder" para facilitar la creación de instancias
@FieldDefaults(level = AccessLevel.PRIVATE) // Anotación Lombok para establecer el nivel de acceso privado por defecto para los campos

public class DelitoDTO {

    Long id; // Campo que almacena el identificador único del delito

    String nombre; // Campo que almacena el nombre del delito

    String descripcion; // Campo que almacena la descripción del delito
}
