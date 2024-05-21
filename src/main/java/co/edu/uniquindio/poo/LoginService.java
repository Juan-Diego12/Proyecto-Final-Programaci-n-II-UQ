package co.edu.uniquindio.poo;

/**
 * La interfaz LoginService define el contrato para los servicios de autenticación.
 */
public interface LoginService {
    /**
     * Método de autenticación de usuarios.
     *
     * @param identificacion La identificación del usuario.
     * @param contrasena     La contraseña del usuario.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    boolean login(String identificacion, String contrasena);
}
