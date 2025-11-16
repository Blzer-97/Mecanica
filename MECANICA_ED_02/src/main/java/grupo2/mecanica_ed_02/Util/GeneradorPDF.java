package grupo2.mecanica_ed_02.Util;

import grupo2.mecanica_ed_02.Modelos.Venta;
import java.io.File;
import java.io.IOException;
// Importaciones de PDFBox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;


/**
 * Tarea D4: Generación de Boletas PDF.
 * Esta es una implementación BÁSICA con PDFBox.
 */
public class GeneradorPDF {

    public File generarBoleta(Venta venta) {
        
        // Asegura que la carpeta 'boletas/' exista
        File boletasDir = new File("boletas");
        if (!boletasDir.exists()) {
            boletasDir.mkdirs();
        }
        
        String nombreArchivo = "boletas/boleta-" + venta.getId().substring(0, 8) + ".pdf";
        File archivoPDF = new File(nombreArchivo);

        // Usamos try-with-resources para asegurar que el documento se cierre
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // PDType1Font.HELVETICA obsoleto, usamos Standard14Fonts
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                
                // --- Encabezado ---
                contentStream.beginText();
                contentStream.setFont(fontBold, 18);
                contentStream.newLineAtOffset(50, 750); // Posición (x, y)
                contentStream.showText("BOLETA DE VENTA");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Cliente: " + venta.getNombreCliente());
                contentStream.newLine(); // Baja una línea
                contentStream.showText("Fecha: " + venta.getFecha().toString());
                contentStream.newLine();
                contentStream.showText("ID Venta: " + venta.getId());
                contentStream.endText();

                // --- Detalles (muy simplificado) ---
                contentStream.beginText();
                contentStream.setFont(font, 10);
                contentStream.newLineAtOffset(50, 650);
                contentStream.showText("Descripción | Cantidad | P. Unit | Total Item");
                contentStream.newLine();
                
                // Dibujar una línea
                contentStream.moveTo(50, 645);
                contentStream.lineTo(550, 645);
                contentStream.stroke();
                
                contentStream.newLine();

                // Items
                venta.getItems().forEach(item -> {
                    try {
                        String linea = String.format("%s | %d | S/ %.2f | S/ %.2f",
                                item.getDescripcion().substring(0, Math.min(item.getDescripcion().length(), 20)),
                                item.getCantidad(),
                                item.getPrecioUnitarioVenta(),
                                (item.getCantidad() * item.getPrecioUnitarioVenta())
                        );
                        contentStream.showText(linea);
                        contentStream.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                
                contentStream.endText();

                // --- Totales ---
                contentStream.beginText();
                contentStream.setFont(fontBold, 12);
                contentStream.newLineAtOffset(350, 500); // Mover a la derecha y arriba
                
                contentStream.showText(String.format("Subtotal: S/ %.2f", venta.getSubtotal()));
                contentStream.newLine();
                contentStream.showText(String.format("IGV (%.0f%%): S/ %.2f", (configService.getPorcentajeIGV() * 100), venta.getIgv())); // Necesitaríamos configService aquí
                contentStream.newLine();
                contentStream.showText(String.format("Descuento: S/ %.2f", venta.getDescuento()));
                contentStream.newLine();
                contentStream.showText(String.format("TOTAL: S/ %.2f", venta.getTotal()));
                
                contentStream.endText();
            }

            document.save(archivoPDF);
            System.out.println("Boleta PDF generada en: " + archivoPDF.getAbsolutePath());
            return archivoPDF;

        } catch (IOException e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Para que el PDF pueda mostrar el IGV, necesita acceso a ConfigService.
    // Lo ideal es pasarlo en el constructor, igual que los otros servicios.
    private ConfigService configService;

    public GeneradorPDF(ConfigService configService) {
        this.configService = configService;
    }
}
