// Package y declaraciones de importación
package co.edu.iudigital.app.model;

import co.edu.iudigital.app.model.Usuario;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

// Anotación de JPA para indicar que esta clase es una entidad
@Entity

// Anotación para especificar el nombre de la tabla en la base de datos
@Table(name = "roles")

// Anotación de Lombok para configurar automáticamente los getters y setters
@Getter
@Setter

// Anotación de Lombok para establecer el nivel de acceso privado por defecto para los campos
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role implements Serializable {

    // Anotación para indicar que este campo es la clave primaria
    @Id

    // Anotación para especificar la generación automática de valores de clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Anotaciones para configurar propiedades específicas de la columna
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    String nombre;

    @Column(name = "descripcion", length = 150)
    String descripcion;

    // Relación ManyToMany inversa con la tabla de usuarios, configurada mediante mappedBy
    @ManyToMany(mappedBy = "roles")

    // Lista de usuarios asociados a este rol
            List<Usuario> usuarios;
}
