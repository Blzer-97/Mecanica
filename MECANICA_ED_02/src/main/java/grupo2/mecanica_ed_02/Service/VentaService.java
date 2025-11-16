package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.ItemVenta;
import grupo2.mecanica_ed_02.Modelos.MovimientoInventario;
import grupo2.mecanica_ed_02.Modelos.Venta;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;
import grupo2.mecanica_ed_02.Util.CalculadoraVentas;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Tarea 3.3 – Lógica de Servicio de Venta
 */
public class VentaService {

    private final GestorDatosJSON gestorDatos;
    private final ConfigService configService;
    private final InventarioService inventarioService;

    public VentaService(GestorDatosJSON gestorDatos, ConfigService configService, InventarioService inventarioService) {
        this.gestorDatos = gestorDatos;
        this.configService = configService;
        this.inventarioService = inventarioService;
    }

    /**
     * Registra una venta, actualiza stock y calcula totales.
     */
    public Venta registrarVenta(Venta v) {

        // Asegurar ID
        if (v.getId() == null || v.getId().isEmpty()) {
            v.setId("V-" + UUID.randomUUID().toString().substring(0, 8));
        }

        // Asegurar fecha
        if (v.getFecha() == null) {
            v.setFecha(new Date());
        }

        // ======== Calcular totales ==========
        double subtotal = CalculadoraVentas.calcularSubtotal(v.getItems());
        double igvPorcentaje = configService.getPorcentajeIGV();
        double total = CalculadoraVentas.calcularTotal(subtotal, v.getDescuento(), igvPorcentaje);
        double margen = CalculadoraVentas.calcularMargenGanancia(v);

        v.setSubtotal(subtotal);
        v.setIgv(subtotal * igvPorcentaje);
        v.setTotal(total);
        v.setMargenGanancia(margen);

        // ======== Guardar venta ==========

        List<Venta> ventas = gestorDatos.leerVentas();
        ventas.add(v);
        gestorDatos.guardarVentas(ventas);

        // ======== Registrar movimientos de inventario ==========
        for (ItemVenta item : v.getItems()) {

            // Si NO es producto (== es servicio) => no afecta stock, saltar
            if (!item.isEsProducto()) {
                continue;
            }

            MovimientoInventario mov = new MovimientoInventario(
                    new Date(),
                    item.getSkuOId(),
                    - item.getCantidad(), // salida
                    "Salida-Venta",
                    "Venta ID: " + v.getId()
            );

            inventarioService.registrarMovimiento(mov);
        }

        return v;
    }
}
