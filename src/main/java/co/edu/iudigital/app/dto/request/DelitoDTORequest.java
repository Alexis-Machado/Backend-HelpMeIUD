package co.edu.iudigital.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class DelitoDTORequest {

    //Aquí representamos la información necesaria para crear un nuevo delito.

    // El nombre del delito.
    @NotNull(message = "¡Ups! Parece que olvidaste ingresar el nombre del delito.")
    @NotEmpty(message = "¡Por favor, asegúrate de proporcionar un nombre válido para el delito!")
    String nombre;

    // Una breve descripción opcional del delito.
    String descripcion;

    //Necesitamos saber quién es el usuario asociado a este delito.
    @NotNull(message = "¡Es importante indicar quién está reportando este delito!")
    @JsonProperty("usuario_id")
    Long usuarioId;
}
