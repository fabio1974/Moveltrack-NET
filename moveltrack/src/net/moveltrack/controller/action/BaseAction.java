package net.moveltrack.controller.action;

import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class BaseAction {
	
	private Boolean readonly;
	private Boolean enabled;
	
	private static final String DETALHE = "._detalhe";
	
	public void makeReadonly(){
		setReadonly(Boolean.TRUE);
		setEnabled(Boolean.FALSE);
	}
	
	public void makeEditable(){
		setReadonly(Boolean.FALSE);
		setEnabled(Boolean.TRUE);
	}
	
	public String getRequestParam(String name){
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();

		return request.getParameter(name);
	}
	
	public String unformatCpfCnpj(String cpfCnpj){
		if( StringUtils.isNotEmpty(cpfCnpj)){
			return cpfCnpj.replace( "-","").replace(".", "").replace("/", "");
		} else {
			return cpfCnpj;
		}
	}	
	
	public void operacaoNenhumRegistroEncontrado(){
		String mensagem = getFromBundler("operecao.nenhum.registro.encontrado");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, ""));
	}
	
	public void operacaoSucesso(Boolean readonly){
		makeReadonly();
		operacaoSucesso();
	}
	public void operacaoSucesso(){
		String mensagem = getFromBundler("operecao.realizada.com.sucesso");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, ""));
	}
	
	public void operacaoSucesso(String mensagem){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, ""));
	}
	
	public void operacaoInfo(String key){
		String mensagem = getFromBundler(key);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, ""));
	}
	
	public void operacaoErroByPass(String msgError){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msgError, ""));
	}
	
	public void removeMessages(){
		FacesContext context = FacesContext.getCurrentInstance();
		Iterator<FacesMessage> it = context.getMessages();
		while ( it.hasNext() ) {
		    it.next();
		    it.remove();
		}
	}

	
	public void operacaoErro(String msgError){
		this.operacaoErro(msgError,null,Boolean.TRUE);
	}

	
	public void operacaoErro(String msgError,String id){
		this.operacaoErro(msgError,id,Boolean.TRUE);
	}

	public void operacaoErro(String msgError,String id,Boolean showDetalhe){
		if(StringUtils.isEmpty(msgError)){
			msgError = "erro.operecao";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getFromBundler(msgError), ""));
		} else {
			String mensagem = getFromBundler(msgError);
			
				String detalhe =  " || ";
				detalhe +=  getFromBundler(msgError+DETALHE);
				
				if(!showDetalhe){
					detalhe="";
				} 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, detalhe));
				if(id!=null){
					UIInput input =(UIInput) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:"+id);
					if(input==null)
						System.out.println("Error: n�o existe elemento no form com esse id="+id);
					else
						input.setValid(false);
				}	
		}
	}
	
	

	
	private String getFromBundler(String key){
		ResourceBundle bundle = ResourceBundle.getBundle("messages");//, FacesContext.getCurrentInstance().getViewRoot().getLocale());
		String value = null;
		try {
			if(StringUtils.isNotEmpty(key)){
				value = bundle.getString(key);
			}
		} catch (MissingResourceException e) {
			value = "Moveltrack Segurança e Tecnologia informa ao desenvolvedor: key ( ??? " + key + " ??? ) não cadastrada no bundler";
		}
		return value;
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public String getContextPath() {
		return getRequest().getContextPath();
	}
	
	public String getRequestURI() {
		return getRequest().getRequestURI();
	}
	
	public String getRequestURL() {
		return getRequest().getRequestURL().toString();
	}

	public Boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getDisabled() {
		return !enabled;
	}

	public void setDisabled(Boolean disnabled) {
		this.enabled = !disnabled;
	}
}