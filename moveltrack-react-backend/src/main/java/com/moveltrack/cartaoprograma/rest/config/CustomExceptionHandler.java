package com.moveltrack.cartaoprograma.rest.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	public CustomExceptionHandler(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}
	
	@Autowired
	private MessageSource messageSource;


	@ExceptionHandler({ RepositoryConstraintViolationException.class })
	public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
		RepositoryConstraintViolationException nevEx = (RepositoryConstraintViolationException) ex;
		List<FieldError> errors = nevEx.getErrors().getFieldErrors();
		
		ErrorMessage em = new ErrorMessage(); 
		for (FieldError error : errors) {
			System.out.println(error.getObjectName() + "-" + error.getField() + error.getDefaultMessage());
			em.getErrors().add(error.getDefaultMessage());
			em.getErrors_description().add(ex.getCause() != null ? ex.getCause().toString() : ex.getMessage());
		}
		return handleExceptionInternal(ex, em , null,	HttpStatus.PARTIAL_CONTENT, request);
	}
	
	@ExceptionHandler
    @ResponseBody
    public ResponseEntity<Object> handleException (Exception ex) {
		ErrorMessage em = new ErrorMessage();
		HttpStatus status = null;
		
		if(ex instanceof DataIntegrityViolationException){
			em.getErrors().add("Erro de integridade. Dados duplicados!");
			status = HttpStatus.CONFLICT;
		}else if(ex instanceof ResourceNotFoundException) {
			em.getErrors().add("Recurso não encontrado!");
			status = HttpStatus.NOT_FOUND;
		}else if(ex instanceof JpaObjectRetrievalFailureException) {
			em.getErrors().add("Objecto não encontrado. Erro no objeto passado!");
			status = HttpStatus.BAD_REQUEST;
		}else if(ex instanceof InsufficientAuthenticationException) {
			em.getErrors().add("Problemas com autenticação!");
			status = HttpStatus.UNAUTHORIZED;
		}else {
			em.getErrors().add(ex.getCause() != null ? ex.getCause().toString() : ex.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		em.getErrors_description().add(ex.getCause() != null ? ex.getCause().toString() : ex.getMessage());		
		return handleExceptionInternal(ex, em , null,status, null);
    }

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage em = new ErrorMessage();
		em.getErrors().add("Erro 400: Json mal formatado!");
		em.getErrors_description().add(ex.getCause() != null ? ex.getCause().toString() : ex.getMessage());
		return handleExceptionInternal(ex, em , headers,HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,	HttpStatus status, WebRequest request) {
		ErrorMessage em = new ErrorMessage();
		em.getErrors().add(status == HttpStatus.NOT_FOUND ? "Erro 404: Erro no caminho": ("Erro " + status.name()));
		em.getErrors_description().add(ex.getCause() != null ? ex.getCause().toString() : ex.getMessage());
		return handleExceptionInternal(ex, em , headers, status, request);
	}
	

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,HttpStatus status, WebRequest request) {
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	
	
	public static class ErrorMessage {
		
		private List<String> errors = new ArrayList<String>();
		private List<String> errors_description = new ArrayList<String>();
		public List<String> getErrors() {
			return errors;
		}
		public void setErrors(List<String> errors) {
			this.errors = errors;
		}
		public List<String> getErrors_description() {
			return errors_description;
		}
		public void setErrors_description(List<String> errors_description) {
			this.errors_description = errors_description;
		}
		
		
	}
}