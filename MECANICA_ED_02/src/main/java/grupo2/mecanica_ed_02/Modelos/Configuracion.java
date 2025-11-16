package grupo2.mecanica_ed_02.Modelos;

import java.io.Serializable;

/**
 * Guarda la configuración global de la aplicación (IGV, Moneda).
 * Se guardará como un único objeto en configuracion.json.
 * (Req. Funcional: Soporte de impuestos configurables)
 */
public class Configuracion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // El IGV/Impuesto se guarda como factor (ej. 0.18 para 18%)
    private double porcentajeIGV; 
    private String simboloMoneda; // Ej. "S/.", "USD", "EUR"

    public Configuracion() {
        // Valores por defecto
        this.porcentajeIGV = 0.18; // 18% por defecto
        this.simboloMoneda = "S/.";
    }

    public Configuracion(double porcentajeIGV, String simboloMoneda) {
        this.porcentajeIGV = porcentajeIGV;
        this.simboloMoneda = simboloMoneda;
    }

    // --- Getters y Setters ---

    public double getPorcentajeIGV() {
        return porcentajeIGV;
    }

    public void setPorcentajeIGV(double porcentajeIGV) {
        this.porcentajeIGV = porcentajeIGV;
    }

    public String getSimboloMoneda() {
        return simboloMoneda;
    }

    public void setSimboloMoneda(String simboloMoneda) {
        this.simboloMoneda = simboloMoneda;
    }
}
