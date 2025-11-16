package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.Servicio;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;
import java.util.List;

/**
 * Tarea D3: CRUD de Servicios.
 */
public class ServicioService {

    private final GestorDatosJSON gestorDatos;

    public ServicioService(GestorDatosJSON gestorDatos) {
        this.gestorDatos = gestorDatos;
    }

    public List<Servicio> getServicios() {
        return gestorDatos.leerServicios();
    }

    public Servicio registrarServicio(Servicio servicio) {
        List<Servicio> servicios = getServicios();
        
        // Simular autoincremento
        int maxId = servicios.stream().mapToInt(Servicio::getId).max().orElse(0);
        servicio.setId(maxId + 1);
        
        servicios.add(servicio);
        gestorDatos.guardarServicios(servicios);
        return servicio;
    }

    public Servicio actualizarServicio(Servicio servicioActualizado) {
        List<Servicio> servicios = getServicios();
        for (int i = 0; i < servicios.size(); i++) {
            if (servicios.get(i).getId() == servicioActualizado.getId()) {
                servicios.set(i, servicioActualizado);
                gestorDatos.guardarServicios(servicios);
                return servicioActualizado;
            }
        }
        throw new RuntimeException("Servicio con ID " + servicioActualizado.getId() + " no encontrado.");
    }

    public void eliminarServicio(int id) {
        List<Servicio> servicios = getServicios();
        servicios.removeIf(s -> s.getId() == id);
        gestorDatos.guardarServicios(servicios);
    }
}