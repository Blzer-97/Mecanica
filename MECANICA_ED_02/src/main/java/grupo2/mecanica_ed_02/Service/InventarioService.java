package grupo2.mecanica_ed_02.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import grupo2.mecanica_ed_02.Modelos.MovimientoInventario;
import grupo2.mecanica_ed_02.Modelos.Producto;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tarea D2: Lógica de Inventario y Productos.
 * Gestiona el CRUD de productos y el registro de movimientos de stock.
 * ESTA ES LA VERSIÓN MERGEADA (limpia, solo JSON).
 */
public class InventarioService {

    private final GestorDatosJSON gestorDatos;

    public InventarioService(GestorDatosJSON gestorDatos) {
        this.gestorDatos = gestorDatos;
    }

    // --- Tarea 2.1: Lógica de Servicio de Inventario (CRUD) ---

    public List<Producto> getProductos() {
        return gestorDatos.leerProductos(); // Usa el método fachada
    }

    public Producto findProductoBySku(String sku) {
        return getProductos().stream()
                .filter(p -> p.getSku().equalsIgnoreCase(sku))
                .findFirst()
                .orElse(null);
    }

    public Producto registrarProducto(Producto producto) {
        List<Producto> productos = getProductos();
        
        // Validación de SKU duplicado
        if (findProductoBySku(producto.getSku()) != null) {
            throw new RuntimeException("Error: El SKU '" + producto.getSku() + "' ya existe.");
        }
        
        productos.add(producto);
        gestorDatos.guardarProductos(productos);
        return producto;
    }

    public Producto actualizarProducto(Producto productoActualizado) {
        List<Producto> productos = getProductos();
        boolean encontrado = false;

        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getSku().equalsIgnoreCase(productoActualizado.getSku())) {
                productos.set(i, productoActualizado); // Reemplaza el producto
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new RuntimeException("Error: No se pudo actualizar. Producto con SKU '" + productoActualizado.getSku() + "' no encontrado.");
        }
        
        gestorDatos.guardarProductos(productos);
        return productoActualizado;
    }

    public void eliminarProducto(String sku) {
        List<Producto> productos = getProductos();
        boolean eliminado = productos.removeIf(p -> p.getSku().equalsIgnoreCase(sku));
        
        if (!eliminado) {
             throw new RuntimeException("Error: No se pudo eliminar. Producto con SKU '" + sku + "' no encontrado.");
        }
        
        gestorDatos.guardarProductos(productos);
    }

    // --- Tarea 2.2: Lógica de Movimientos de Stock ---
    
    /**
     * Registra un movimiento y actualiza el stock del producto.
     * La cantidad puede ser positiva (ENTRADA) o negativa (SALIDA).
     */
    public void registrarMovimiento(MovimientoInventario mov) {
        Producto producto = findProductoBySku(mov.getSkuProducto());
        if (producto == null) {
            throw new RuntimeException("Error: Producto con SKU '" + mov.getSkuProducto() + "' no existe.");
        }

        int nuevoStock = producto.getStock() + mov.getCantidad(); // cantidad ya es + o -

        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente para " + producto.getNombre() + ". Stock actual: " + producto.getStock() + ", se intentó sacar: " + (-mov.getCantidad()));
        }

        // 1. Actualizar el stock en el producto
        producto.setStock(nuevoStock);
        actualizarProducto(producto); // Llama al método que guarda la lista de productos

        // 2. Guardar el movimiento en el historial
        List<MovimientoInventario> movimientos = gestorDatos.leerMovimientos();
        movimientos.add(mov);
        gestorDatos.guardarMovimientos(movimientos);
    }

    public List<MovimientoInventario> getHistorialPorProducto(String sku) {
        List<MovimientoInventario> todos = gestorDatos.leerMovimientos();
        return todos.stream()
                .filter(m -> m.getSkuProducto().equalsIgnoreCase(sku))
                .collect(Collectors.toList());
    }
}