package net.moveltrack.util;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

public class MyParagraph extends Paragraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyParagraph(int alignment,float leading, String string, Font font, int spacingAfter) {
		super(leading, string, font);
		super.setAlignment(alignment);
		super.setSpacingAfter(spacingAfter);
	}

}
