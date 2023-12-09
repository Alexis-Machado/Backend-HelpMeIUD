package co.edu.iudigital.app.exceptions;

// Declaración de la clase InternalServerErrorException que extiende RestException
public class InternalServerErrorException extends RestException {
    private static final long serialVersionUID = 1L;
    private String codigoError; // Código de error específico asociado a la excepción

    // Constructor que toma un mensaje, un código de error y una excepción como argumentos
    public InternalServerErrorException(String msg, String codigoError, Exception ex) {
        super(msg, ex);
        this.codigoError = codigoError;
    }

    // Constructor que toma un mensaje y una excepción como argumentos
    public InternalServerErrorException(String msg, Exception ex) {
        super(msg, ex);
    }

    // Constructor predeterminado sin argumentos
    public InternalServerErrorException() {
        super();
    }

    // Constructor que toma un objeto ErrorDto como argumento
    public InternalServerErrorException(ErrorDto errorDto) {
        super(errorDto);
    }

    // Método getter para obtener el código de error asociado a la excepción
    public String getCodigoError() {
        return codigoError;
    }
}
