CREATE DATABASE IF NOT EXISTS helpme_iud;

USE helpme_iud;

CREATE TABLE IF NOT EXISTS roles(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(150) NULL,
    PRIMARY KEY(id),
    UNIQUE(nombre)
);

CREATE TABLE IF NOT EXISTS usuarios(
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(150) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    apellido VARCHAR(150) NULL,
    password VARCHAR(250) NOT NULL,
    fecha_nacimiento DATE NULL,
    enabled BOOLEAN NULL DEFAULT 1,
    image VARCHAR(250) NULL DEFAULT 'https://happytravel.viajes/wp-content/uploads/2020/04/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png',
    red_social BOOLEAN NULL DEFAULT 0,
    PRIMARY KEY(id),
    UNIQUE(username)
);

CREATE TABLE IF NOT EXISTS roles_usuarios(
    roles_id INT NOT NULL,
    usuarios_id INT NOT NULL,
    PRIMARY KEY(roles_id, usuarios_id),
    FOREIGN KEY (roles_id) REFERENCES roles(id) ,
    FOREIGN KEY (usuarios_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS delitos(
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(250) NULL,
    usuarios_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (usuarios_id) REFERENCES usuarios(id),
    UNIQUE(nombre)
);

CREATE TABLE IF NOT EXISTS casos(
    id INT NOT NULL AUTO_INCREMENT,
    fecha_hora DATETIME NULL DEFAULT now(),
    latitud FLOAT NULL,
    longitud FLOAT NULL,
    altitud FLOAT NULL,
    visible BOOLEAN NULL DEFAULT 1,
    descripcion VARCHAR(250) NULL,
    url_map VARCHAR(250) NULL,
    rmi_url VARCHAR(250) NULL,
    usuarios_id INT NOT NULL,
    delitos_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (usuarios_id) REFERENCES usuarios(id),
    FOREIGN KEY (delitos_id) REFERENCES delitos(id)
);
