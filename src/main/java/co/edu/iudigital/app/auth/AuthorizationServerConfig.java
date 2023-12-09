package co.edu.iudigital.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;


@Configuration
@EnableAuthorizationServer

public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${security.jwt.client-service}")
	private String client;

	@Value("${security.jwt.password-service}")
	private String secret;

	@Value("${security.jwt.scope-read}")
	private String read;

	@Value("${security.jwt.scope-write}")
	private String write;

	@Value("${security.jwt.grant-password}")
	private String grantPassword;

	@Value("${security.jwt.grant-refresh}")
	private String grantRefresh;

	@Value("${security.jwt.token-validity-seconds}")
	private Integer accessTime;

	@Value("${security.jwt.refresh-validity-seconds}")
	private Integer refreshTime;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenMoreInfo tokenMoreInfo;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// Registro de información adicional con la creación del token
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenMoreInfo, accessTokenConverter()));
		// Registro de endpoints
		endpoints.authenticationManager(authenticationManager)
				.tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter())
				.tokenEnhancer(tokenEnhancerChain);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// Configuración de permisos para rutas de acceso
		security
				.passwordEncoder(passwordEncoder)
				.tokenKeyAccess("permitAll()")  // Permisos para usuarios anónimos o no autenticados
				.checkTokenAccess("isAuthenticated()");  // Chequea o valida el token, solo accesible por usuarios autenticados
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// Configuración de clientes
		clients.inMemory()
				.withClient(client)
				.secret(passwordEncoder.encode(secret))
				.scopes(read, write)
				.authorizedGrantTypes(grantPassword, grantRefresh)
				.accessTokenValiditySeconds(accessTime)
				.refreshTokenValiditySeconds(refreshTime);
	}

	@Bean
	public JwtTokenStore tokenStore() {
		// Creación del almacenamiento de token
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		// Creación del convertidor de token JWT
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVATE);  // Clave secreta
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLIC);
		return jwtAccessTokenConverter;
	}
}
