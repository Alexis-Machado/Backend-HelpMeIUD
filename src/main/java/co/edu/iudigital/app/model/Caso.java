// Package y declaraciones de importación
package co.edu.iudigital.app.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

// Anotación de JPA para indicar que esta clase es una entidad
@Entity

// Anotación para especificar el nombre de la tabla en la base de datos
@Table(name = "casos")

// Anotación de Lombok para configurar automáticamente los getters y setters
@Getter
@Setter

// Anotación de Lombok para establecer el nivel de acceso privado por defecto para los campos
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Caso implements Serializable {

    // Anotación para indicar que este campo es la clave primaria
    @Id

    // Anotación para especificar la generación automática de valores de clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Anotación para configurar propiedades específicas de la columna
    @Column(name = "fecha_hora")
    LocalDateTime fechaHora;

    @Column(name = "latitud")
    Float latitud;

    @Column(name = "longitud")
    Float longitud;

    @Column(name = "altitud")
    Float altitud;

    @Column(name = "visible")
    Boolean esVisible;

    @Column(name = "descripcion", length = 250)
    String descripcion;

    @Column(name = "url_map", length = 250)
    String urlMap;

    @Column(name = "rmi_Url", length = 250)
    String rmiUrl;

    // Relación ManyToOne con la tabla de usuarios, configurada para carga predeterminada
    @ManyToOne
    @JoinColumn(name = "usuarios_id")
    Usuario usuario;

    // Relación ManyToOne con la tabla de delitos, configurada para carga predeterminada
    @ManyToOne
    @JoinColumn(name = "delitos_id")
    Delito delito;

    // Método ejecutado antes de la persistencia para establecer la fecha y hora si es nula
    @PrePersist
    public void prePersist() {
        if (Objects.isNull(fechaHora)) {
            fechaHora = LocalDateTime.now();
        }
    }
}
