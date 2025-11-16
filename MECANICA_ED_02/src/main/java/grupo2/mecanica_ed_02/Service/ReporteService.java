package grupo2.mecanica_ed_02.Service;

import grupo2.mecanica_ed_02.Modelos.ItemVenta;
import grupo2.mecanica_ed_02.Modelos.Venta;
import grupo2.mecanica_ed_02.Persistence.GestorDatosJSON;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Luis
 */
public class ReporteService {
    private final GestorDatosJSON gestorDatos;
    
    public ReporteService(GestorDatosJSON gestorDatos){
        this.gestorDatos = gestorDatos;
    }
    
    public List<Venta> getVentasPorPeriodo(Date inicio, Date fin){
        System.out.println("Ventas entre: " + inicio + " y " + fin);
        
        LocalDate fechaInicio = inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaFin = fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // (Asumimos que D3 implementó leerVentas() en GestorDatosJSON)
        List<Venta> todasLasVentas = gestorDatos.leerVentas();
        
        return todasLasVentas.stream()
                .filter(venta ->{
                    LocalDate fechaVenta = venta.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return !fechaVenta.isBefore(fechaInicio) && !fechaVenta.isAfter(fechaFin);
                })
                .collect(Collectors.toList());
    }
    
    public double calcularTotalVendido(List<Venta> ventas){
        if(ventas == null){
            return 0.0;
        }
        
        return ventas.stream().mapToDouble(Venta::getTotal).sum();
    }
    
    public double calcularGananciaTotal(List<Venta> ventas){
        if (ventas == null){
            return 0.0;
        }
        
        try{
            return ventas.stream()
                    .flatMap(venta -> venta.getItems().stream())
                    .filter(item -> Objects.nonNull(item.getPrecioUnitarioCosto()))
                    .mapToDouble(item ->{
                        double precioVenta = item.getPrecioUnitarioVenta();
                        double precioCosto = item.getPrecioUnitarioCosto();
                        return (precioVenta - precioCosto)*item.getCantidad();
                    }).sum();
        }catch(Exception e){
            System.out.println("No se pudo calcular la Ganancia Total.");
            return 0.0;
        }
    }
}
