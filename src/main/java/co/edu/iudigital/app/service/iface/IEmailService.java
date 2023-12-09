package co.edu.iudigital.app.service.iface;

// Interfaz IEmailService que define operaciones relacionadas con el envío de correos electrónicos
public interface IEmailService {

    /**
     * Método para enviar un correo electrónico.
     *
     * @param mensaje Contenido del correo electrónico.
     * @param email   Dirección de correo electrónico del destinatario.
     * @param asunto  Asunto del correo electrónico.
     * @return true si el correo electrónico se envió correctamente, false en caso contrario.
     */
    boolean sendEmail(String mensaje, String email, String asunto);
}
