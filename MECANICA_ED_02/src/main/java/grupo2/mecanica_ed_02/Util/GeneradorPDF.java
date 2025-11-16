package grupo2.mecanica_ed_02.Util;

import grupo2.mecanica_ed_02.Modelos.ItemVenta;
import grupo2.mecanica_ed_02.Modelos.Venta;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GeneradorPDF {
    public GeneradorPDF(){
        
    }
    
    public File generarBoleta(Venta v){
        File boletaDir = new File("boletas");
        
        if(!boletaDir.exists()){
            boletaDir.mkdirs();
        }
        
        String path = "boletas/venta-" + v.getId() + ".pdf";
        File file = new File(path);
        
        try(PDDocument document = new PDDocument()){
            PDPage page = new PDPage();
            document.addPage(page);
            
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fontPlain = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            
            try(PDPageContentStream contentStream = new PDPageContentStream(document, page)){
                contentStream.beginText();
                contentStream.setFont(fontBold, 18);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("BOLETA DE VENTA");
                contentStream.endText();
                
                contentStream.beginText();
                contentStream.setFont(fontPlain, 12);
                contentStream.newLineAtOffset(50, 720);
                        
                contentStream.showText("Cliente: " + v.getNombreCliente());
                contentStream.newLineAtOffset(0, -15);
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaFormateada = formatter.format(v.getFecha().toInstant().atZone(ZoneId.systemDefault()));
                contentStream.showText("Fecha: " + fechaFormateada);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Venta ID: " + v.getId());
                contentStream.endText();
                
                int yStart = 660;
                contentStream.beginText();
                contentStream.setFont(fontBold, 10);
                contentStream.newLineAtOffset(50, yStart);
                contentStream.showText("Cant.");
                contentStream.newLineAtOffset(50, 0);
                contentStream.showText("Descripcion");
                contentStream.newLineAtOffset(250, 0);
                contentStream.showText("P. Unit.");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Subtotal");
                contentStream.endText();
                
                contentStream.moveTo(50, yStart - 5);
                contentStream.lineTo(550, yStart - 5);
                contentStream.stroke();
                
                contentStream.setFont(fontPlain, 10);
                int yPos = yStart - 20;
                for (ItemVenta item : v.getItems()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPos);

                    contentStream.showText(String.valueOf(item.getCantidad()));
                    contentStream.newLineAtOffset(50, 0);

                    contentStream.showText(item.getDescripcion());
                    contentStream.newLineAtOffset(250, 0);

                    contentStream.showText(String.format("S/ %.2f", item.getPrecioUnitarioVenta()));
                    contentStream.newLineAtOffset(100, 0);

                    double subtotalItem = item.getPrecioUnitarioVenta() * item.getCantidad();
                    contentStream.showText(String.format("S/ %.2f", subtotalItem));
                    contentStream.endText();
                    yPos -= 15;
                }
                
                contentStream.moveTo(50, yPos + 5);
                contentStream.lineTo(550, yPos + 5);
                contentStream.stroke();
                
                double porcentajeIgvCalc = (v.getSubtotal() != 0) ? (v.getIgv() / v.getSubtotal()) * 100 : 0;
                
                contentStream.beginText();
                contentStream.setFont(fontBold, 14);
                contentStream.newLineAtOffset(350, yPos - 20);
                contentStream.showText(String.format("SUBTOTAL: S/ %.2f", v.getSubtotal()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.format("IGV (%.0f%%): S/ %.2f", porcentajeIgvCalc, v.getIgv()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.format("TOTAL: S/ %.2f", v.getTotal()));
                contentStream.endText();
            }
            document.save(file);
            System.out.printf("[GeneradorPDF] Boleta generada exitosamente en: %s%n", file.getAbsolutePath());
        }catch (IOException e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
        }
        return file;
    }
}
