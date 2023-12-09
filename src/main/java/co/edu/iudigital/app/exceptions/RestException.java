package co.edu.iudigital.app.exceptions;

// Declaración de la clase RestException que extiende Exception
public class RestException extends Exception {
    private static final long serialVersionUID = 1L;
    private ErrorDto errorDto; // Objeto ErrorDto asociado a la excepción

    // Constructor predeterminado sin argumentos
    public RestException() {
        super();
    }

    // Constructor que toma un objeto ErrorDto como argumento
    public RestException(ErrorDto errorDto) {
        super(errorDto.getError());
        this.errorDto = errorDto;
    }

    // Constructor que toma un mensaje como argumento
    public RestException(String msg) {
        super(msg);
    }

    // Constructor que toma un mensaje y una excepción como argumentos
    public RestException(String msg, Exception ex) {
        super(msg, ex);
    }

    /**
     * @return el errorDto asociado a la excepción
     */
    public ErrorDto getErrorDto() {
        return errorDto;
    }

    /**
     * @param errorDto el errorDto a establecer
     */
    public void setErrorDto(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }
}
