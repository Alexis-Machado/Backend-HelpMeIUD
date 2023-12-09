package co.edu.iudigital.app.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableResourceServer

public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	// Configuración de protección del lado de OAuth2
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				// Se especifican rutas más específicas a más generales o genéricas
				// URLs abiertas sin autenticación ni autorización (se puede simplificar con anotaciones @Secured)
				.antMatchers(HttpMethod.GET, "/delitos").permitAll()
				.antMatchers(HttpMethod.POST, "/usuarios/signup**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/usuarios/signup**").permitAll()
				.antMatchers(HttpMethod.GET, "/casos", "/casos/caso/**").permitAll()
				// Autorizaciones específicas (sobreescribe la genérica correspondiente)
				.antMatchers(HttpMethod.GET, "/usuarios/uploads/img/**").permitAll()
				// Genéricas
				.anyRequest().authenticated()  // Las rutas no especificadas serán para usuarios autenticados
				.and()
				.cors().configurationSource(corsConfigurationSource())  // Configuración de CORS
				.and()
				.csrf().disable();  // Se deshabilita CSRF
	}

	// Configuración de CORS
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
		config.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);  // Permitir credenciales
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

		// Registramos la configuración
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);  // Para todas las rutas del back
		return source;
	}

	// Configuración del filtro CORS
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);  // Establece la prioridad más alta
		// Como es el filtro más alto, es suficiente para aplicar a todos los controllers
		return bean;
	}
}
