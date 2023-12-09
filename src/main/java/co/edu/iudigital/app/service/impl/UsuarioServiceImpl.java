package co.edu.iudigital.app.service.impl;

import co.edu.iudigital.app.dto.request.UsuarioDTORequest;
import co.edu.iudigital.app.dto.response.UsuarioDTO;
import co.edu.iudigital.app.exceptions.BadRequestException;
import co.edu.iudigital.app.exceptions.ErrorDto;
import co.edu.iudigital.app.exceptions.InternalServerErrorException;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.model.Role;
import co.edu.iudigital.app.model.Usuario;
import co.edu.iudigital.app.repository.IUsuarioRepository;
import co.edu.iudigital.app.service.iface.IUsuarioService;
import co.edu.iudigital.app.util.ConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Anotación Service indica que esta clase es un servicio
@Service
// Anotación para logging con SLF4J
@Slf4j
// Implementación de la interfaz UserDetailsService para la autenticación de Spring Security
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {

    // Valor de la propiedad emailserver.enabled desde el archivo de propiedades
    @Value("${emailserver.enabled}")
    private Boolean emailEnabled;

    // Inyección de dependencia del BCryptPasswordEncoder
    @Lazy
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Inyección de dependencia del repositorio de Usuario
    @Autowired
    private IUsuarioRepository usuarioRepository;

    // Inyección de dependencia del servicio de Email
    @Autowired
    private EmailService emailService;

    // Método para consultar todos los usuarios (no implementado en este código)
    @Override
    public List<UsuarioDTO> consultarTodos() {
        return null;
    }

    // Método para consultar un usuario por su ID (no implementado en este código)
    @Override
    public UsuarioDTO consultarPorId(Long id) {
        return null;
    }

    // Método para consultar un usuario por su nombre de usuario (no implementado en este código)
    @Override
    public UsuarioDTO consultarPorUsername(String username) {
        return null;
    }

    // Método para guardar un nuevo usuario
    @Override
    public UsuarioDTO guardar(UsuarioDTORequest usuarioDTORequest) throws RestException {
        // Declaración de variables locales
        Usuario usuario;
        Role role = new Role();
        role.setId(2L);

        // Verificación de existencia del usuario por nombre de usuario
        usuario = usuarioRepository.findByUsername(usuarioDTORequest.getUsername());
        if (usuario != null) {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Este usuario ya existe")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        // Creación de un nuevo usuario
        usuario = new Usuario();
        usuario.setNombre(usuarioDTORequest.getNombre());
        usuario.setUsername(usuarioDTORequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTORequest.getPassword()));
        usuario.setApellido(usuarioDTORequest.getApellido());
        usuario.setImage(usuarioDTORequest.getImage());
        usuario.setEnabled(true);
        usuario.setRedSocial(false);
        usuario.setFechaNacimiento(usuarioDTORequest.getFechaNacimiento());
        usuario.setRoles(Collections.singletonList(role));

        // Guardar el nuevo usuario en la base de datos
        usuario = usuarioRepository.save(usuario);

        // Envío de correo electrónico si la propiedad emailserver.enabled está habilitada
        if (usuario != null && usuario.getUsername() != null && Boolean.TRUE.equals(emailEnabled)) {
            String mensaje = "Su usuario: " + usuario.getUsername() + "; password: " + usuarioDTORequest.getPassword();
            String asunto = "Registro en HelmeIUD";
            emailService.sendEmail(mensaje, usuario.getUsername(), asunto);
        }

        // Construcción y retorno del DTO del usuario guardado
        return UsuarioDTO.builder()
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .enabled(usuario.getEnabled())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .redSocial(usuario.getRedSocial())
                .image(usuario.getImage())
                .roles(usuario.getRoles()
                        .stream().map(r -> r.getNombre())
                        .collect(Collectors.toList()))
                .build();
    }

    // Método para buscar un usuario por nombre de usuario
    @Override
    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Método para obtener la información de un usuario autenticado
    @Override
    public UsuarioDTO userInfo(Authentication authentication) throws RestException {
        // Verificación de autenticación
        if (!authentication.isAuthenticated()) {
            throw new RestException(
                    ErrorDto.builder()
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_NOT_AUTHORIZED)
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        // Obtención del nombre de usuario autenticado
        String userName = authentication.getName();

        // Búsqueda del usuario en la base de datos por nombre de usuario
        Usuario usuario = usuarioRepository.findByUsername(userName);

        // Manejo de casos en los que el usuario no existe
        if (usuario == null) {
            throw new RestException(
                    ErrorDto.builder()
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_NOT_AUTHORIZED)
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        // Construcción y retorno del DTO del usuario
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .image(usuario.getImage())
                .roles(usuario.getRoles()
                        .stream().map(r -> r.getNombre())
                        .collect(Collectors.toList()))
                .username(usuario.getUsername())
                .build();
        return usuarioDTO;
    }

    // Método para actualizar un usuario
    @Override
    public Usuario actualizar(Usuario usuario) throws RestException {
        // Validación de datos del usuario
        if (usuario == null || usuario.getId() == null || !usuarioRepository.existsById(usuario.getId())) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_ERROR_DATA)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        // Guardar el usuario actualizado en la base de datos
        return usuarioRepository.save(usuario);
    }

    // Método para eliminar un usuario por su ID
    @Override
    public void eliminarUsuario(Long id) throws InternalServerErrorException {
        // Intento de eliminación del usuario por ID
        try {
            usuarioRepository.deleteById(id);
            log.info("Usuario eliminado con éxito, ID: {}", id);
        } catch (Exception ex) {
            // Manejo de errores durante la eliminación
            log.error("Error al eliminar usuario con ID: {}", id, ex);
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                            .message(ConstUtil.MESSAGE_GENERAL)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
    }

    // Método de la interfaz UserDetailsService para cargar el usuario por nombre de usuario
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Búsqueda del usuario por nombre de usuario
        Usuario usuario = usuarioRepository.findByUsername(username);

        // Manejo de casos en los que el usuario no existe
        if (usuario == null) {
            log.error("Error de login, no existe usuario: " + usuario);
            throw new UsernameNotFoundException("Error de login, no existe usuario: " + username);
        }

        // Construcción de la lista de roles para el usuario
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : usuario.getRoles()) {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getNombre());
            log.info("Rol {}", authority.getAuthority());
            authorities.add(authority);
        }

        // Construcción y retorno del UserDetails de Spring Security
        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }
}
