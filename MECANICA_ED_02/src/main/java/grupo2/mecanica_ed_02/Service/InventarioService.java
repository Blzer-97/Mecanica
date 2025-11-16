package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.MovimientoInventario;
import grupo2.mecanica_ed_02.Modelos.Producto;
import grupo2.mecanica_ed_02.Modelos.TipoMovimiento;
import com.fasterxml.jackson.core.type.TypeReference;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventarioService {

    private final ProductoService productoService;
    private final List<MovimientoInventario> movimientos = new ArrayList<>();

    public InventarioService(ProductoService productoService) {
        this.productoService = productoService;
    }

    // RF002: registrarEntrada
    public MovimientoInventario registrarEntrada(String skuProducto,
                                                 int cantidad,
                                                 String motivo) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }

        Producto producto = productoService.consultarProducto(skuProducto);
        int stockAnterior = producto.getStock();
        int stockNuevo = stockAnterior + cantidad;
        producto.setStock(stockNuevo);

        MovimientoInventario mov = new MovimientoInventario(
                skuProducto,
                TipoMovimiento.ENTRADA,
                cantidad,
                stockAnterior,
                stockNuevo,
                motivo
        );
        movimientos.add(mov);
        return mov;
    }

    // RF002: registrarSalida
    public MovimientoInventario registrarSalida(String skuProducto,
                                                int cantidad,
                                                String motivo) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }

        Producto producto = productoService.consultarProducto(skuProducto);
        int stockAnterior = producto.getStock();

        if (stockAnterior < cantidad) {
            throw new IllegalStateException("Stock insuficiente para el producto «" + skuProducto + "»");
        }

        int stockNuevo = stockAnterior - cantidad;
        producto.setStock(stockNuevo);

        MovimientoInventario mov = new MovimientoInventario(
                skuProducto,
                TipoMovimiento.SALIDA,
                cantidad,
                stockAnterior,
                stockNuevo,
                motivo
        );
        movimientos.add(mov);
        return mov;
    }

    // RF002: consultarHistorial
    public List<MovimientoInventario> consultarHistorial(String skuProducto) {
        List<MovimientoInventario> resultado = new ArrayList<>();
        for (MovimientoInventario mov : movimientos) {
            if (mov.getSkuProducto().equals(skuProducto)) {
                resultado.add(mov);
            }
        }
        resultado.sort(Comparator.comparing(MovimientoInventario::getFechaHora));
        return resultado;
    }
    
    private final GestorDatosJSON gestorDatos;

    public InventarioService(GestorDatosJSON gestorDatos) {
        this.gestorDatos = gestorDatos;
    }

    // -----------------------------
    //    PRODUCTOS
    // -----------------------------

    public List<Producto> getProductos() {
        return gestorDatos.leerColeccion("productos.json",
                new TypeReference<List<Producto>>() {});
    }

    public void guardarProductos(List<Producto> productos) {
        gestorDatos.guardarColeccion("productos.json", productos);
    }

    public Producto buscarPorSKU(String sku) {
        return getProductos().stream()
                .filter(p -> p.getSku().equalsIgnoreCase(sku))
                .findFirst()
                .orElse(null);
    }

    // -----------------------------
    //    MOVIMIENTOS DE INVENTARIO
    // -----------------------------

    public List<MovimientoInventario> getMovimientos() {
        return gestorDatos.leerColeccion("movimientos.json",
                new TypeReference<List<MovimientoInventario>>() {});
    }

    public void registrarMovimiento(MovimientoInventario mov) {

        Producto p = buscarPorSKU(mov.getSkuProducto());
        if (p == null) {
            throw new RuntimeException("Producto con SKU " + mov.getSkuProducto() + " no existe.");
        }

        int nuevoStock = p.getStock() + mov.getCantidad(); // cantidad ya puede ser negativa

        // Validación de stock antes de permitir salidas
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente para producto " +
                    p.getNombre() + " (SKU " + p.getSku() + ")");
        }

        // Actualiza stock
        p.setStock(nuevoStock);

        // Guardar lista de productos actualizada
        List<Producto> productos = getProductos();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getSku().equalsIgnoreCase(p.getSku())) {
                productos.set(i, p);
                break;
            }
        }
        guardarProductos(productos);

        // Guardar movimiento
        List<MovimientoInventario> movimientos = getMovimientos();
        movimientos.add(mov);
        gestorDatos.guardarColeccion("movimientos.json", movimientos);
    }
}