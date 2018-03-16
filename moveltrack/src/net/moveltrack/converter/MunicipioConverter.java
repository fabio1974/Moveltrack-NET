package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.MunicipioDao;
import net.moveltrack.domain.Chip;
import net.moveltrack.domain.Municipio;

@Named
public class MunicipioConverter implements Converter, Serializable {

	public MunicipioConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	MunicipioDao dao;
	
	Class<Chip> clazz = Chip.class;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			String[] a = arg2.split("-"); 
			return dao.findByDescricaoUf(a[0].trim(),a[1].trim());
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Municipio) {
			Municipio obj = (Municipio) arg2;
			return obj.getDescricao() + "-" + obj.getUf().getSigla();
		}
		return "";
	}
}