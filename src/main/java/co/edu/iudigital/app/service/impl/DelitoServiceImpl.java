package co.edu.iudigital.app.service.impl;

import co.edu.iudigital.app.dto.request.DelitoDTORequest;
import co.edu.iudigital.app.dto.response.DelitoDTO;
import co.edu.iudigital.app.exceptions.BadRequestException;
import co.edu.iudigital.app.exceptions.ErrorDto;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.model.Delito;
import co.edu.iudigital.app.model.Usuario;
import co.edu.iudigital.app.repository.IDelitoRepository;
import co.edu.iudigital.app.repository.IUsuarioRepository;
import co.edu.iudigital.app.service.iface.IDelitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Anotación Service indica que esta clase es un servicio
@Service
public class DelitoServiceImpl implements IDelitoService {

    // Inyección de dependencias por constructor
    private final IDelitoRepository delitoRepository;
    private final IUsuarioRepository usuarioRepository;

    @Autowired
    public DelitoServiceImpl(IDelitoRepository delitoRepository, IUsuarioRepository usuarioRepository) {
        this.delitoRepository = delitoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DelitoDTO> consultarTodos() {
        List<Delito> delitos = delitoRepository.findAll();
        return delitos.stream()
                .map(delito ->
                        DelitoDTO.builder()
                                .id(delito.getId())
                                .nombre(delito.getNombre())
                                .descripcion(delito.getDescripcion())
                                .build()
                ).collect(Collectors.toList());
    }

    @Override
    public DelitoDTO consultarPorId(Long id) {
        // Implementar lógica para consultar un delito por su ID
        return null;
    }

    @Transactional
    @Override
    public DelitoDTO guardarDelito(DelitoDTORequest delitoDTORequest) throws RestException {
        Delito delito = new Delito();
        delito.setNombre(delitoDTORequest.getNombre());
        delito.setDescripcion(delitoDTORequest.getDescripcion());
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(delitoDTORequest.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe usuario")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        delito.setUsuario(usuarioOptional.get());
        delito = delitoRepository.save(delito);
        return DelitoDTO.builder()
                .id(delito.getId())
                .nombre(delito.getNombre())
                .descripcion(delito.getDescripcion())
                .build();
    }

    @Override
    public void borrarDelitoPorId(Long id) {
        // Implementar lógica para borrar un delito por su ID
    }

    @Override
    public DelitoDTO actualizarDelito(Long id, DelitoDTORequest delitoDTORequest) {
        Optional<Delito> delitoOptional = delitoRepository.findById(id);
        if (delitoOptional.isPresent()) {
            Delito delito = delitoOptional.get();
            delito.setNombre(delitoDTORequest.getNombre());
            delito.setDescripcion(delitoDTORequest.getDescripcion());

            delito = delitoRepository.save(delito);

            return DelitoDTO.builder()
                    .id(delito.getId())
                    .nombre(delito.getNombre())
                    .descripcion(delito.getDescripcion())
                    .build();
        } else {
            // Manejar el caso en el que no se encuentre el delito con el ID especificado.
            // Se puede lanzar una excepción, devolver un DTO vacío o realizar alguna otra acción.
            return null;
        }
    }

    @Override
    public void eliminarDelito(Long id) {
        Optional<Delito> delitoOptional = delitoRepository.findById(id);
        if (delitoOptional.isPresent()) {
            delitoRepository.delete(delitoOptional.get());
        } else {
            // Manejar el caso en el que no se encuentre el delito con el ID especificado.
            // Se puede lanzar una excepción, loggear un mensaje.
        }
    }
}
