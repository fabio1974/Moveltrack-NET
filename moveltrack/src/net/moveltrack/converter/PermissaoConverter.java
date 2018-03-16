package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.PermissaoDao;
import net.moveltrack.domain.Permissao;

@Named
public class PermissaoConverter implements Converter, Serializable {

	public PermissaoConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;

	@Inject
	PermissaoDao dao;



	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return dao.findByDescricao(arg2);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Permissao) {
			Permissao obj = (Permissao) arg2;
			return obj.getDescricao();
		}
		return null;
	}
}