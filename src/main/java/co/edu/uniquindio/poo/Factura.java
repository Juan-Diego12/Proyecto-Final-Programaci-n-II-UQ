package co.edu.uniquindio.poo;

import java.io.Serializable;

/**
 * La clase Factura representa una factura generada por la compra de entradas en el sistema UniEventos.
 */
public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double totalCompra;
    private double totalAntesDescuentos;
    private String nombreEvento;
    private int cantidadBoletas;

    /**
     * Constructor de la clase Factura.
     *
     * @param totalCompra         El total pagado después de aplicar los descuentos.
     * @param totalAntesDescuentos El total antes de aplicar los descuentos.
     * @param nombreEvento        El nombre del evento del cual se realizó la compra.
     * @param cantidadBoletas     La cantidad de boletas compradas.
     */
    public Factura(double totalCompra, double totalAntesDescuentos, String nombreEvento, int cantidadBoletas) {
        this.totalCompra = totalCompra;
        this.totalAntesDescuentos = totalAntesDescuentos;
        this.nombreEvento = nombreEvento;
        this.cantidadBoletas = cantidadBoletas;
    }

    /**
     * Obtiene el total pagado después de aplicar los descuentos.
     *
     * @return El total pagado.
     */
    public double getTotalCompra() {
        return totalCompra;
    }

    /**
     * Obtiene el total antes de aplicar los descuentos.
     *
     * @return El total antes de los descuentos.
     */
    public double getTotalAntesDescuentos() {
        return totalAntesDescuentos;
    }

    /**
     * Obtiene el nombre del evento del cual se realizó la compra.
     *
     * @return El nombre del evento.
     */
    public String getNombreEvento() {
        return nombreEvento;
    }

    /**
     * Obtiene la cantidad de boletas compradas.
     *
     * @return La cantidad de boletas.
     */
    public int getCantidadBoletas() {
        return cantidadBoletas;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "totalCompra=" + totalCompra +
                ", totalAntesDescuentos=" + totalAntesDescuentos +
                ", nombreEvento='" + nombreEvento + '\'' +
                ", cantidadBoletas=" + cantidadBoletas +
                '}';
    }
}
