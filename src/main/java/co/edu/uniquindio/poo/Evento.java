package co.edu.uniquindio.poo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * La clase Evento representa un evento en el sistema UniEventos.
 */
@Getter
@Setter
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String ciudad;
    private String descripcion;
    private TipoEvento tipo;
    private LocalDate fecha;
    private String direccion;
    private List<Localidad> localidades;
    private int entradasVendidas; // Campo para rastrear las entradas vendidas

    /**
     * Constructor de la clase Evento.
     *
     * @param nombre      El nombre del evento.
     * @param ciudad      La ciudad donde se llevará a cabo el evento.
     * @param descripcion La descripción del evento.
     * @param tipo        El tipo de evento.
     * @param fecha       La fecha del evento.
     * @param direccion   La dirección del evento.
     * @param localidades La lista de localidades del evento.
     */
    public Evento(String nombre, String ciudad, String descripcion, TipoEvento tipo, LocalDate fecha, String direccion, List<Localidad> localidades) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.direccion = direccion;
        this.localidades = localidades;
        this.entradasVendidas = 0; // Inicializar en 0
    }

    /**
     * Incrementa el número de entradas vendidas para este evento.
     *
     * @param cantidad La cantidad de entradas vendidas.
     */
    public void venderEntradas(int cantidad) {
        this.entradasVendidas += cantidad;
    }
}
