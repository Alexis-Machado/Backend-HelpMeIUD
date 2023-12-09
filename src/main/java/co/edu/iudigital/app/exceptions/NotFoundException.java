package co.edu.iudigital.app.exceptions;

// Declaración de la clase NotFoundException que extiende RestException
public class NotFoundException extends RestException {
    private static final long serialVersionUID = 1L;

    // Constructor predeterminado sin argumentos
    public NotFoundException() {
        super();
    }

    // Constructor que toma un objeto ErrorDto como argumento
    public NotFoundException(ErrorDto errorDto) {
        super(errorDto);
    }
}
