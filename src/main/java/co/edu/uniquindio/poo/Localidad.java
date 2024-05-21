package co.edu.uniquindio.poo;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * La clase Localidad representa una localidad dentro de un evento en el sistema UniEventos.
 */
@Getter
@Setter
public class Localidad implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private double precio;
    private int capacidadMaxima;

    /**
     * Constructor de la clase Localidad.
     *
     * @param nombre         El nombre de la localidad.
     * @param precio         El precio de una entrada en esta localidad.
     * @param capacidadMaxima La capacidad máxima de la localidad.
     */
    public Localidad(String nombre, double precio, int capacidadMaxima) {
        this.nombre = nombre;
        this.precio = precio;
        this.capacidadMaxima = capacidadMaxima;
    }
}
