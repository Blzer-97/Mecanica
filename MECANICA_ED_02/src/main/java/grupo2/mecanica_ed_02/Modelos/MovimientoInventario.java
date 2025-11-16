package grupo2.mecanica_ed_02.Modelos;

import java.time.LocalDateTime;
import java.util.UUID;

public class MovimientoInventario {

    private final String id;
    private final String skuProducto;
    private final TipoMovimiento tipo;
    private final int cantidad;
    private final int stockAnterior;
    private final int stockNuevo;
    private final LocalDateTime fechaHora;
    private final String motivo;

    public MovimientoInventario(String skuProducto,TipoMovimiento tipo,int cantidad,int stockAnterior,int stockNuevo,String motivo) {

        this.id = UUID.randomUUID().toString();
        this.skuProducto = skuProducto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.fechaHora = LocalDateTime.now();
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public String getSkuProducto() {
        return skuProducto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getStockAnterior() {
        return stockAnterior;
    }

    public int getStockNuevo() {
        return stockNuevo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        return "MovimientoInventario{" +
                "id='" + id + '\'' +
                ", skuProducto='" + skuProducto + '\'' +
                ", tipo=" + tipo +
                ", cantidad=" + cantidad +
                ", stockAnterior=" + stockAnterior +
                ", stockNuevo=" + stockNuevo +
                ", fechaHora=" + fechaHora +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}