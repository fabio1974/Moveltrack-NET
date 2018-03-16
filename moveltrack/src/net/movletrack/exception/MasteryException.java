package net.movletrack.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Representa as execoes da camada de negocio EBJ em forma de mensagens.
 * 
 *  Estao divididas em tres grupos:
 *  
 *  INFO  - mensagens em carater informativo
 *  WARN  - mensagens em carater de alerta
 *  ERROR - mensagens em carater de erro
 *  
 *  Uma mensagem pode ser exibida de forma global, ou seja, nao tem referencia a um campo especifico do formulario,
 *  ou ser referente a um campo especifico do formulario.
 *   
 * 
 * @author celio.abreu@swpremium.com.br
 *
 */
//@Stateful
//@LocalBean
public class MasteryException extends Exception implements Serializable{
	
	private static final long serialVersionUID = 992467286519907878L;
	
	/**
	 * Mensagens globais em carater informativo
	 */
	private List<String> globalInfos;
	
	/**
	 * Mensagens globais em carater de alerta
	 */
	private List<String> globalWarns;
	
	/**
	 * Mensagens globais em carater de erro
	 */
	private List<String> globalErrors;
	

	/**
	 * Mensagens referente a um campo em carater informativo
	 */
	private Map<String,String> fieldInfos;
	
	/**
	 * Mensagens referente a um campo em carater de alerta
	 */
	private Map<String,String> fieldWarns;
	
	/**
	 * Mensagens referente a um campo em carater de erro
	 */
	private Map<String,String> fieldErrors;
	
	
	
	/**
	 * construtor padrao 
	 */
	public MasteryException(){
		
	}
	
	/**
	 * 
	 * Por padrao sera criado uma mensagem global em carater Informativo
	 * 
	 * @param mensagem - mensagen a ser adicionada
	 * @return A propria instancia de MasteryException
	 */
	public MasteryException(String mensagem){
		
		globalInfos = new ArrayList<String>(0);
		if(mensagem != null){
			globalInfos.add(mensagem);
		}
	}
	
	/**
	 * Por padrao sera criado uma mensagem global em carater Informativo
	 * 
	 * @param exception - exception a qual a mensagem sera obtida
	 * @return A propria instancia de MasteryException 
	 */
	public MasteryException(Exception exception){
		
		if(exception == null) return;
		
		globalInfos = new ArrayList<String>(0);
		if(exception.getMessage() != null){
			globalInfos.add(exception.getMessage());
		}
	}

	/**
	 * 
	 * Adiciona uma mensagem global em carater informativo
	 * @param mensagem - mensagen a ser adicionada
	 * @return A propria instancia de MasteryException
	 */
	public MasteryException addGlobalInfo(String mensagem){
		if(globalInfos == null){
			globalInfos = new ArrayList<String>();
		}
		
		globalInfos.add(mensagem);
		
		return this;
		
	}
	
	/**
	 * Adiciona uma mensagem global em carater de alerta
	 * @param mensagem - mensagen a ser adicionada
	 * @return A propria instancia de MasteryException
	 */
	public MasteryException addGlobalWarn(String mensagem){
		if(globalWarns == null){
			globalWarns = new ArrayList<String>();
		}
		
		globalWarns.add(mensagem);
		
		return this;
		
	}
	
	/**
	 * Adiciona uma mensagem global em carater de erro
	 * @param mensagem - mensagen a ser adicionada
	 * @return A propria instancia de MasteryException
	 */
	public MasteryException addGlobalError(String mensagem){
		if(globalErrors == null){
			globalErrors = new ArrayList<String>();
		}
		
		globalErrors.add(mensagem);
		return this;
	}
	
	/**
	 * Adciona uma mensagem referente a um campo especifico do formulario em carater informativo
	 * 
	 * @param fieldId - identificador do campo no formulario
	 * @param mensagem - mensagen a ser adicionada
	 * @return A propria instancia de MasteryException
	 */
	public MasteryException addFieldInfo(String fieldId, String mensagem){
		if(fieldInfos == null){
			fieldInfos = new HashMap<String, String>();
		}
		
		fieldInfos.put(fieldId,mensagem);
		return this;
	}
	
