package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.Servicio;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarea 3.1 — CRUD de Servicios
 */
public class ServicioService {

    private final GestorDatosJSON gestorDatos;

    public ServicioService(GestorDatosJSON gestorDatos) {
        this.gestorDatos = gestorDatos;
    }

    private List<Servicio> safeList(List<Servicio> lista) {
        return (lista != null) ? lista : new ArrayList<>();
    }

    // Obtener lista completa
    public List<Servicio> getServicios() {
        return safeList(gestorDatos.leerServicios());
    }

    // Registrar nuevo servicio
    public void registrarServicio(Servicio s) {
        List<Servicio> lista = safeList(gestorDatos.leerServicios());

        // Generar ID si no tiene
        if (s.getId() <= 0) {
            int max = lista.stream()
                    .mapToInt(Servicio::getId)
                    .max()
                    .orElse(0);
            s.setId(max + 1);
        }

        lista.add(s);
        gestorDatos.guardarServicios(lista);
    }

    // Actualizar servicio existente
    public void actualizarServicio(Servicio s) {
        List<Servicio> lista = safeList(gestorDatos.leerServicios());

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == s.getId()) {
                lista.set(i, s);
                gestorDatos.guardarServicios(lista);
                return;
            }
        }
    }

    // Eliminar servicio por ID
    public void eliminarServicio(int id) {
        List<Servicio> lista = safeList(gestorDatos.leerServicios());
        lista.removeIf(s -> s.getId() == id);
        gestorDatos.guardarServicios(lista);
    }

    // Buscar por nombre
    public Servicio buscarPorNombre(String nombre) {
        if (nombre == null) return null;

        return getServicios()
                .stream()
                .filter(s -> s.getNombre() != null &&
                             s.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst()
                .orElse(null);
    }
}