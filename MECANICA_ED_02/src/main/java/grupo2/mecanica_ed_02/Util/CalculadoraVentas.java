package grupo2.mecanica_ed_02.Util;

import grupo2.mecanica_ed_02.Modelos.ItemVenta;
import grupo2.mecanica_ed_02.Modelos.Venta;
import grupo2.mecanica_ed_02.Service.ConfigService;
import java.util.List;

/**
 * Tarea D3: Lógica de Cálculo de Ventas.
 * Nota: Esta clase NO es estática. Depende de ConfigService.
 */
public class CalculadoraVentas {

    private final ConfigService configService;

    public CalculadoraVentas(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * Calcula el subtotal (suma de precios) de una lista de items.
     */
    public double calcularSubtotal(List<ItemVenta> items) {
        double subtotal = 0;
        for (ItemVenta item : items) {
            subtotal += (item.getPrecioUnitarioVenta() * item.getCantidad());
        }
        return subtotal;
    }
    
    /**
     * Calcula el IGV basado en el subtotal y la configuración.
     */
    public double calcularMontoIGV(double subtotal) {
        double porcentajeIGV = configService.getPorcentajeIGV();
        return subtotal * porcentajeIGV;
    }

    /**
     * Calcula el total final de la venta.
     */
    public double calcularTotal(double subtotal, double igv, double descuento) {
        return (subtotal + igv) - descuento;
    }

    /**
     * Calcula el margen de ganancia total de la venta.
     */
    public double calcularMargenGanancia(Venta venta) {
        double costoTotal = 0;
        double ventaTotal = 0;
        
        for (ItemVenta item : venta.getItems()) {
            costoTotal += (item.getPrecioUnitarioCosto() * item.getCantidad());
            ventaTotal += (item.getPrecioUnitarioVenta() * item.getCantidad());
        }
        
        // (Venta - Costo) = Ganancia
        return ventaTotal - costoTotal;
    }
}