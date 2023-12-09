package co.edu.iudigital.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//Clase principal de la aplicación Spring Boot HELPMEIUD
@SpringBootApplication
public class HelpmeiudApplication {

	// Método principal que inicia la aplicación
	public static void main(String[] args) {
		SpringApplication.run(HelpmeiudApplication.class, args);
	}
}