	/**
	 * Adciona uma mensagem referente a um campo especifico do formulario em carater de alerta
	 * 
	 * @param fieldId - identificador do campo no formulario
	 * @param mensagem - mensagen a ser adicionada
	 * @return A propria instancia de MasteryException
	 */
	public MasteryException addFieldWarn(String fieldId, String mensagem){
		if(fieldWarns == null){
			fieldWarns = new HashMap<String, String>();
		}
		
		fieldWarns.put(fieldId,mensagem);
		return this;
	}
	
	/**
	 * Adciona uma mensagem referente a um campo especifico do formulario em carater de erro
	 * 
	 * @param fieldId - identificador do campo no formulario
	 * @param mensagem - mensagen a ser adicionada
	 * @return A propria instancia de MasteryException
	 */
	public MasteryException addFieldError(String fieldId, String mensagem){
		if(fieldErrors == null){
			fieldErrors = new HashMap<String, String>();
		}
		
		fieldErrors.put(fieldId,mensagem);
		
		return this;
		
	}

	/**
	 * Checa se existe mensagem global em carater informativo
	 * @return true se existe pelo menos uma mensagem global de carater informativo
	 */
	public boolean hasGlobalInfo(){
		if (globalInfos == null){
			return false;
		} else {
			return !globalInfos.isEmpty();
		}
	}

	/**
	 * Checa se existe mensagem global em carater de alerta
	 * @return true se existe pelo menos uma mensagem global em carater de alerta
	 */
	public boolean hasGlobalWarn(){
		if (globalWarns == null){
			return false;
		} else {
			return !globalWarns.isEmpty();
		}
	}

	/**
	 * Checa se existe mensagem global em carater de erro
	 * @return true se existe pelo menos uma mensagem global em carater de erro
	 */
	public boolean hasGlobalError(){
		if (globalErrors == null){
			return false;
		} else {
			return !globalErrors.isEmpty();
		}
	}


	/**
	 * Checa se existe mensagem de campo em carater informativo
	 * @return true se existe pelo menos uma mensagem de campo em carater informativo
	 */
	public boolean hasFieldInfo(){
		if (fieldInfos == null){
			return false;
		} else {
			return !fieldInfos.isEmpty();
		}
	}

	/**
	 * Checa se existe mensagem de campo em carater de alerta
	 * @return true se existe pelo menos uma mensagem de campo em carater de alerta
	 */
	public boolean hasFieldWarn(){
		if (fieldWarns == null){
			return false;
		} else {
			return !fieldWarns.isEmpty();
		}
	}

	/**
	 * Checa se existe mensagem de campo em carater de erro
	 * @return true se existe pelo menos uma mensagem de campo em carater de erro
	 */
	public boolean hasFieldError(){
		if (fieldErrors == null){
			return false;
		} else {
			return !fieldErrors.isEmpty();
		}
	}

	/**
	 * Retorna a lsta de mensagens gloabis em carater informativo 
	 * @return mensagens em forma de List<String>
	 */
	public List<String> getGlobalInfos(){
		if (hasGlobalInfo()){
			return globalInfos;
		} else {
			return new ArrayList<String>(0);
		}
	}

	/**
	 * Retorna a lsta de mensagens gloabis em carater de alerta 
	 * @return mensagens em forma de List<String>
	 */
	public List<String> getGlobalWarns(){
		if (hasGlobalWarn()){
			return globalWarns;
		} else {
			return new ArrayList<String>(0);
		}
	}

	/**
	 * Retorna a lsta de mensagens gloabis em carater de erro 
	 * @return mensagens em forma de List<String>
	 */
	public List<String> getGlobalErrors(){
		if (hasGlobalError()){
			return globalErrors;
		} else {
			return new ArrayList<String>(0);
		}
	}

	/**
	 * Retorna a lsta de mensagens de campo em carater informativo 
	 * @return mensagens em forma de List<String>
	 */
	public Map<String,String> getFieldInfos(){
		if (hasFieldInfo()){
			return fieldInfos;
		} else {
			return new HashMap<String, String>();
		}
	}

