package grupo2.mecanica_ed_02;

// Renombramos tu MECANICA_ED_02.java a App.java (un nombre más estándar)
// Esta clase contiene SOLO la "PRUEBA DE INTEGRACION" (Tarea D4)

import grupo2.mecanica_ed_02.Modelos.Configuracion;
import grupo2.mecanica_ed_02.Modelos.ItemVenta;
import grupo2.mecanica_ed_02.Modelos.MovimientoInventario;
import grupo2.mecanica_ed_02.Modelos.Producto;
import grupo2.mecanica_ed_02.Modelos.Servicio;
import grupo2.mecanica_ed_02.Modelos.Venta;
import grupo2.mecanica_ed_02.Service.*;
import grupo2.mecanica_ed_02.Util.CalculadoraVentas;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;
import grupo2.mecanica_ed_02.Service.ConfigService;
import grupo2.mecanica_ed_02.Service.ReporteService;
import grupo2.mecanica_ed_02.Util.GeneradorPDF;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.List;

/**
 * Tarea D4: Pruebas de Integración en Consola.
 * Esta es la versión "limpia" de tu MECANICA_ED_02.java.
 * Contiene solo el main() de "PRUEBA DE INTEGRACION".
 */
public class App {

    public static void main(String[] args) {
        System.out.println("--- PRUEBA DE INTEGRACION (BACKEND MECÁNICA) ---");
        
        // 1. D1: Inicializar Persistencia y Configuración
        System.out.println("Paso 1: Inicializando servicios base (D1)...");
        GestorDatosJSON gestorDatos = new GestorDatosJSON();
        ConfigService configService = new ConfigService(gestorDatos);
        
        // Guardar configuración inicial (18% IGV, Soles)
        configService.guardarConfiguracion(new Configuracion(0.18, "S/"));
        System.out.println("Configuración guardada: IGV = " + configService.getPorcentajeIGV());

        // 2. D2, D3, D4: Inicializar el resto de servicios
        System.out.println("Paso 2: Inicializando servicios de negocio (D2, D3, D4)...");
        InventarioService inventarioService = new InventarioService(gestorDatos);
        ServicioService servicioService = new ServicioService(gestorDatos);
        CalculadoraVentas calculadoraVentas = new CalculadoraVentas(configService); // D3
        VentaService ventaService = new VentaService(gestorDatos, inventarioService, calculadoraVentas); // D3
        ReporteService reporteService = new ReporteService(gestorDatos); // D4
        GeneradorPDF generadorPDF = new GeneradorPDF(configService); // D4
        
        try {
            // 3. D2: Registrar un producto
            System.out.println("\nPaso 3 (D2): Registrando producto...");
            Producto aceite = new Producto(
                "Aceite Sintético 5W-30", // nombre
                "ACE-001", // sku
                "Aceite de motor", // categoria
                50, // stock
                80.00, // precioCosto
                120.00 // precioVenta
            );
            // Limpieza: Borrar el producto si ya existe de una prueba anterior
            try { inventarioService.eliminarProducto("ACE-001"); } catch (Exception e) {}
            
            inventarioService.registrarProducto(aceite);
            System.out.printf("Producto registrado: %s (Stock: %d)%n", aceite.getNombre(), aceite.getStock());
            
            // 4. D3: Registrar una venta
            System.out.println("\nPaso 4 (D3): Registrando venta...");
            List<ItemVenta> items = new ArrayList<>();
            ItemVenta itemVendido = new ItemVenta(
                aceite.getSku(),
                true, // esProducto
                aceite.getNombre(),
                2, // cantidad
                aceite.getPrecioVenta(), 
                aceite.getPrecioCosto() 
            );
            items.add(itemVendido);
            
            Venta nuevaVenta = new Venta();
            nuevaVenta.setNombreCliente("Cliente Mostrador");
            nuevaVenta.setItems(items);
            // (El ID, Fecha, y totales se calculan DENTRO de VentaService)
            
            Venta ventaGuardada = ventaService.registrarVenta(nuevaVenta);
            System.out.printf("Venta registrada ID: %s. Total: S/ %.2f%n", ventaGuardada.getId().substring(0,8), ventaGuardada.getTotal());
            
            // 5. D2: Verificar Stock
            System.out.println("\nPaso 5 (D2): Verificando stock post-venta...");
            Producto aceiteActualizado = inventarioService.findProductoBySku("ACE-001");
            System.out.printf("Verificación: Nuevo stock de '%s': %d (Esperado: 48)%n", aceiteActualizado.getNombre(), aceiteActualizado.getStock());
            
            // 6. D4: Generar Reporte
            System.out.println("\nPaso 6 (D4): Generando reporte de ventas...");
            Date inicio = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date fin = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            List<Venta> ventasReporte = reporteService.getVentasPorPeriodo(inicio, fin);
            double totalVendido = reporteService.calcularTotalVendido(ventasReporta);
            double gananciaTotal = reporteService.calcularGananciaTotal(ventasReporte);
            
            System.out.printf("Reporte: %d ventas encontradas hoy.%n", ventasReporte.size());
            System.out.printf("Reporte: Total Vendido: S/ %.2f%n", totalVendido);
            // (120 - 80) * 2 = 80
            System.out.printf("Reporte: Ganancia Total: S/ %.2f (Esperado: 80.00)%n", gananciaTotal);
            
            // 7. D4: Generar PDF
            System.out.println("\nPaso 7 (D4): Generando boleta PDF...");
            File boletaPDF = generadorPDF.generarBoleta(ventaGuardada);
            if(boletaPDF != null) {
                System.out.println("Boleta generada exitosamente.");
            }
            
            System.out.println("\n--- PRUEBA DE INTEGRACION COMPLETADA EXITOSAMENTE ---");
            
        } catch(Exception e) {
            System.err.println("\n--- FALLÓ LA PRUEBA DE INTEGRACION ---");
            e.printStackTrace();
        }
    }
}