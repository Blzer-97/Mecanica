package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.Configuracion;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;

/**
 * Tarea 1.3: Lógica del Servicio de Configuración.
 * Este servicio gestiona las operaciones de configuración de la aplicación.
 * Actúa como intermediario entre la lógica de negocio y el GestorDatosJSON.
 */
public class ConfigService {

    private final GestorDatosJSON gestorDatos;
    private Configuracion configuracionCache; // Mantiene una copia en memoria

    /**
     * Constructor del servicio. Recibe una instancia del gestor de datos.
     * @param gestorDatos El gestor de persistencia (GestorDatosJSON).
     */
    public ConfigService(GestorDatosJSON gestorDatos) {
        this.gestorDatos = gestorDatos;
        // Carga la configuración en memoria la primera vez que se inicia el servicio
        this.configuracionCache = gestorDatos.leerConfiguracion();
    }

    /**
     * Obtiene la configuración actual.
     * Lee desde la caché interna para no leer el archivo JSON cada vez.
     * * @return El objeto Configuracion.
     */
    public Configuracion getConfiguracion() {
        // Devuelve la copia en memoria (caché)
        return this.configuracionCache;
    }

    /**
     * Guarda una nueva configuración.
     * La escribe en el archivo JSON y actualiza la caché interna.
     * * @param nuevaConfig La nueva configuración a guardar.
     */
    public void guardarConfiguracion(Configuracion nuevaConfig) {
        gestorDatos.guardarConfiguracion(nuevaConfig);
        // Actualiza la caché interna con los nuevos valores
        this.configuracionCache = nuevaConfig;
        System.out.println("Configuración guardada y caché actualizada.");
    }

    /**
     * Método de acceso directo para obtener el porcentaje de IGV actual.
     * @return El porcentaje de IGV (ej. 0.18).
     */
    public double getPorcentajeIGV() {
        return this.configuracionCache.getPorcentajeIGV();
    }

    /**
     * Método de acceso directo para obtener el símbolo de moneda.
     * @return El símbolo (ej. "S/.").
     */
    public String getSimboloMoneda() {
        return this.configuracionCache.getSimboloMoneda();
    }
}