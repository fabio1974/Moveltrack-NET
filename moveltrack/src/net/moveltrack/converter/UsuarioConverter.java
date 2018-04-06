package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Usuario;

@Named
public class UsuarioConverter implements Converter, Serializable {

	public UsuarioConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;

	@Inject
	UsuarioDao dao;
	
	Class<Usuario> clazz = Usuario.class;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return dao.findByNomeUsuario(arg2);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Usuario) {
			Usuario obj = (Usuario) arg2;
			return obj.getNomeUsuario();
		}
		return "";
	}
}