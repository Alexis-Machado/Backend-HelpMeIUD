package co.edu.iudigital.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)  // Para usar @Secured
@EnableWebSecurity

public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService usuarioService;

	// Registra el objeto como componente de Spring, retorna un encriptador de contraseñas BCrypt
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * Registra el UserDetailsService, configura el password encoder y establece
	 * la implementación del encriptador de contraseñas.
	 */
	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
	}

	/**
	 * Coloca el AuthenticationManager como bean para usarlo en el servidor de autorización.
	 */
	@Bean("authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// Configuración de seguridad en el lado de Spring
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("**/swagger-ui.html").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()  // Sin protección de formularios para prevenir ataques CSRF en capas React paradise del backend
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	// Configuración específica para ignorar ciertos recursos de Swagger y otros
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/app/**/*.{js,html}")
				.antMatchers("/i18n/**")
				.antMatchers("/content/**")
				.antMatchers("/h2-console/**")
				.antMatchers("/swagger-ui/index.html")
				.antMatchers("/swagger-ui.html")
				.antMatchers("/v2/api-docs")
				.antMatchers("/test/**");
	}
}
