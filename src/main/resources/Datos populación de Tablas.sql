INSERT INTO roles(nombre, descripcion) VALUES ("ROLE_USER", "Usuarios Normales que se Registran");
INSERT INTO roles(nombre, descripcion) VALUES ("ROLE_ADMIN", "Usuarios Administradores");


INSERT INTO delitos(nombre,descripcion,usuarios_id)
VALUES ('hurto', 'cuando se quitan pertenencias', 1);
INSERT INTO delitos(nombre,descripcion,usuarios_id)
VALUES ('acoso sexual', 'groserías a una persona', 1);