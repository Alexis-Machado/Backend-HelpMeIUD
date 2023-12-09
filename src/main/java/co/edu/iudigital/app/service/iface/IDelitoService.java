package co.edu.iudigital.app.service.iface;

import co.edu.iudigital.app.dto.request.DelitoDTORequest;
import co.edu.iudigital.app.dto.response.DelitoDTO;
import co.edu.iudigital.app.exceptions.RestException;

import java.util.List;

// Interfaz IDelitoService que define operaciones relacionadas con Delito
public interface IDelitoService {

    // Método para consultar todos los delitos y retornar una lista de DelitoDTO
    List<DelitoDTO> consultarTodos();

    // Método para consultar un delito por su identificador y retornar un DelitoDTO
    DelitoDTO consultarPorId(Long id);

    // Método para guardar un delito a partir de un DelitoDTORequest y retornar un DelitoDTO
    DelitoDTO guardarDelito(DelitoDTORequest delitoDTORequest) throws RestException;

    // Método para borrar un delito por su identificador
    void borrarDelitoPorId(Long id);

    // Método para actualizar un delito por su identificador y un DelitoDTORequest, y retornar un DelitoDTO
    DelitoDTO actualizarDelito(Long id, DelitoDTORequest delitoDTORequest);

    // Método para eliminar un delito por su identificador
    void eliminarDelito(Long id);
}
