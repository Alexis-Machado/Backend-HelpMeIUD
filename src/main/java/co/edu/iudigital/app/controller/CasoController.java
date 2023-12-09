package co.edu.iudigital.app.controller;

import co.edu.iudigital.app.dto.CasoDTO;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.model.Caso;
import co.edu.iudigital.app.service.iface.ICasoService;
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

// Este controlador gestiona las operaciones relacionadas con los casos
@RestController
@RequestMapping("/casos")
@Api(value = "/casos", tags = {"Casos"})
@SwaggerDefinition(tags = { @Tag (name = "Casos", description = "Gestión de Casos API") })

public class CasoController {

    // Inyección de dependencia del servicio de casos
    @Autowired
    private ICasoService casoService;

    // Operación para obtener todos los casos
    @ApiOperation(value = "Obtiene todos los casos", responseContainer = "List", produces = "application/json", httpMethod = "GET")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<CasoDTO>> index() {
        return ResponseEntity.ok().body(casoService.consultarTodos()
                );
    }

    // Operación para crear un nuevo caso
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation(value = "Crea un caso", response = Caso.class, responseContainer = "Caso", produces = "application/json", httpMethod = "POST")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Caso> create(@RequestBody @Valid CasoDTO casoDTO) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(casoService.crear(casoDTO)
                );
    }

    // Operación para actualizar un caso existente
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation(value = "Actualiza un caso", response = Caso.class, responseContainer = "Caso", produces = "application/json", httpMethod = "PUT")
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Caso> update(@PathVariable Long id, @RequestBody @Valid CasoDTO casoDTO
    ) throws RestException {
        return ResponseEntity.ok().body(casoService.actualizar(id, casoDTO));
    }

    // Operación para eliminar un caso existente
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation(value = "Elimina un caso", produces = "application/json", httpMethod = "DELETE")
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> delete(@PathVariable Long id
    ) throws RestException {
        casoService.eliminar(id);
        String mensaje = "Caso eliminado con éxito";
        return ResponseEntity.ok(mensaje);
    }
}
