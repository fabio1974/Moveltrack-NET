package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ContratoDao;
import net.moveltrack.domain.Chip;
import net.moveltrack.domain.Contrato;

@Named
public class ContratoConverter implements Converter, Serializable {

	public ContratoConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	ContratoDao dao;
	

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			String[] a = arg2.split("-"); 
			Object obj = dao.findByNumeroContrato(a[0].trim());
			return obj;
		}catch(NumberFormatException ex){
			System.err.println(ex.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		try{
			if (arg2 != null && arg2 instanceof Contrato) {
				Contrato obj = (Contrato) arg2;
				return obj.getNumeroContrato() + "-" + obj.getCliente().getNome();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}