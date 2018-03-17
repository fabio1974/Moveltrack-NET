package org.jrimum.bopepo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jrimum.bopepo.view.BoletoViewer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Exemplo de boleto no estilo carnê com três boletos em uma página.
 * 
 * @author Rômulo Augusto
 */
public class BoletoCarne3PorPagina {

	
	public static byte[] groupInPages(List<Boleto> boletos, File templatePersonalizado) {
		byte[] arq = null;
		BoletoViewer boletoViewer = new BoletoViewer(boletos.get(0));
		boletoViewer.setTemplate(templatePersonalizado);
		List<byte[]> boletosEmBytes = new ArrayList<byte[]>(boletos.size());
/*		for (int i = 11; i >= 0; i--) {
			Boleto bop = boletos.get(i);
			boletosEmBytes.add(boletoViewer.setBoleto(bop).getPdfAsByteArray());
		}
*/
		for (Boleto bop : boletos) {
			boletosEmBytes.add(boletoViewer.setBoleto(bop).getPdfAsByteArray());
		}

		try {
			arq = mergeFilesInPages(boletosEmBytes);
		} catch (Exception e) {
			throw new IllegalStateException("Erro durante geração do PDF! Causado por " + e.getLocalizedMessage(), e);
		}
		return arq;
	}

	public static byte[] mergeFilesInPages(List<byte[]> pdfFilesAsByteArray) throws DocumentException, IOException {
		Document document = new Document();
		ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, byteOS);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        float positionAnterior = 0;
        for (byte[] in : pdfFilesAsByteArray) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            	float documentHeight = cb.getPdfDocument().getPageSize().getHeight();
            	//import the page from source pdf
            	PdfImportedPage page = writer.getImportedPage(reader, i);
            	float pagePosition = positionAnterior;
            	if ( (documentHeight - positionAnterior) <=  page.getHeight()) {
            		document.newPage();
            		pagePosition = 0;
            		positionAnterior = 0;
            	}
                //add the page to the destination pdf
                cb.addTemplate(page, 0, pagePosition);
                positionAnterior += page.getHeight();
            }
        }
        byteOS.flush();
        document.close();
        byte[] arquivoEmBytes = byteOS.toByteArray();
        byteOS.close();
        return arquivoEmBytes;
	}
}
