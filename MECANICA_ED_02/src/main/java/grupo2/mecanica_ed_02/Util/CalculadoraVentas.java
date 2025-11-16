package grupo2.mecanica_ed_02.Util;

import grupo2.mecanica_ed_02.Modelos.ItemVenta;
import grupo2.mecanica_ed_02.Modelos.Venta;
import grupo2.mecanica_ed_02.Service.ConfigService;
import java.util.List;

/**
 * Lógica de cálculo para ventas.
 * Tarea 3.2 — Desarrollador 3
 */
public class CalculadoraVentas {

    private static ConfigService config;

    // Permite inyectar el ConfigService una vez al inicio
    public static void setConfigService(ConfigService cfg) {
        config = cfg;
    }

    /**
     * Calcula el subtotal: suma de precioUnitarioVenta * cantidad
     */
    public static double calcularSubtotal(List<ItemVenta> items) {
        if (items == null || items.isEmpty()) return 0;

        double subtotal = 0.0;

        for (ItemVenta item : items) {
            subtotal += item.getPrecioUnitarioVenta() * item.getCantidad();
        }

        return subtotal;
    }

    /**
     * Calcula el total aplicando descuento e IGV.
     * IGV se espera como valor decimal (ej: 0.18 = 18%)
     */
    public static double calcularTotal(double subtotal, double descuento, double igvPorcentaje) {

        if (subtotal < 0) subtotal = 0;
        if (descuento < 0) descuento = 0;
        if (igvPorcentaje < 0) igvPorcentaje = 0;

        double base = subtotal - descuento;
        if (base < 0) base = 0;

        return base + (base * igvPorcentaje);
    }

    /**
     * Calcula margen de ganancia: (ingreso - costo) / costo
     */
    public static double calcularMargenGanancia(Venta v) {
        if (v == null || v.getItems() == null) return 0.0;

        double costoTotal = 0.0;
        double ingresoTotal = 0.0;

        for (ItemVenta item : v.getItems()) {
            costoTotal += item.getPrecioUnitarioCosto() * item.getCantidad();
            ingresoTotal += item.getPrecioUnitarioVenta() * item.getCantidad();
        }

        if (costoTotal == 0) return 0.0;

        return (ingresoTotal - costoTotal) / costoTotal;
    }

    /**
     * Atajo: calcula total usando IGV desde ConfigService
     */
    public static double calcularTotalConIGVConfig(double subtotal, double descuento) {
        if (config == null)
            throw new IllegalStateException("CalculadoraVentas no tiene ConfigService asignado.");

        double igv = config.getPorcentajeIGV(); // CORREGIDO
        return calcularTotal(subtotal, descuento, igv);
    }
}