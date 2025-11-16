package grupo2.mecanica_ed_02.Modelos;

import java.io.Serializable;

/**
 * Representa una línea en una Venta (un producto o un servicio).
 */
public class ItemVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String skuOId; // Guarda el SKU (si es Producto) o el ID (si es Servicio)
    private boolean esProducto; // true si es Producto, false si es Servicio
    private String descripcion; // Nombre del producto o servicio
    private int cantidad;
    private double precioUnitarioVenta; // Precio al que se vendió
    private double precioUnitarioCosto; // Precio de costo (para rentabilidad)

    public ItemVenta() {
    }

    public ItemVenta(String skuOId, boolean esProducto, String descripcion, int cantidad, double precioUnitarioVenta, double precioUnitarioCosto) {
        this.skuOId = skuOId;
        this.esProducto = esProducto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitarioVenta = precioUnitarioVenta;
        this.precioUnitarioCosto = precioUnitarioCosto;
    }

    // --- Getters y Setters ---

    public String getSkuOId() {
        return skuOId;
    }

    public void setSkuOId(String skuOId) {
        this.skuOId = skuOId;
    }

    public boolean isEsProducto() {
        return esProducto;
    }

    public void setEsProducto(boolean esProducto) {
        this.esProducto = esProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    public void setPrecioUnitarioVenta(double precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    public double getPrecioUnitarioCosto() {
        return precioUnitarioCosto;
    }

    public void setPrecioUnitarioCosto(double precioUnitarioCosto) {
        this.precioUnitarioCosto = precioUnitarioCosto;
    }
}