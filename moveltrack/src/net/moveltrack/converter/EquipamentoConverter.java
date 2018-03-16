package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.domain.Chip;
import net.moveltrack.domain.Equipamento;

@Named
public class EquipamentoConverter implements Converter, Serializable {

	public EquipamentoConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	EquipamentoDao dao;
	

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			String[] a = arg2.split("-"); 
			Object obj = dao.findByImei(a[0].trim());
			return obj;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Equipamento) {
			Equipamento obj = (Equipamento) arg2;
			return obj.getImei()+ "-" + obj.getModelo();
		}
		return "";
	}
}