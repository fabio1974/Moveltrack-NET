package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.OrdemDeServicoDao;
import net.moveltrack.domain.OrdemDeServico;
import net.moveltrack.domain.Viagem;

@Named
public class OrdemDeServicoConverter implements Converter, Serializable {

	public OrdemDeServicoConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	OrdemDeServicoDao dao;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return dao.findByNumero(arg2);
		}catch(Exception e){
			//System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof OrdemDeServico) {
			OrdemDeServico obj = (OrdemDeServico) arg2;
			return obj.getNumero();
		}
		return null;
	}
}