package co.edu.iudigital.app.service.impl;

import co.edu.iudigital.app.dto.CasoDTO;
import co.edu.iudigital.app.exceptions.BadRequestException;
import co.edu.iudigital.app.exceptions.ErrorDto;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.model.Caso;
import co.edu.iudigital.app.model.Delito;
import co.edu.iudigital.app.model.Usuario;
import co.edu.iudigital.app.repository.ICasoRepository;
import co.edu.iudigital.app.repository.IDelitoRepository;
import co.edu.iudigital.app.repository.IUsuarioRepository;
import co.edu.iudigital.app.service.iface.ICasoService;
import lombok.extern.slf4j.Slf4j;
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
// Anotación Slf4j proporciona logging
@Slf4j
public class CasoServiceImpl implements ICasoService {

    // Anotación Autowired permite la inyección de dependencias
    @Autowired
    private ICasoRepository casoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IDelitoRepository delitoRepository;

    // Anotación Transactional indica que los métodos de esta clase son transaccionales
    @Transactional(readOnly = true)
    @Override
    public List<CasoDTO> consultarTodos() {
        log.info("consultando todos los casos{}");
        List<Caso> casos = casoRepository.findAll();
        // Programación funcional: Lambdas Java
        return casos.stream().map(caso ->
                CasoDTO.builder()
                        .id(caso.getId())
                        .descripcion(caso.getDescripcion())
                        .altitud(caso.getAltitud())
                        .latitud(caso.getLatitud())
                        .longitud(caso.getLongitud())
                        .esVisible(caso.getEsVisible())
                        .fechaHora(caso.getFechaHora())
                        .rmiUrl(caso.getRmiUrl())
                        .urlMap(caso.getUrlMap())
                        .usuarioId(caso.getUsuario().getId())
                        .delitoId(caso.getDelito().getId())
                        .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Caso crear(CasoDTO casoDTO) throws RestException {
        Optional<Usuario> usuario = usuarioRepository.findById(casoDTO.getUsuarioId());
        Optional<Delito> delito = delitoRepository.findById(casoDTO.getDelitoId());
        if (!usuario.isPresent() || !delito.isPresent()) {
            log.error("No existe usuario {}", casoDTO.getUsuarioId());
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe usuario o delito")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        Caso caso = new Caso();
        caso.setFechaHora(casoDTO.getFechaHora());
        caso.setLatitud(casoDTO.getLatitud());
        caso.setLongitud(casoDTO.getLongitud());
        caso.setAltitud(casoDTO.getAltitud());
        caso.setDescripcion(casoDTO.getDescripcion());
        caso.setEsVisible(true);
        caso.setUrlMap(casoDTO.getUrlMap());
        caso.setRmiUrl(casoDTO.getRmiUrl());
        caso.setUsuario(usuario.get());
        caso.setDelito(delito.get());
        return casoRepository.save(caso);
    }

    @Transactional
    @Override
    public Boolean visible(Boolean visible, Long id) {
        return casoRepository.setVisible(visible, id);
    }

    @Transactional(readOnly = true)
    @Override
    public CasoDTO consultarPorId(Long id) {
        Optional<Caso> casoOptional = casoRepository.findById(id);
        if (casoOptional.isPresent()) {
            Caso caso = casoOptional.get();
            return CasoDTO.builder()
                    .id(caso.getId())
                    .descripcion(caso.getDescripcion())
                    .altitud(caso.getAltitud())
                    .latitud(caso.getLatitud())
                    .longitud(caso.getLongitud())
                    .esVisible(caso.getEsVisible())
                    .fechaHora(caso.getFechaHora())
                    .rmiUrl(caso.getRmiUrl())
                    .urlMap(caso.getUrlMap())
                    .usuarioId(caso.getUsuario().getId())
                    .delitoId(caso.getDelito().getId())
                    .build();
        }
        log.warn("No existe usuario {}", id);
        return null; //TODO: controlar con excepciones
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        casoRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Caso actualizar(Long id, CasoDTO casoDTO) {
        Optional<Caso> casoOptional = casoRepository.findById(id);
        if (casoOptional.isPresent()) {
            Caso caso = casoOptional.get();
            // Aplicar los cambios necesarios desde casoDTO al caso existente
            caso.setDescripcion(casoDTO.getDescripcion());
            caso.setLatitud(casoDTO.getLatitud());
            return casoRepository.save(caso);
        } else {
            // Manejo de caso en el que no se encuentra el caso con el ID dado
            log.warn("No existe caso con ID {}", id);
            return null; //lanzar una excepción, dependiendo de las necesidades
        }
    }
}
