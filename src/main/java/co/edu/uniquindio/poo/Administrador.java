package co.edu.uniquindio.poo;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * La clase Administrador representa a un administrador del sistema.
 * Esta clase es un Singleton, lo que significa que solo puede haber una instancia de Administrador.
 */
public class Administrador extends Usuario {
    private static Administrador instance;
    private String contrasena;

    /**
     * Constructor privado para crear una instancia del administrador.
     * Este constructor es privado para implementar el patrón Singleton.
     *
     * @param identificacion La identificación del administrador.
     * @param nombre         El nombre del administrador.
     * @param telefono       El teléfono del administrador.
     * @param email          El email del administrador.
     * @param contrasena     La contraseña del administrador.
     */
    private Administrador(String identificacion, String nombre, String telefono, String email, String contrasena) {
        super(identificacion, nombre, telefono, email);
        this.contrasena = contrasena;
    }

    /**
     * Obtiene la instancia única de la clase Administrador.
     * Si no existe una instancia, se crea una nueva.
     *
     * @return La instancia única de la clase Administrador.
     */
    public static Administrador getInstance() {
        if (instance == null) {
            instance = new Administrador("admin", "Administrador", "0000000000", "admin@unieventos.com", "admin123");
        }
        return instance;
    }

    /**
     * Obtiene la contraseña del administrador.
     *
     * @return La contraseña del administrador.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Gestiona la creación de un evento y lo agrega a la lista de eventos del sistema.
     *
     * @param uniEventos   La instancia del sistema de eventos.
     * @param nombre       El nombre del evento.
     * @param ciudad       La ciudad donde se llevará a cabo el evento.
     * @param descripcion  La descripción del evento.
     * @param tipo         El tipo de evento.
     * @param rutaImagen   La ruta de la imagen del evento.
     * @param fecha        La fecha del evento.
     * @param direccion    La dirección del evento.
     * @param localidades  La lista de localidades del evento.
     */
    public void gestionarEvento(Unieventos uniEventos, String nombre, String ciudad, String descripcion, TipoEvento tipo, String rutaImagen, LocalDate fecha, String direccion, List<Localidad> localidades) {
        Evento evento = new Evento(nombre, ciudad, descripcion, tipo, fecha, direccion, localidades);
        uniEventos.agregarEvento(evento);
    }
}
