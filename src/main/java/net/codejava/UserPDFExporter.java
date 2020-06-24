package net.codejava;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserPDFExporter {
	private List<Product> listProducts;

	public UserPDFExporter(List<Product> listProducts) {
		this.listProducts = listProducts;
	} 
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell(); 
		
		cell.setBackgroundColor(Color.GRAY);
		cell.setPadding(5);
			
		Font font = FontFactory.getFont(FontFactory.HELVETICA); 
		font.setColor(Color.white);
		
		cell.setPhrase(new Phrase("Product ID", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell); 
		
		cell.setPhrase(new Phrase("Brand", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Made in", font));
		table.addCell(cell);
 
		cell.setPhrase(new Phrase("Price", font));
		table.addCell(cell);
	}
	
	private void writeTableData(PdfPTable table) {
		  for(Product product : listProducts) {
			  table.addCell(String.valueOf(product.getId())); 
			  table.addCell(product.getName());
			  table.addCell(product.getBrand());
			  table.addCell(product.getMadein());
			  table.addCell(Float.toString(product.getPrice()));		  			  
		  }
	} 
	
	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4); 
		
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD); 
		font.setColor(Color.BLUE);
		font.setSize(16);
		
		Paragraph title = new Paragraph("List of products", font); 
		title.setAlignment(Paragraph.ALIGN_CENTER); 
		document.add(title); 
		
		//document.add(new Paragraph("Lists of all products")); 
		
		PdfPTable table = new PdfPTable(5); // 5 = numColumns 
		table.setWidthPercentage(100);
		table.setSpacingBefore(13);
		table.setWidths(new float[] {1.75f, 3.5f, 3.0f, 3.0f, 1.5f} );
		
		writeTableHeader(table);
		writeTableData(table);
		
		document.add(table); 
		
		document.close();	

	}
}
