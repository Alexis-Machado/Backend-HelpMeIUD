package co.edu.iudigital.app.auth;

import co.edu.iudigital.app.model.Usuario;
import co.edu.iudigital.app.service.iface.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Adicionar info del usuario al token
 * o cualquier otra
 * luego se registra en AuthorizationServerConfig
 * @author JULIOCESARMARTINEZ
 *
 */
@Component
public class TokenMoreInfo implements TokenEnhancer {

	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		// Se obtiene el usuario a partir del nombre de usuario en la autenticación
		Usuario usuario = usuarioService.findByUsername(authentication.getName());

		// Se crea un mapa para almacenar información adicional en el token
		Map<String, Object> info = new HashMap<>();
		info.put("id_usuario", "" + usuario.getId());
		info.put("nombre", usuario.getNombre());
		info.put("image", usuario.getImage());
		info.put("fecha_nacimiento", "" + usuario.getFechaNacimiento());

		// Se agrega la información adicional al token
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		// Se devuelve el token modificado
		return accessToken;
	}
}
