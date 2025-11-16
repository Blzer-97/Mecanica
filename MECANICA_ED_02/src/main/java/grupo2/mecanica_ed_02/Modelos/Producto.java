package grupo2.mecanica_ed_02.Modelos;

import java.math.BigDecimal;
import java.util.UUID;

public class Producto {

    private final String id;
    private String nombre;
    private String sku;
    private String categoria;
    private int stock;
    private BigDecimal precioCosto;
    private BigDecimal precioVenta;
    private boolean activo;

    public Producto(String nombre,String sku,String categoria,int stock,BigDecimal precioCosto,BigDecimal precioVenta) {

        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.sku = sku;
        this.categoria = categoria;
        this.stock = stock;
        this.precioCosto = precioCosto;
        this.precioVenta = precioVenta;
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSku() {
        return sku;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getStock() {
        return stock;
    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void desactivar() {
        this.activo = false;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", sku='" + sku + '\'' +
                ", categoria='" + categoria + '\'' +
                ", stock=" + stock +
                ", precioCosto=" + precioCosto +
                ", precioVenta=" + precioVenta +
                ", activo=" + activo +
                '}';
    }
}
