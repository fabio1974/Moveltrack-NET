package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ChipDao;
import net.moveltrack.domain.Chip;

@Named
public class ChipConverter implements Converter, Serializable {

	public ChipConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	ChipDao dao;
	
	Class<Chip> clazz = Chip.class;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return dao.findByIccid(arg2);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Chip) {
			Chip obj = (Chip) arg2;
			return obj.getIccid();
		}
		return "";
	}
}