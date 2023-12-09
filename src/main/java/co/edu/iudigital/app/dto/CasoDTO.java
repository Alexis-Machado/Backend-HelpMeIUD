package co.edu.iudigital.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CasoDTO {

    // Identificador único del caso
    Long id;

    // Fecha y hora del caso
    @JsonProperty("fecha_hora")
    LocalDateTime fechaHora;

    // Coordenada de latitud del lugar del caso
    Float latitud;

    // Coordenada de longitud del lugar del caso
    Float longitud;

    // Altitud del lugar del caso
    Float altitud;

    // Descripción detallada del caso
    String descripcion;

    // Indica si el caso es visible
    Boolean esVisible;

    // URL del mapa asociado al caso
    @JsonProperty("url_map")
    String urlMap;

    // URL relacionada con el sistema de información RMI
    @JsonProperty("rmi_url")
    String rmiUrl;

    // Identificador del usuario asociado al caso
    @JsonProperty("usuario_id")
    Long usuarioId;

    // Identificador del delito asociado al caso
    @JsonProperty("delito_id")
    Long delitoId;
}
