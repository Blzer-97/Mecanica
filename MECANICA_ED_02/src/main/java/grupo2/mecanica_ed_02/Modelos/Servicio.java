package grupo2.mecanica_ed_02.Modelos;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un servicio ofrecido por la mecánica (ej. "Cambio de Aceite").
 * (Req. Funcional: Gestión de servicios)
 */
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int id; // ID autoincremental o gestionado por el servicio
    private String nombre;
    private double precio;

    public Servicio() {
    }

    public Servicio(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // --- Getters y Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // --- hashCode y equals ---

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Servicio other = (Servicio) obj;
        return this.id == other.id;
    }
}