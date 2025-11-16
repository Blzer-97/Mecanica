package grupo2.mecanica_ed_02;

import grupo2.mecanica_ed_02.Service.InventarioService;
import grupo2.mecanica_ed_02.Service.ProductoService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class MECANICA_ED_02 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ProductoService productoService = new ProductoService();
        InventarioService inventarioService = new InventarioService(productoService);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MENÚ INVENTARIO ===");
            System.out.println("1. Registrar nuevo producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Registrar entrada de inventario");
            System.out.println("4. Registrar salida de inventario");
            System.out.println("5. Ver historial de movimientos de un producto");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1":
                        registrarProductoDesdeConsola(scanner, productoService);
                        break;
                    case "2":
                        listarProductos(productoService);
                        break;
                    case "3":
                        registrarEntradaDesdeConsola(scanner, inventarioService);
                        break;
                    case "4":
                        registrarSalidaDesdeConsola(scanner, inventarioService);
                        break;
                    case "5":
                        verHistorialDesdeConsola(scanner, inventarioService);
                        break;
                    case "0":
                        salir = true;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void registrarProductoDesdeConsola(Scanner scanner,
                                                      ProductoService productoService) {
        System.out.println("\n--- Registrar nuevo producto ---");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("SKU (código único): ");
        String sku = scanner.nextLine();

        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();

        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine());

        System.out.print("Precio de costo: ");
        BigDecimal precioCosto = new BigDecimal(scanner.nextLine());

        System.out.print("Precio de venta: ");
        BigDecimal precioVenta = new BigDecimal(scanner.nextLine());

        Producto p = productoService.registrarProducto(
                nombre, sku, categoria, stock, precioCosto, precioVenta
        );

        System.out.println("Producto registrado correctamente:");
        System.out.println(p);
    }

    private static void listarProductos(ProductoService productoService) {
        System.out.println("\n--- Lista de productos activos ---");
        List<Producto> productos = productoService.listarProductos(true);
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    private static void registrarEntradaDesdeConsola(Scanner scanner,
                                                     InventarioService inventarioService) {
        System.out.println("\n--- Registrar entrada de inventario ---");

        System.out.print("SKU del producto: ");
        String sku = scanner.nextLine();

        System.out.print("Cantidad a ingresar: ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        System.out.print("Motivo (ej. «Compra proveedor X»): ");
        String motivo = scanner.nextLine();

        MovimientoInventario mov = inventarioService.registrarEntrada(sku, cantidad, motivo);

        System.out.println("Entrada registrada:");
        System.out.println(mov);
    }

    private static void registrarSalidaDesdeConsola(Scanner scanner,
                                                    InventarioService inventarioService) {
        System.out.println("\n--- Registrar salida de inventario ---");

        System.out.print("SKU del producto: ");
        String sku = scanner.nextLine();

        System.out.print("Cantidad a retirar: ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        System.out.print("Motivo (ej. «Venta boleta 0001»): ");
        String motivo = scanner.nextLine();

        MovimientoInventario mov = inventarioService.registrarSalida(sku, cantidad, motivo);

        System.out.println("Salida registrada:");
        System.out.println(mov);
    }

    private static void verHistorialDesdeConsola(Scanner scanner,InventarioService inventarioService) {
        System.out.println("\n--- Historial de movimientos ---");

        System.out.print("SKU del producto: ");
        String sku = scanner.nextLine();

        List<MovimientoInventario> historial = inventarioService.consultarHistorial(sku);
        if (historial.isEmpty()) {
            System.out.println("No hay movimientos para ese SKU.");
            return;
        }

        for (MovimientoInventario m : historial) {
            System.out.println(m);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("---MECANICA AUTOMOTRIZ---");
        
        GestorDatosJSON gestorDatos = new GestorDatosJSON();
        ConfigService configService = new ConfigService(gestorDatos);
        
        //acá van los demás servicios
        InventarioService inventarioService = new InventarioService(gestorDatos);
        ServicioService servicioService = new ServicioService(gestorDatos);
        CalculadoraVentas calculadoraVentas = new CalculadoraVentas(configService);
        VentaService ventaService = new VentaService(gestorDatos,inventarioService,calculadoraVentas);
        
        //reportes
        GeneradorPDF generadorPDF = new GeneradorPDF();
        ReporteService reporteService = new ReporteService(gestorDatos);
        
        try{
            System.out.println("PRUEBA DE INTEGRACION");
            
            System.out.println("Guardando configuracion");
            Configuracion config = new Configuracion(0.18, "S/");
            configService.guardarConfiguracion(config);
            System.out.println("Configuracion guardada: IGV = " + config.getPorcentajeIGV());
            
            System.out.println("\nRegistrando producto");
            Producto aceite = new Producto(
                "Aceite Sintético 5W-30", // nombre
                "ACE-001", // sku
                "Aceite de motor", // categoria
                50, // stock
                80.00, // precioCosto
                120.00 // precioVenta
            );
            inventarioService.registrarProducto(aceite); //esta sería la funcion para registrar el producto
            System.out.printf("Producto registrado: %s (Stock: %d)%n", aceite.getNombre(), aceite.getStock());
            
            
            System.out.println("\nRegistrando venta");
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
            nuevaVenta.setId(UUID.randomUUID().toString());
            nuevaVenta.setNombreCliente("Cliente Mostrador");
            
            nuevaVenta.setFecha(new Date());
            nuevaVenta.setItems(items);
            
            Venta ventaGuardada = ventaService.registrarVenta(nuevaVenta);
            System.out.printf("Venta registrada ID: %s. Total: S/ %.2f%n", ventaGuardada.getId(), ventaGuardada.getTotal());
            
            Producto aceiteActualizado = inventarioService.findProductoBySku("ACE-001");
            System.out.printf("Verificación D2: Nuevo stock de '%s': %d (Esperado: 48)%n",aceiteActualizado.getNombre(),aceiteActualizado.getStock());
            
            
            System.out.println("\nGenerando reporte de ventas");
            Date inicio = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date fin = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            List<Venta> ventasReporte = reporteService.getVentasPorPeriodo(inicio, fin);
            double totalVendido = reporteService.calcularTotalVendido(ventasReporte);
            double gananciaTotal = reporteService.calcularGananciaTotal(ventasReporte);
            
            System.out.printf("Reporte: %d ventas encontradas hoy.%n", ventasReporte.size());
            System.out.printf("Reporte: Total Vendido: S/ %.2f%n", totalVendido);
            System.out.printf("Reporte: Ganancia Total: S/ %.2f (Esperado: (120-80)*2 = 80.00)%n", gananciaTotal);
            
            
            System.out.println("\nGenerando boleta");
            File boletaPDF = generadorPDF.generarBoleta(ventaGuardada);
            
        }catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    
    private static ServicioService servicioService;
    private static VentaService ventaService;
    private static InventarioService inventarioService;
    private static ConfigService configService;
    private static GestorDatosJSON gestorDatos;

    public static void main(String[] args) {

        // 1) Crear Gestor de persistencia (único)
        gestorDatos = new GestorDatosJSON();

        // 2) Crear servicios que dependen del GestorDatosJSON
        configService = new ConfigService(gestorDatos);
        inventarioService = new InventarioService(gestorDatos);
        servicioService = new ServicioService(gestorDatos);

        // 3) VentaService necesita GestorDatosJSON + ConfigService + InventarioService
        ventaService = new VentaService(gestorDatos, configService, inventarioService);
        
        CalculadoraVentas.setConfigService(configService);

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== MENÚ PRINCIPAL =====");
            System.out.println("1. CRUD Servicios");
            System.out.println("2. Calcular Venta (Subtotal / Total / Margen)");
            System.out.println("3. Registrar Venta");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> menuServicios();
                case 2 -> menuCalculos();
                case 3 -> registrarVenta();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    // ======================================================
    // =============== CRUD SERVICIOS =======================
    // ======================================================
    private static void menuServicios() {
        Scanner sc = new Scanner(System.in);
        int op;

        do {
            System.out.println("\n--- CRUD Servicios ---");
            System.out.println("1. Listar servicios");
            System.out.println("2. Registrar servicio");
            System.out.println("3. Actualizar servicio");
            System.out.println("4. Eliminar servicio");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");
            op = sc.nextInt();

            switch (op) {

                case 1 -> {
                    List<Servicio> lista = servicioService.getServicios();
                    System.out.println("\n--- LISTA DE SERVICIOS ---");
                    if (lista.isEmpty()) System.out.println("(vacío)");
                    else lista.forEach(s -> System.out.println(s.getId() + " - " + s.getNombre() + " - S/ " + s.getPrecio()));
                }

                case 2 -> {
                    sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Precio: ");
                    double precio = sc.nextDouble();

                    Servicio s = new Servicio(0, nombre, precio);
                    servicioService.registrarServicio(s);
                    System.out.println("Servicio registrado.");
                }

                case 3 -> {
                    System.out.print("ID a actualizar: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Nuevo precio: ");
                    double precio = sc.nextDouble();

                    Servicio s = new Servicio(id, nombre, precio);
                    servicioService.actualizarServicio(s);
                    System.out.println("Servicio actualizado.");
                }

                case 4 -> {
                    System.out.print("ID a eliminar: ");
                    int id = sc.nextInt();
                    servicioService.eliminarServicio(id);
                    System.out.println("Servicio eliminado.");
                }
            }

        } while (op != 0);
    }

    // ======================================================
    // =============== CALCULADORA DE VENTAS ================
    // ======================================================
    private static void menuCalculos() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Cálculos de Venta ---");
        System.out.print("Número de ítems: ");
        int cant = sc.nextInt();

        List<ItemVenta> items = new ArrayList<>();

        for (int i = 0; i < cant; i++) {
            System.out.println("\nItem #" + (i + 1));
            System.out.print("Precio venta: ");
            double pv = sc.nextDouble();
            System.out.print("Precio costo: ");
            double pc = sc.nextDouble();
            System.out.print("Cantidad: ");
            int q = sc.nextInt();

            ItemVenta it = new ItemVenta();
            it.setPrecioUnitarioVenta(pv);
            it.setPrecioUnitarioCosto(pc);
            it.setCantidad(q);

            items.add(it);
        }

        double subtotal = CalculadoraVentas.calcularSubtotal(items);
        System.out.println("Subtotal: " + subtotal);

        System.out.print("Descuento: ");
        double desc = sc.nextDouble();

        double total = CalculadoraVentas.calcularTotalConIGVConfig(subtotal, desc);
        System.out.println("Total con IGV: " + total);

        Venta v = new Venta();
        v.setItems(items);

        double margen = CalculadoraVentas.calcularMargenGanancia(v);
        System.out.println("Margen de ganancia: " + (margen * 100) + "%");
    }

    // ======================================================
    // =============== REGISTRO DE VENTAS ===================
    // ======================================================
    private static void registrarVenta() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Registrar Venta ---");

        Venta v = new Venta();
        List<ItemVenta> items = new ArrayList<>();

        System.out.print("Número de ítems: ");
        int cant = sc.nextInt();

        for (int i = 0; i < cant; i++) {
            System.out.println("\nÍtem #" + (i + 1));

            ItemVenta it = new ItemVenta();

            System.out.print("¿Es producto? (1=Sí, 0=No): ");
            boolean esProducto = sc.nextInt() == 1;
            it.setEsProducto(esProducto);

            System.out.print("Ingrese SKU (si es producto) o ID del servicio: ");
            it.setSkuOId(sc.next());

            System.out.print("Cantidad: ");
            it.setCantidad(sc.nextInt());

            System.out.print("Precio unitario de venta: ");
            it.setPrecioUnitarioVenta(sc.nextDouble());

            System.out.print("Precio unitario de costo: ");
            it.setPrecioUnitarioCosto(sc.nextDouble());

            System.out.print("Cantidad: ");
            it.setCantidad(sc.nextInt());

            items.add(it);
        }

        v.setItems(items);

        try {
            ventaService.registrarVenta(v);
            System.out.println("Venta registrada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar venta: " + e.getMessage());
        }
    }
}
