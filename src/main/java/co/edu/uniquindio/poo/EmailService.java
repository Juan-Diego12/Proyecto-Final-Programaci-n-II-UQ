package co.edu.uniquindio.poo;

import javax.mail.*;
import javax.mail.internet.*;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * La clase EmailService se encarga de enviar correos electrónicos.
 */
public class EmailService {
    private final String username;
    private final String password;
    private final Properties props;

    /**
     * Constructor para la clase EmailService.
     * Configura las propiedades para la conexión SMTP y deshabilita la verificación SSL.
     *
     * @param username El nombre de usuario del correo electrónico.
     * @param password La contraseña del correo electrónico.
     */
    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");

        disableSSLVerification();
    }

    /**
     * Envía un correo electrónico simple.
     *
     * @param to      El destinatario del correo.
     * @param subject El asunto del correo.
     * @param text    El texto del correo.
     * @throws MessagingException Si ocurre un error al enviar el correo.
     */
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);
    }

    /**
     * Envía un correo electrónico con un archivo adjunto.
     *
     * @param to        El destinatario del correo.
     * @param subject   El asunto del correo.
     * @param text      El texto del correo.
     * @param filePath  La ruta del archivo adjunto.
     * @throws MessagingException Si ocurre un error al enviar el correo.
     */
    public void sendEmailWithAttachment(String to, String subject, String text, String filePath) throws MessagingException {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(text);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart attachPart = new MimeBodyPart();
        try {
            attachPart.attachFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        multipart.addBodyPart(attachPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    /**
     * Deshabilita la verificación SSL para evitar problemas de certificados en el entorno de desarrollo.
     */
    private void disableSSLVerification() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }}, new java.security.SecureRandom());
            SSLContext.setDefault(sslContext);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
