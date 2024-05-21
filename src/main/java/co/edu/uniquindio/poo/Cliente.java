package co.edu.uniquindio.poo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * La clase Cliente representa a un cliente del sistema.
 * Hereda de la clase Usuario y añade atributos y métodos específicos para un cliente.
 */
@Getter
@Setter
public class Cliente extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String contrasena;
    private boolean verificado;
    private String codigoVerificacion;
    private List<String> codigosDescuento;
    private boolean haRealizadoPrimeraCompra; // Nuevo campo

    /**
     * Constructor sin argumentos para la clase Cliente.
     * Inicializa los campos verificado, codigoVerificacion, codigosDescuento y haRealizadoPrimeraCompra.
     */
    public Cliente() {
        super();
        this.verificado = false;
        this.codigoVerificacion = CodeGenerator.generateCode();
        this.codigosDescuento = new ArrayList<>();
        this.haRealizadoPrimeraCompra = false; // Inicialización
    }

    /**
     * Constructor con argumentos para la clase Cliente.
     * 
     * @param identificacion La identificación del cliente.
     * @param nombre El nombre del cliente.
     * @param telefono El teléfono del cliente.
     * @param email El email del cliente.
     * @param contrasena La contraseña del cliente.
     */
    public Cliente(String identificacion, String nombre, String telefono, String email, String contrasena) {
        super(identificacion, nombre, telefono, email);
        this.contrasena = contrasena;
        this.verificado = false;
        this.codigoVerificacion = CodeGenerator.generateCode();
        this.codigosDescuento = new ArrayList<>();
        this.haRealizadoPrimeraCompra = false; // Inicialización
    }

    /**
     * Agrega un código de descuento a la lista de códigos de descuento del cliente.
     * 
     * @param codigo El código de descuento a agregar.
     */
    public void agregarCodigoDescuento(String codigo) {
        this.codigosDescuento.add(codigo);
    }

    /**
     * Valida si un código de descuento está presente en la lista de códigos de descuento del cliente.
     * 
     * @param codigo El código de descuento a validar.
     * @return true si el código está presente, false en caso contrario.
     */
    public boolean validarCodigoDescuento(String codigo) {
        return this.codigosDescuento.contains(codigo);
    }

    /**
     * Elimina un código de descuento de la lista de códigos de descuento del cliente.
     * 
     * @param codigo El código de descuento a eliminar.
     */
    public void eliminarCodigoDescuento(String codigo) {
        this.codigosDescuento.remove(codigo);
    }
}
