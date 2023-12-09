// Package y declaraciones de importación
package co.edu.iudigital.app.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

// Anotación de JPA para indicar que esta clase es una entidad
@Entity

// Anotación para especificar el nombre de la tabla en la base de datos
@Table(name = "usuarios")

// Anotación de Lombok para configurar automáticamente los getters y setters
@Getter
@Setter

// Anotación de Lombok para establecer el nivel de acceso privado por defecto para los campos
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Usuario implements Serializable {

    // Anotación para indicar que este campo es la clave primaria
    @Id

    // Anotación para especificar la generación automática de valores de clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Anotaciones para configurar propiedades específicas de la columna
    @Column(name = "username", nullable = false, unique = true, length = 150)
    String username;

    @Column(name = "nombre", nullable = false, length = 150)
    String nombre;

    @Column(name = "apellido", length = 150)
    String apellido;

    @Column(name = "password", length = 250)
    String password;

    @Column(name = "fecha_nacimiento")
    LocalDate fechaNacimiento;

    @Column(name = "enabled")
    Boolean enabled;

    @Column(name = "image", length = 250)
    String image;

    // Columna que indica si el usuario está registrado a través de una red social
    @Column(name = "red_social")
    Boolean redSocial;

    // Relación ManyToMany con la tabla de roles, configurada para carga diferida
    @ManyToMany(fetch = FetchType.LAZY)

    // Configuración de la tabla de unión
    @JoinTable(
            name = "roles_usuarios",
            joinColumns = {@JoinColumn(name = "usuarios_id")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id")}
    )
    List<Role> roles;
}
