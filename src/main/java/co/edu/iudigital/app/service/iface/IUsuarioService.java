package co.edu.iudigital.app.service.iface;

import co.edu.iudigital.app.dto.request.UsuarioDTORequest;
import co.edu.iudigital.app.dto.response.UsuarioDTO;
import co.edu.iudigital.app.exceptions.InternalServerErrorException;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.model.Usuario;
import org.springframework.security.core.Authentication;

import java.util.List;

// Interfaz IUsuarioService que define operaciones relacionadas con Usuario
public interface IUsuarioService {

    // Método para consultar todos los usuarios y retornar una lista de UsuarioDTO
    List<UsuarioDTO> consultarTodos();

    // Método para consultar un usuario por su identificador y retornar un UsuarioDTO
    UsuarioDTO consultarPorId(Long id);

    // Método para consultar un usuario por su nombre de usuario y retornar un UsuarioDTO
    UsuarioDTO consultarPorUsername(String username);

    // Método para guardar un usuario a partir de un UsuarioDTORequest y retornar un UsuarioDTO
    UsuarioDTO guardar(UsuarioDTORequest usuarioDTORequest) throws RestException;

    // Método para encontrar un usuario por su nombre de usuario y retornar un objeto Usuario
    Usuario findByUsername(String username);

    // Método para obtener información del usuario autenticado y retornar un UsuarioDTO
    UsuarioDTO userInfo(Authentication authentication) throws RestException;

    // Método para actualizar un usuario y retornar un UsuarioDTO
    Usuario actualizar(Usuario usuario) throws RestException;

    // Método para eliminar un usuario por su identificador
    void eliminarUsuario(Long id) throws InternalServerErrorException;
}
