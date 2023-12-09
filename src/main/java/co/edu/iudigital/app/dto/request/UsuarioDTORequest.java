package co.edu.iudigital.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UsuarioDTORequest {

    //Aquí se encuentran los detalles para crear un nuevo usuario.

    // El nombre de usuario es obligatorio.
    @NotNull(message = "¡Es necesario proporcionar un nombre de usuario!")
    @Email(message = "¡Oops! Parece que el formato del correo electrónico no es correcto.")
    String username;

    // El nombre es obligatorio.
    @NotNull(message = "¡Ups! Olvidaste ingresar el nombre.")
    @NotBlank(message = "¡El nombre es necesario! Por favor, indícalo.")
    @Size(min = 2, max = 150)
    String nombre;

    // Proporcionar el Apellido.
    String apellido;

    // La contraseña debe tener al menos 5 caracteres. ¡Asegúrate de que sea segura!
    @Size(min = 5, message = "¡Necesitamos una contraseña segura! Por favor, asegúrate de que tenga al menos 5 caracteres.")
    String password;

    //Red Social.
    @JsonProperty("red_social")
    Boolean redSocial;

    // La fecha de nacimiento es opcional.
    @JsonProperty("fecha_nacimiento")
    LocalDate fechaNacimiento;

    //La cuenta esta Habilitada o Desabilitada.
    Boolean enabled;

    //Agrega una Imagen a tu Perfil.
    String image;
}
