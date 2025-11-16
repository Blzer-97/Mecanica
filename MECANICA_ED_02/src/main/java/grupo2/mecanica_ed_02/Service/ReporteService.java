package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.Venta;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tarea D4: Lógica de Reportes.
 */
public class ReporteService {

    private final GestorDatosJSON gestorDatos;

    public ReporteService(GestorDatosJSON gestorDatos) {
        this.gestorDatos = gestorDatos;
    }

    /**
     * Filtra las ventas guardadas por un rango de fechas.
     */
    public List<Venta> getVentasPorPeriodo(Date inicio, Date fin) {
        List<Venta> todas = gestorDatos.leerVentas();
        return todas.stream()
                .filter(venta -> !venta.getFecha().before(inicio) && !venta.getFecha().after(fin))
                .collect(Collectors.toList());
    }

    /**
     * Calcula el monto total vendido (Total) de una lista de ventas.
     */
    public double calcularTotalVendido(List<Venta> ventas) {
        return ventas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
    }

    /**
     * Calcula la ganancia total (Margen) de una lista de ventas.
     */
    public double calcularGananciaTotal(List<Venta> ventas) {
        return ventas.stream()
                .mapToDouble(Venta::getMargenGanancia)
                .sum();
    }
}