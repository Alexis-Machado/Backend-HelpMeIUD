package co.edu.iudigital.app.exceptions;

// Declaración de la clase BadRequestException que extiende RestException
public class BadRequestException extends RestException {
    private static final long serialVersionUID = 1L;

    // Constructor predeterminado sin argumentos
    public BadRequestException() {
        super();
    }

    // Constructor que toma un objeto ErrorDto como argumento
    public BadRequestException(ErrorDto errorDto) {
        super(errorDto);
    }

    // Constructor que toma un mensaje de error como argumento
    public BadRequestException(String msg) {
        super(msg);
    }
}
