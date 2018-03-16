package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import net.moveltrack.util.Utils;

@Named
public class RealConverter implements Converter, Serializable {

	public RealConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return Utils.stringToPriceDouble(arg2);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		try {
			return "R$"+Utils.priceWithDecimal((Double)arg2);	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}