package co.edu.iudigital.app.controller;

import co.edu.iudigital.app.dto.request.DelitoDTORequest;
import co.edu.iudigital.app.dto.response.DelitoDTO;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.service.iface.IDelitoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// Este controlador gestiona las operaciones relacionadas con los delitos
@RestController
@RequestMapping("/delitos")
@CrossOrigin(origins = "http://localhost:3000/")
@Api(value = "/delitos", tags = {"Delitos"})
@SwaggerDefinition(tags = {@Tag(name = "Delitos", description = "Gestión de Delitos API")})

public class DelitoController {

    @Autowired
    IDelitoService delitoService;

    // Operación para obtener todos los delitos
    @ApiOperation(value = "Obtiene todos los delitos", responseContainer = "List", produces = "application/json", httpMethod = "GET")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<DelitoDTO>> index() {
        return ResponseEntity.ok().body(delitoService.consultarTodos()
                );
    }

    // Operación para guardar un nuevo delito
    @ApiOperation(value = "Guardar un Delito", response = DelitoDTO.class, responseContainer = "DelitoDTO", produces = "application/json", httpMethod = "POST")
    @Secured("ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DelitoDTO> create(@RequestBody @Valid DelitoDTORequest delitoDTORequest
    ) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(delitoService.guardarDelito(delitoDTORequest)
                );
    }

    // Operación para actualizar un delito existente
    @ApiOperation(value = "Actualiza un Delito", produces = "application/json", httpMethod = "PUT")
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<DelitoDTO> update(@PathVariable Long id, @RequestBody @Valid DelitoDTORequest delitoDTORequest
    ) throws RestException {
        return ResponseEntity.ok(delitoService.actualizarDelito(id, delitoDTORequest));
    }

    // Operación para eliminar un delito existente
    @Secured({"ROLE_ADMIN"})
    @ApiOperation(value = "Elimina un Delito", produces = "application/json", httpMethod = "DELETE")
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> eliminarDelito(@PathVariable Long id
    ) throws RestException {
        delitoService.eliminarDelito(id);
        String mensaje = "Delito eliminado con éxito";
        return ResponseEntity.ok(mensaje);
    }
}
