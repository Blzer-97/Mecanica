package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.Producto;

import java.math.BigDecimal;
import java.util.*;

public class ProductoService {

    // Mapa en memoria: clave = sku, valor = producto
    private final Map<String, Producto> productosPorSku = new HashMap<>();
    
    public ProductoService() {
        // por ahora no cargamos nada, todo es en memoria
    }

    // RF001: registrarProducto
    public Producto registrarProducto(String nombre,
                                      String sku,
                                      String categoria,
                                      int stockInicial,
                                      BigDecimal precioCosto,
                                      BigDecimal precioVenta) {

        if (productosPorSku.containsKey(sku)) {
            throw new IllegalArgumentException("El SKU «" + sku + "» ya existe");
        }
        if (stockInicial < 0) {
            throw new IllegalArgumentException("El stock inicial no puede ser negativo");
        }
        if (precioCosto.compareTo(BigDecimal.ZERO) < 0 ||
            precioVenta.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Los precios deben ser positivos");
        }

        Producto p = new Producto(nombre, sku, categoria,
                                  stockInicial, precioCosto, precioVenta);

        productosPorSku.put(sku, p);
        return p;
    }

    // RF001: modificarProducto
    public Producto modificarProducto(String sku,
                                      String nuevoNombre,
                                      String nuevaCategoria,
                                      BigDecimal nuevoPrecioCosto,
                                      BigDecimal nuevoPrecioVenta) {

        Producto p = obtenerProductoActivo(sku);

        if (nuevoNombre != null) p.setNombre(nuevoNombre);
        if (nuevaCategoria != null) p.setCategoria(nuevaCategoria);
        if (nuevoPrecioCosto != null) p.setPrecioCosto(nuevoPrecioCosto);
        if (nuevoPrecioVenta != null) p.setPrecioVenta(nuevoPrecioVenta);

        return p;
    }

    // RF001: eliminarProducto (lógico)
    public void eliminarProducto(String sku) {
        Producto p = obtenerProductoActivo(sku);
        p.desactivar();
    }

    // RF001: consultarProducto
    public Producto consultarProducto(String sku) {
        Producto p = productosPorSku.get(sku);
        if (p == null) {
            throw new NoSuchElementException("No existe producto con SKU «" + sku + "»");
        }
        return p;
    }

    // RF001: consultarProductos con filtro por activos
    public List<Producto> listarProductos(boolean soloActivos) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productosPorSku.values()) {
            if (!soloActivos || p.isActivo()) {
                resultado.add(p);
            }
        }
        resultado.sort(Comparator.comparing(Producto::getNombre));
        return resultado;
    }

    // helper interno
    private Producto obtenerProductoActivo(String sku) {
        Producto p = productosPorSku.get(sku);
        if (p == null || !p.isActivo()) {
            throw new NoSuchElementException("No existe producto activo con SKU «" + sku + "»");
        }
        return p;
    }
}

