package net.moveltrack.converter;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

@Named
public class LitrosConverter implements Converter, Serializable {

	public LitrosConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			String s = arg2.replaceAll("[^\\d]","");
			return (Double.parseDouble(s)/100);
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		double value = 0.0;
		try{
			value =	((Double)arg2).doubleValue();
		}catch(Exception e){
			
		}
		
		if(value>0){
		    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
		    return formatter.format((Double)arg2);
		}else
			return "";
	}
}


