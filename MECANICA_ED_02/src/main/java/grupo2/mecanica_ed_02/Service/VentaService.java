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
 * Tarea D3: Lógica de Servicio de Venta.
 */
public class VentaService {

    private final GestorDatosJSON gestorDatos;
    private final InventarioService inventarioService;
    private final CalculadoraVentas calculadoraVentas;

    public VentaService(GestorDatosJSON gestorDatos, InventarioService inventarioService, CalculadoraVentas calculadoraVentas) {
        this.gestorDatos = gestorDatos;
        this.inventarioService = inventarioService;
        this.calculadoraVentas = calculadoraVentas;
    }

    /**
     * Registra una venta, calcula totales y descuenta el stock.
     */
    public Venta registrarVenta(Venta venta) {
        // 1. Asignar ID y Fecha si no existen
        if (venta.getId() == null) {
            venta.setId(UUID.randomUUID().toString());
        }
        if (venta.getFecha() == null) {
            venta.setFecha(new Date());
        }

        // 2. Calcular totales (REQ. FUNCIONAL)
        double subtotal = calculadoraVentas.calcularSubtotal(venta.getItems());
        double igv = calculadoraVentas.calcularMontoIGV(subtotal);
        double total = calculadoraVentas.calcularTotal(subtotal, igv, venta.getDescuento());
        double ganancia = calculadoraVentas.calcularMargenGanancia(venta);

        venta.setSubtotal(subtotal);
        venta.setIgv(igv);
        venta.setTotal(total);
        venta.setMargenGanancia(ganancia);

        // 3. Descontar stock (REQ. FUNCIONAL)
        for (ItemVenta item : venta.getItems()) {
            if (item.isEsProducto()) {
                // La cantidad debe ser negativa para una salida
                int cantidadSalida = -item.getCantidad(); 
                
                MovimientoInventario mov = new MovimientoInventario(
                    new Date(),
                    item.getSkuOId(),
                    cantidadSalida, // Ej. -2
                    "Salida-Venta",
                    "Venta ID: " + venta.getId()
                );
                
                // inventarioService se encarga de validar stock y actualizar
                inventarioService.registrarMovimiento(mov);
            }
        }

        // 4. Guardar la Venta
        List<Venta> ventas = gestorDatos.leerVentas();
        ventas.add(venta);
        gestorDatos.guardarVentas(ventas);

        return venta;
    }
}