	/**
	 * Retorna a lsta de mensagens de campo em carater de alerta 
	 * @return mensagens em forma de List<String>
	 */
	public Map<String,String> getFieldWarns(){
		if (hasFieldWarn()){
			return fieldWarns;
		} else {
			return new HashMap<String, String>();
		}
	}

	/**
	 * Retorna a lsta de mensagens de campo em carater de erro 
	 * @return mensagens em forma de List<String>
	 */
	public Map<String,String> getFieldErrors(){
		if (hasFieldError()){
			return fieldErrors;
		} else {
			return new HashMap<String, String>();
		}
	}

	/**
	 * Checa se existe mensagem global ou de campo em carater informatifo
	 * @return true, se existe mensagem global ou de campo em carater informatifo
	 */
	public boolean hasInfo(){
		return hasGlobalInfo() || hasFieldInfo();
	}

	/**
	 * Checa se existe mensagem global ou de campo em carater de alerta
	 * @return true, se existe mensagem global ou de campo em carater de alerta
	 */
	public boolean hasWarn(){
		return hasGlobalWarn() || hasFieldWarn();
	}

	/**
	 * Checa se existe mensagem global ou de campo em carater de erro
	 * @return true, se existe mensagem global ou de campo em carater de erro
	 */
	public boolean hasError(){
		return hasGlobalError() || hasFieldError();
	}
	
	/**
	 * Checa se existe mensagem global ou de campo de qualquer carater
	 * @return true, se existe mensagem global ou de campo de qualquer carater
	 */
	public boolean hasAnyMessage(){
		return hasInfo() || hasWarn() || hasError();
	}

	/**
	 * Remove todas as mensagens globais em carater informativo
	 */
	public void clearGlobalInfos(){
		if(globalInfos != null){
			globalInfos.clear();
		}
	}

	/**
	 * Remove todas as mensagens globais em carater de alerta 
	 */
	public void clearGlobalWarns(){
		if(globalWarns != null){
			globalWarns.clear();
		}
	}
	
	/**
	 * Remove todas as mensagens globais em carater de erro
	 */
	public void clearGlobalErrors(){
		if(globalErrors != null){
			globalErrors.clear();
		}
	}

	/**
	 * Remove todas as mensagens de campo em carater informativo
	 */
	public void clearFieldInfos(){
		if(fieldInfos != null){
			fieldInfos.clear();
		}
	}

	/**
	 * Remove todas as mensagens de campo em carater de alerta
	 */
	public void clearFieldWarns(){
		if(fieldWarns != null){
			fieldWarns.clear();
		}
	}
	
	/**
	 * Remove todas as mensagens de campo em carater de erro
	 */
	public void clearFieldErrors(){
		if(fieldErrors != null){
			fieldErrors.clear();
		}
	}

	/**
	 * Remove todas as mensagens globais de qualquer carater
	 */
	public void clearGlobalMessages(){
		clearGlobalInfos();
		clearGlobalWarns();
		clearGlobalErrors();
	}

	/**
	 * Remove todas as mensagens de campo de qualquer carater
	 */
	public void clearFieldMessages(){
		clearFieldInfos();
		clearFieldWarns();
		clearFieldErrors();
	}

	/**
	 * Remove todas as mensagens gloabis e de campo de qualquer carater
	 */
	public void clearAllMessages(){
		clearGlobalMessages();
		clearFieldMessages();
		
	}
	
	/**
	 * 
	 * Converte uma Exception em uma mensagem global de carater de erro
	 * 
	 * @param e
	 */
	public void add(Exception e){
		addGlobalError(e.getMessage());
	}
	
	
	/**
	 * 
	 * Converte uma Exception em uma mensagem global de carater de erro e lanca a MasteryException
	 * 
	 * @param e
	 */
	public void addAndThrow(Exception e) throws MasteryException{
		addGlobalError(e.getMessage());
		throw this;
	}
	
	@Override
	public String toString() {
		super.toString();
		StringBuffer s = null;
		
		if(hasAnyMessage()){
			s = new StringBuffer();
			
			if(hasFieldError()){
				s.append("Field Erros="+fieldErrors.size());
			}
			
		}
		
		return s!= null ? s.toString() : super.toString();
	}
	

}
