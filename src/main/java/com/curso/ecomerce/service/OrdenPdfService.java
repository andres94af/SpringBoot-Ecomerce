package com.curso.ecomerce.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.stereotype.Service;
import com.curso.ecomerce.model.DetalleOrden;
import com.curso.ecomerce.model.Orden;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class OrdenPdfService {
	
	public void generarOrdenPDF(Orden orden, List<DetalleOrden> detalles) throws FileNotFoundException, DocumentException {
		Document documento;
		FileOutputStream fileOutputStream;
		Paragraph saltoLinea = new Paragraph();
		saltoLinea.add(new Phrase(Chunk.NEWLINE));
		// FUENTE Y TAMANO DE LAS LETRAS
		Font fuenteTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 17);
		Font fuenteParrafo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
		Font fuenteParrafoNegrita = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
		// CREA DOCUMENTO
		documento = new Document(PageSize.A4, 35, 30, 50, 50);
		// ARCHIVO PDF
//		String rutaSistema = System.getProperty("user.home");
//		String nombreArchivo = "/Desktop/Ordenes/Orden 01-";
		String rutaSistema = System.getProperty("user.dir");
		String nombreArchivo = "/OrdenesPDF/Orden 001-";
		String rutaDeGuardado = rutaSistema + nombreArchivo + orden.getNumero() + ".pdf";
		fileOutputStream = new FileOutputStream(rutaDeGuardado);
		// OBTENER INSTANCIA DE PDF WRITER
		PdfWriter.getInstance(documento, fileOutputStream);
		// ABRIR DOCUMENTO
		documento.open();
		// AGREGAR TITULO
		PdfPTable tablaTitulo = new PdfPTable(1);
		PdfPCell celda = new PdfPCell(new Phrase("ORDEN DE COMPRA 001-"+orden.getNumero(), fuenteTitulo));
		celda.setColspan(4);
		celda.setBorderColor(BaseColor.WHITE); 	
		celda.setHorizontalAlignment(Element.ALIGN_CENTER);
		tablaTitulo.addCell(celda);
		documento.add(tablaTitulo);
		documento.add(saltoLinea);
		documento.add(saltoLinea);
		// AGREGAR INFO USUARIO Y FECHA
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
		PdfPTable tablaInfo = new PdfPTable(1);
		PdfPCell cell = new PdfPCell(new Phrase("Fecha: " + formatter.format(orden.getFechaCreacion()), fuenteParrafo));
		cell.setBorderColor(BaseColor.WHITE);
		tablaInfo.addCell(cell);
		PdfPCell cell2 = new PdfPCell(new Phrase("Nombre: " + orden.getUsuario().getNombre() + " " + orden.getUsuario().getApellido(), fuenteParrafo));
		cell2.setBorderColor(BaseColor.WHITE);
		tablaInfo.addCell(cell2);
		PdfPCell cell3 = new PdfPCell(new Phrase("Direccion: " + orden.getUsuario().getDireccion(), fuenteParrafo));
		cell3.setBorderColor(BaseColor.WHITE);
		tablaInfo.addCell(cell3);
		PdfPCell cell4 = new PdfPCell(new Phrase("Mail: " + orden.getUsuario().getEmail(), fuenteParrafo));
		cell4.setBorderColor(BaseColor.WHITE);
		tablaInfo.addCell(cell4);
		tablaInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		documento.add(tablaInfo);
		documento.add(saltoLinea);
		documento.add(saltoLinea);
		// AGREGAR TABLA DE DETALLES
		PdfPTable tablaDetalles = new PdfPTable(4);
		PdfPCell celdahead1 = new PdfPCell(new Phrase("Cantidad", fuenteParrafoNegrita));
		celdahead1.setBorderWidth(0);
		tablaDetalles.addCell(celdahead1);
		PdfPCell celdahead2 = new PdfPCell(new Phrase("Producto", fuenteParrafoNegrita));
		celdahead2.setBorderWidth(0);
		tablaDetalles.addCell(celdahead2);
		PdfPCell celdahead3 = new PdfPCell(new Phrase("Precio", fuenteParrafoNegrita));
		celdahead3.setBorderWidth(0);
		celdahead3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tablaDetalles.addCell(celdahead3);
		PdfPCell celdahead4 = new PdfPCell(new Phrase("Subtotal", fuenteParrafoNegrita));
		celdahead4.setBorderWidth(0);
		celdahead4.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tablaDetalles.addCell(celdahead4);
		for(DetalleOrden d: detalles) {
			PdfPCell cell5 = new PdfPCell(new Phrase(String.valueOf(d.getCantidad()),fuenteParrafo));
			cell5.setBorderWidth(0);
			cell5.setPaddingLeft(5);
			tablaDetalles.addCell(cell5);
			PdfPCell cell6 = new PdfPCell(new Phrase(d.getProducto().getNombre(),fuenteParrafo));
			cell6.setBorderWidth(0);
			tablaDetalles.addCell(cell6);
			PdfPCell cell7 = new PdfPCell(new Phrase("€ " + String.valueOf(d.getPrecio()),fuenteParrafo));
			cell7.setBorderWidth(0);
			cell7.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaDetalles.addCell(cell7);
			PdfPCell cell8 = new PdfPCell(new Phrase("€ " + String.valueOf(d.getTotal()),fuenteParrafo));
			cell8.setBorderWidth(0);
			cell8.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaDetalles.addCell(cell8);
		}
		documento.add(tablaDetalles);
		documento.add(saltoLinea);
		//AGREGA TABLA DE TOTAL
		PdfPTable tablaTotal = new PdfPTable(1);
		PdfPCell celdaTotal = new PdfPCell(new Phrase("Total: € " + orden.getTotal(), fuenteParrafoNegrita));
		celdaTotal.setColspan(4);
		celdaTotal.setBorderColor(BaseColor.WHITE); 	
		celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tablaTotal.addCell(celdaTotal);
		documento.add(tablaTotal);
		documento.add(saltoLinea);
		documento.add(saltoLinea);
		// PIE DE PAGINA
		PdfPTable tablaPie = new PdfPTable(1);
		PdfPCell celdaPie = new PdfPCell(new Phrase("Spring eComerce © Andres Mariano Fernández 2023", fuenteParrafo));
		celdaPie.setColspan(4);
		celdaPie.setBorderColor(BaseColor.WHITE); 	
		celdaPie.setHorizontalAlignment(Element.ALIGN_CENTER);
		tablaPie.addCell(celdaPie);
		documento.add(tablaPie);
		// CERRAR DOCUMENTO
		documento.close();
	}

}
