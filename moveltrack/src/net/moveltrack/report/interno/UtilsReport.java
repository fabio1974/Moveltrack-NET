package net.moveltrack.report.interno;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;

import net.moveltrack.domain.Operadora;
import net.moveltrack.util.MyParagraph;



public class UtilsReport {
	
	public static final Color FAIRLY_LIGTH_GRAY = new Color(240,240,240);
	public static final float interTables = 10f;

	
	public static PdfPTable getCabecalho() {
		try{
			PdfPTable table = new PdfPTable(9);
			table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
			table.setWidthPercentage(100);
/*		
		    Image image;		
		    String filePath = "basepath" + File.separator + "images"+ File.separator + "LOGOTIPO_WEB.png";
			System.out.println(filePath);
			image = Image.getInstance(filePath);
			table.addCell(getImageCell(image,2,Paragraph.ALIGN_CENTER));*/
			
			PdfPCell c = new PdfPCell();
			c.addElement(new MyParagraph(Paragraph.ALIGN_CENTER,12f,"MOVELTRACK RASTREAMENTO",new Font(Font.TIMES_ROMAN, 12),12));
			c.addElement(new MyParagraph(Paragraph.ALIGN_CENTER,0,"Av. Visconde do Rio Branco, 3066, sala 04 - Fortaleza/CE",new Font(Font.TIMES_ROMAN, 10),12));
			c.addElement(new MyParagraph(Paragraph.ALIGN_CENTER,0,"CNPJ: 17.547.013/0001-88",new Font(Font.TIMES_ROMAN, 10),12));
			c.addElement(new MyParagraph(Paragraph.ALIGN_CENTER,0,"Telefones: (85)4105-0145 / (85)98549-7318",new Font(Font.TIMES_ROMAN, 10),2));
			c.setColspan(5);
			
			table.addCell(c);
			//table.addCell(getImageCell(image,2,Paragraph.ALIGN_CENTER));
			return table;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static PdfPTable getInformacoes(String titulo,List<String> linhas) {

		try{
			PdfPTable table = new PdfPTable(1);
			table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
			table.setWidthPercentage(100);

			PdfPCell c = new PdfPCell();
			c.addElement(new MyParagraph(Paragraph.ALIGN_CENTER,10,titulo,new Font(Font.TIMES_ROMAN, 12),14));
			
			for (int i = 0; i < linhas.size(); i++) {
				c.addElement(new MyParagraph(Paragraph.ALIGN_LEFT,0,linhas.get(i),new Font(Font.TIMES_ROMAN, 10),(i==(linhas.size()-1)?2:12)));
			}

			table.addCell(c);
			return table;
		}catch(Exception e){
			return null;
		}
	}	
	
	

	
	
	public static PdfPCell getHeader(String title,int colspan,int horizontalAlignment){
		Font f = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(FAIRLY_LIGTH_GRAY);
		cell.setColspan(colspan);
		cell.addElement(new MyParagraph(horizontalAlignment,10f,title,f,0));
		return cell;
	} 

	public static PdfPCell getMyCell(String header,String content,int colspan){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		PdfPTable t = new PdfPTable(1);
		t.setWidthPercentage(100);
		PdfPCell c1 = new PdfPCell();
		c1.setNoWrap(false);
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		c1.addElement(new MyParagraph(Paragraph.ALIGN_LEFT,7f,header,new Font(Font.TIMES_ROMAN, 10, Font.BOLD),0));
		c1.addElement(new MyParagraph(Paragraph.ALIGN_LEFT,13f,"   "+content,new Font(Font.TIMES_ROMAN, 11, Font.NORMAL),0));
		t.addCell(c1);
		c.addElement(t);
		return c;
	}
	
	
	public static PdfPCell getCellBoleto(String header,String content,int colspan,int align){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		PdfPTable t = new PdfPTable(1);
		t.setWidthPercentage(100);
		PdfPCell c1 = new PdfPCell();
		c1.setNoWrap(false);
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		c1.addElement(new MyParagraph(align,7f," "+header,new Font(Font.TIMES_ROMAN, 9, Font.NORMAL),0));
		c1.addElement(new MyParagraph(align,13f," "+content,new Font(Font.HELVETICA, 12, Font.BOLD),0));
		t.addCell(c1);
		c.addElement(t);
		return c;
	}
	
	public static PdfPCell getCellHeader(String content,int colspan,int align,int contentSize,Color bg){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		c.addElement(new MyParagraph(align,14f," "+content,new Font(Font.TIMES_ROMAN, contentSize, Font.BOLD),0));
		c.setBackgroundColor(bg);
		return c;
	}
	
	
	
	
	public static PdfPCell getCellBoleto(String header,String content,int colspan,int align, int headerSize, int contentSize){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		PdfPTable t = new PdfPTable(1);
		t.setWidthPercentage(100);
		PdfPCell c1 = new PdfPCell();
		c1.setNoWrap(false);
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		c1.addElement(new MyParagraph(align,7f," "+header,new Font(Font.TIMES_ROMAN, headerSize, Font.NORMAL),0));
		c1.addElement(new MyParagraph(align,11f," "+content,new Font(Font.HELVETICA, contentSize, Font.BOLD),0));
		t.addCell(c1);
		c.addElement(t);
		return c;
	}
	
	
	public static PdfPCell getImageCellFromByteArray(byte[] byteArray,int colspan){
		PdfPCell cell = new PdfPCell();
		cell.setColspan(colspan);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.addElement(getFromByteArray(byteArray));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}
	
	public static PdfPCell getImageCellFromImage(Image image,int colspan){
		PdfPCell cell = new PdfPCell();
		cell.setColspan(colspan);
		cell.addElement(image);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	} 
	
	private static Image getFromByteArray(byte[] byteArray) {
		Image image = null;
		try {
			image = Image.getInstance(byteArray);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	
	public static PdfPCell getImageCellFromPath(ServletContext servletContext,String imagePath,int colspan){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		c.addElement(getFromPath(servletContext,imagePath));
		return c;
	}
	
	public static PdfPCell getImageCellFromPath(ServletContext servletContext,String imagePath,int colspan,boolean noBorder,int padding){
		PdfPCell c = new PdfPCell();
		if(noBorder)
			c.setBorder(PdfPCell.NO_BORDER);
		c.setPadding(padding);
		c.setNoWrap(false);
		c.setColspan(colspan);
		c.addElement(getFromPath(servletContext,imagePath));
		return c;
	}

	
	
	
	private static Image getFromPath(ServletContext servletContext, String imagePath) {
		Image image = null;
		String realPath = servletContext.getRealPath(imagePath);
		try {
			image = Image.getInstance(realPath);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	

	public static Paragraph dottedLineSeparator(){
	      Paragraph p = new Paragraph("");
	      LineSeparator line  = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -10);
	      p.add(line);
	      return p;
	}

	
	
	public static PdfPCell getMyCellWithHeader(String header,String content,int colspan){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		PdfPTable t = new PdfPTable(1);
		t.setWidthPercentage(100);
		PdfPCell c1 = new PdfPCell();
		c1.setNoWrap(false);
		c1.setBorder(PdfPCell.NO_BORDER);
		c1.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		c1.setBackgroundColor(FAIRLY_LIGTH_GRAY);
		c1.addElement(new MyParagraph(Paragraph.ALIGN_LEFT,7f,header,new Font(Font.TIMES_ROMAN, 10, Font.BOLD),0));
		c1.addElement(new MyParagraph(Paragraph.ALIGN_LEFT,13f,"   "+content,new Font(Font.TIMES_ROMAN, 11, Font.NORMAL),0));
		t.addCell(c1);
		c.addElement(t);
		return c;
	}
	


	public static PdfPCell getMyCell2(int horizontalAlignment,String content,int colspan,Color baseColor){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		c.addElement(new MyParagraph(horizontalAlignment,10f,content,new Font(Font.TIMES_ROMAN, 10, Font.BOLD,baseColor),0));
		return c;
	}	
	
	public static PdfPCell getEmptyCell(int colspan){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setBorder(PdfPCell.NO_BORDER);
		c.setColspan(colspan);
		return c;
	}
	
	public static PdfPCell getMyCell2(int horizontalAlignment,String content,int colspan,Color baseColor, Color bgcolor){
		PdfPCell c = new PdfPCell();
		c.setNoWrap(false);
		c.setColspan(colspan);
		c.addElement(new MyParagraph(horizontalAlignment,10f,content,new Font(Font.TIMES_ROMAN, 10, Font.BOLD,baseColor),0));
		c.setBackgroundColor(bgcolor);
        return c;
	}
	
	
	public static String getOperadoraShort(Operadora o){
		if(o==null)
			return "";
		switch (o) {
		case CLARO:
			return "C";
		case TIM:
			return "T";
		case OI:
			return "O";
		case VIVO:
			return "V";
		case LINK_CLARO:
			return "LC";
		case LINK_TIM:
			return "LT";
		case LINK_VIVO:
			return "LV";
		case VODAFONE:
			return "VO";
		case TRANSMEET_VIVO:
			return "TV";
		default:
			break;
		}
		return "";
	}


}
