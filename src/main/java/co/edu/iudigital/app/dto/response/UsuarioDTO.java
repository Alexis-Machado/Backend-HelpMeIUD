package co.edu.iudigital.app.dto.response;

// Importaciones de las anotaciones de Lombok para simplificar el código
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

// Importación de clases de Java para manejar tipos como LocalDate y List
import java.time.LocalDate;
import java.util.List;

// Declaración de la clase UsuarioDTO
@Data // Anotación Lombok que genera automáticamente métodos toString, equals, hashCode, getter y setter
@Builder // Anotación Lombok que genera un constructor de tipo "builder" para facilitar la creación de instancias
@FieldDefaults(level = AccessLevel.PRIVATE) // Anotación Lombok para establecer el nivel de acceso privado por defecto para los campos

public class UsuarioDTO {

    Long id; // Campo que almacena el identificador único del usuario

    String username; // Campo que almacena el Correo Electronico de usuario

    String nombre; // Campo que almacena el nombre del usuario

    String apellido; // Campo que almacena el apellido del usuario

    Boolean redSocial; // Campo que indica si el usuario está vinculado a una red social

    LocalDate fechaNacimiento; // Campo que almacena la fecha de nacimiento del usuario

    Boolean enabled; // Campo que indica si la cuenta del usuario está habilitada

    String image; // Campo que almacena la URL de la imagen del usuario

    List<String> roles; // Lista que almacena los roles asociados al usuario
}
