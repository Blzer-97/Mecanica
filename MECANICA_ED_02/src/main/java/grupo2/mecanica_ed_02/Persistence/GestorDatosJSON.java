package grupo2.mecanica_ed_02.Persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import grupo2.mecanica_ed_02.Modelos.Configuracion;
import grupo2.mecanica_ed_02.Modelos.Producto;
import grupo2.mecanica_ed_02.Modelos.Servicio;
import grupo2.mecanica_ed_02.Modelos.Venta;
import grupo2.mecanica_ed_02.Modelos.MovimientoInventario;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarea 1.2: Capa de Persistencia (DAO).
 * Gestiona la lectura y escritura de todos los archivos JSON en la carpeta 'data/'.
 * Es la única clase en todo el sistema que sabe cómo interactuar con los archivos.
 */
public class GestorDatosJSON {

    // Define la carpeta donde se guardarán los archivos JSON.
    // "data/" significa que buscará la carpeta en la raíz del proyecto.
    private static final String DATA_DIRECTORY = "data/";
    
    // Nombres de los archivos
    private static final String ARCHIVO_PRODUCTOS = "productos.json";
    private static final String ARCHIVO_SERVICIOS = "servicios.json";
    private static final String ARCHIVO_VENTAS = "ventas.json";
    private static final String ARCHIVO_MOVIMIENTOS = "movimientos.json";
    private static final String ARCHIVO_CONFIGURACION = "configuracion.json";

    private final ObjectMapper objectMapper; // El "cerebro" de Jackson

    public GestorDatosJSON() {
        // Configura Jackson para que escriba los JSON de forma legible ("Pretty Print")
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Asegura que la carpeta 'data/' exista al iniciar
        crearDirectorioDataSiNoExiste();
    }
    
    /**
     * Asegura que la carpeta 'data/' exista. Si no, la crea.
     */
    private void crearDirectorioDataSiNoExiste() {
        Path path = Paths.get(DATA_DIRECTORY);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Directorio 'data/' creado en: " + path.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error al crear directorio 'data/': " + e.getMessage());
                // En una app real, podríamos lanzar una RuntimeException aquí
            }
        }
    }

    /**
     * Construye la ruta completa a un archivo dentro del directorio 'data/'.
     * @param nombreArchivo El nombre del archivo (ej. "productos.json")
     * @return Objeto File apuntando al archivo.
     */
    private File getArchivo(String nombreArchivo) {
        return new File(DATA_DIRECTORY + nombreArchivo);
    }

    // --- Métodos Genéricos para Colecciones (Listas) ---

    /**
     * Lee una colección (lista) de objetos desde un archivo JSON.
     * Si el archivo no existe, devuelve una lista vacía.
     *
     * @param <T> El tipo de objeto (ej. Producto, Servicio)
     * @param nombreArchivo El nombre del archivo (ej. "productos.json")
     * @param typeReference El tipo de la lista (ej. new TypeReference<List<Producto>>(){})
     * @return La lista de objetos leída.
     */
    public <T> List<T> leerColeccion(String nombreArchivo, TypeReference<List<T>> typeReference) {
        File archivo = getArchivo(nombreArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
            // Si no existe o está vacío, devuelve una lista nueva
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(archivo, typeReference);
        } catch (IOException e) {
            System.err.println("Error al leer colección " + nombreArchivo + ": " + e.getMessage());
            // En caso de error (ej. JSON corrupto), devuelve una lista vacía
            // para evitar que la aplicación se caiga.
            return new ArrayList<>();
        }
    }

    /**
     * Guarda (sobrescribe) una colección (lista) de objetos en un archivo JSON.
     *
     * @param <T> El tipo de objeto
     * @param nombreArchivo El nombre del archivo
     * @param datos La lista completa de datos a guardar
     */
    public <T> void guardarColeccion(String nombreArchivo, List<T> datos) {
        File archivo = getArchivo(nombreArchivo);
        try {
            objectMapper.writeValue(archivo, datos);
        } catch (IOException e) {
            System.err.println("Error al guardar colección " + nombreArchivo + ": " + e.getMessage());
        }
    }

    // --- Métodos Específicos para Configuración (Objeto Único) ---
    // (Configuracion.json no es una lista, es un solo objeto)

    /**
     * Lee el archivo de configuración.
     * Si no existe, crea uno nuevo con valores por defecto.
     *
     * @return El objeto Configuracion.
     */
    public Configuracion leerConfiguracion() {
        File archivo = getArchivo(ARCHIVO_CONFIGURACION);
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("configuracion.json no encontrado, creando uno por defecto.");
            Configuracion configDefecto = new Configuracion(); // Usa el constructor por defecto
            guardarConfiguracion(configDefecto); // Guarda el nuevo archivo
            return configDefecto;
        }

        try {
            return objectMapper.readValue(archivo, Configuracion.class);
        } catch (IOException e) {
            System.err.println("Error al leer " + ARCHIVO_CONFIGURACION + ": " + e.getMessage());
            return new Configuracion(); 
        }
    }

    public void guardarConfiguracion(Configuracion config) {
        File archivo = getArchivo(ARCHIVO_CONFIGURACION);
        try {
            objectMapper.writeValue(archivo, config);
        } catch (IOException e) {
            System.err.println("Error al guardar " + ARCHIVO_CONFIGURACION + ": " + e.getMessage());
        }
    }
    
  
    public List<Producto> leerProductos() {
        return leerColeccion(ARCHIVO_PRODUCTOS, new TypeReference<List<Producto>>() {});
    }
    public void guardarProductos(List<Producto> productos) {
      guardarColeccion(ARCHIVO_PRODUCTOS, productos);
    }
    

    public List<Producto> leerProductos() {
        return leerColeccion(ARCHIVO_PRODUCTOS, new TypeReference<List<Producto>>() {});
    }
    public void guardarProductos(List<Producto> productos) {
        guardarColeccion(ARCHIVO_PRODUCTOS, productos);
    }
    public List<MovimientoInventario> leerMovimientos() {
        return leerColeccion(ARCHIVO_MOVIMIENTOS, new TypeReference<List<MovimientoInventario>>() {});
    }
    public void guardarMovimientos(List<MovimientoInventario> movimientos) {
        guardarColeccion(ARCHIVO_MOVIMIENTOS, movimientos);
    }
    
    public List<Servicio> leerServicios() {
        return leerColeccion(ARCHIVO_SERVICIOS, new TypeReference<List<Servicio>>() {});
    }
    public void guardarServicios(List<Servicio> servicios) {
        guardarColeccion(ARCHIVO_SERVICIOS, servicios);
    }
    

    public List<Venta> leerVentas() {
        return leerColeccion(ARCHIVO_VENTAS, new TypeReference<List<Venta>>() {});
    }
    
    public void guardarVentas(List<Venta> ventas) {
        guardarColeccion(ARCHIVO_VENTAS, ventas);
    }
        
}