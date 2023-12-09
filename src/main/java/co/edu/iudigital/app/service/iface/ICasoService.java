package co.edu.iudigital.app.service.iface;

import co.edu.iudigital.app.dto.CasoDTO;
import co.edu.iudigital.app.exceptions.RestException;
import co.edu.iudigital.app.model.Caso;

import java.util.List;

// Interfaz ICasoService que define operaciones relacionadas con Caso
public interface ICasoService {

    // Método para consultar todos los casos y retornar una lista de CasoDTO
    List<CasoDTO> consultarTodos();

    // Método para crear un caso a partir de un CasoDTO y lanzar una RestException en caso de error
    Caso crear(CasoDTO casoDTO) throws RestException;

    // Método para actualizar la visibilidad de un caso y retornar un valor booleano
    Boolean visible(Boolean visible, Long id);

    // Método para consultar un caso por su identificador y retornar un CasoDTO
    CasoDTO consultarPorId(Long id);

    // Método para eliminar un caso por su identificador
    void eliminar(Long id);

    // Método para actualizar un caso por su identificador y un CasoDTO
    Caso actualizar(Long id, CasoDTO casoDTO);
}
