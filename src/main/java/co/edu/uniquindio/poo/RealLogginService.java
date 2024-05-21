package co.edu.uniquindio.poo;

/**
 * La clase RealLogginService implementa la interfaz LoginService y proporciona la lógica
 * real de autenticación de usuarios.
 */
public class RealLogginService implements LoginService {
    private Unieventos unieventos;

    /**
     * Constructor de la clase RealLogginService.
     *
     * @param unieventos La instancia de Unieventos que se usará para la autenticación.
     */
    public RealLogginService(Unieventos unieventos) {
        this.unieventos = unieventos;
    }

    /**
     * Método de autenticación de usuarios.
     *
     * @param identificacion La identificación del usuario.
     * @param contrasena     La contraseña del usuario.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    @Override
    public boolean login(String identificacion, String contrasena) {
        Cliente cliente = unieventos.buscarClientePorIdentificacion(identificacion);
        if (cliente != null && cliente.getContrasena().equals(contrasena)) {
            if (!cliente.isVerificado()) {
                System.out.println("Debe verificar su cuenta ingresando el código de verificación.");
                return false;
            }
            unieventos.setUsuarioActual(cliente);
            System.out.println("Login exitoso. Bienvenido, " + cliente.getNombre() + "!");
            return true;
        }
        System.out.println("Identificación o contraseña incorrecta.");
        return false;
    }
}
