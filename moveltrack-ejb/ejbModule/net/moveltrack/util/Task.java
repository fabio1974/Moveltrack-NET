package net.moveltrack.util;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletContext;

public abstract class Task implements Runnable{
	
	public Task() {
	}

	private String inicio;
	private long periodo;
	private long intervalo;
	private ServletContext servletContext;

	public String geInicio() {
		return inicio;
	}
	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	public long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(long periodo) {
		this.periodo = periodo;
	}
	public long getIntervalo() {
		if(this.inicio==null){
			System.out.println(this.getClass().getName()+" vai começar em "+ this.intervalo + " segundos, rodando a cada "+ this.periodo + " segundos.");
			return this.intervalo;
		}	

		Calendar inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY,Integer.parseInt(this.inicio.substring(0,2)));
		inicio.set(Calendar.MINUTE,Integer.parseInt(this.inicio.substring(3,5)));
		inicio.set(Calendar.SECOND,Integer.parseInt(this.inicio.substring(6,8)));

		Calendar now = Calendar.getInstance();
		if(inicio.before(now))
			inicio.add(Calendar.DAY_OF_YEAR,1);

		System.out.println(this.getClass().getName()+" vai começar em "+ inicio.getTime()+ ", rodando a cada "+ this.periodo + " segundos.");
		inicio.add(Calendar.SECOND,1);
		return (inicio.getTimeInMillis() - now.getTimeInMillis())/1000;	
	}

	
	/**
	 * @param intervalo em segundos
	 */
	public void setIntervalo(long intervalo) {
		this.intervalo = intervalo;
	}
	public ServletContext getServletContext() {
		return servletContext;
	}
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	
	

}
