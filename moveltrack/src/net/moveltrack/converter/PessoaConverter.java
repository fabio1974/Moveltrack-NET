package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.PessoaDao;
import net.moveltrack.domain.Pessoa;

@Named
public class PessoaConverter implements Converter, Serializable {

	public PessoaConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;

	@Inject
	ConverterHelper converterHelper;

	@Inject
	PessoaDao dao;
	
	Class<Pessoa> clazz = Pessoa.class;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return dao.findByNome(arg2);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Pessoa) {
			Pessoa obj = (Pessoa) arg2;
			return obj.getNome();
		}
		return "";
	}
}