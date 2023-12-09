package co.edu.iudigital.app.controller;

import co.edu.iudigital.app.dto.request.UsuarioDTORequest;
import co.edu.iudigital.app.dto.response.UsuarioDTO;
import co.edu.iudigital.app.exceptions.*;
import co.edu.iudigital.app.model.Usuario;
import co.edu.iudigital.app.service.iface.IUsuarioService;
import co.edu.iudigital.app.util.ConstUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Este controlador gestiona las operaciones relacionadas con los usuarios
@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000/")
@Api(value = "/usuarios", tags = {"Usuarios"})
@SwaggerDefinition(tags = { @Tag(name = "Usuarios", description = "Gestión de Usuarios API") })

public class UsuarioController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IUsuarioService usuarioService;

    // Operación para registrar un nuevo usuario
    @PostMapping("/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<UsuarioDTO> create(@RequestBody @Valid UsuarioDTORequest usuarioDTORequest
    ) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDTORequest)
                );
    }

    // Operación para consultar la información del usuario autenticado actualmente
    @ApiOperation(value = "Consultar el usuario autenticado", response = UsuarioDTO.class, produces = "application/json", httpMethod = "GET")
    @GetMapping("/usuario")
    public ResponseEntity<UsuarioDTO> userInfo(Authentication authentication) throws RestException {
        return ResponseEntity.ok().body(usuarioService.userInfo(authentication));
    }

    // Operación para subir la imagen de un usuario
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile image, Authentication authentication
    ) throws RestException {
        Map<String, Object> response = new HashMap<>();
        Usuario usuario = usuarioService.findByUsername(authentication.getName());
        if (!image.isEmpty()) {
            String nombreImage =
                    UUID.randomUUID().toString() + "_" + image.getOriginalFilename().replace(" ", "");
            Path path = Paths.get("uploads").resolve(nombreImage).toAbsolutePath();
            try {
                Files.copy(image.getInputStream(), path);
            } catch (IOException e) {
                response.put("Error", e.getMessage().concat(e.getCause().getMessage()));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String oldImage = usuario.getImage();

            if (oldImage != null && oldImage.length() > 0 && !oldImage.startsWith("http")) {
                Path oldPath = Paths.get("uploads").resolve(oldImage).toAbsolutePath();
                File oldFileImage = oldPath.toFile();
                if (oldFileImage.exists() && oldFileImage.canRead()) {
                    oldFileImage.delete();
                }
            }

            usuario.setImage(nombreImage);
            usuarioService.actualizar(usuario);
            response.put("usuario", usuario);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Operación para actualizar un usuario
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation(value = "Actualiza un usuario", response = Usuario.class, produces = "application/json", httpMethod = "PUT")
    @PutMapping("/usuario")
    public ResponseEntity<Usuario> update(Authentication authentication, @RequestBody Usuario usuario) throws RestException {
        try {
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
            Usuario usuarioFind = usuarioService.findByUsername(authentication.getName());
            if (usuarioFind == null) {
                throw new NotFoundException(
                        ErrorDto.builder()
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message(ConstUtil.MESSAGE_NOT_FOUND)
                                .status(HttpStatus.NOT_FOUND.value())
                                .date(LocalDateTime.now())
                                .build()
                );
            }
            usuarioFind.setUsername(usuario.getUsername());
            usuarioFind.setNombre(usuario.getNombre());
            usuarioFind.setApellido(usuario.getApellido());
            usuarioFind.setFechaNacimiento(usuario.getFechaNacimiento());
            if (usuario.getPassword() != null) {
                usuarioFind.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.actualizar(usuarioFind));
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            // log.error("Error {}", ex);
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

    // Obtiene la imagen de un usuario
    //@Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/uploads/img/{name:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String name) throws InternalServerErrorException {
        Path path = Paths.get("uploads").resolve(name).toAbsolutePath();
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                try {
                    path = Paths.get("uploads").resolve("default.png").toAbsolutePath();
                    resource = new UrlResource(path.toUri());
                } catch (MalformedURLException ex) {
                    throw new InternalServerErrorException(
                            ErrorDto.builder()
                                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                    .message(ex.getMessage())
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .date(LocalDateTime.now())
                                    .build());
                }
            }
        } catch (MalformedURLException e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // Operación para eliminar un usuario
    @ApiOperation(value = "Elimina un usuario", produces = "application/json", httpMethod = "DELETE")
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) throws RestException {
        try {
            usuarioService.eliminarUsuario(id);
            String mensaje = "Usuario eliminado con éxito";
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Usuario-Eliminado-Mensaje", mensaje);
            return ResponseEntity.noContent().headers(headers).build();
        } catch (Exception ex) {
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
}
