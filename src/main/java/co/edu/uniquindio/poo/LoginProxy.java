package co.edu.uniquindio.poo;

/**
 * La clase LoginProxy actúa como un intermediario para el servicio de autenticación,
 * permitiendo agregar lógica adicional como controles de acceso o registro de intentos de login.
 */
public class LoginProxy implements LoginService {
    private RealLogginService realLogginService;

    /**
     * Constructor de la clase LoginProxy.
     *
     * @param unieventos La instancia de Unieventos que se usará para crear el servicio de login real.
     */
    public LoginProxy(Unieventos unieventos) {
        this.realLogginService = new RealLogginService(unieventos);
    }

    /**
     * Método de autenticación que llama al servicio de autenticación real.
     *
     * @param identificacion La identificación del usuario.
     * @param contrasena     La contraseña del usuario.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    @Override
    public boolean login(String identificacion, String contrasena) {
        // Aquí puedes agregar lógica adicional, como controles de acceso o registro de intentos de login
        return realLogginService.login(identificacion, contrasena);
    }
}
