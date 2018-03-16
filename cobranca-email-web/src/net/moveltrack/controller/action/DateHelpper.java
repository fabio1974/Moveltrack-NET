package net.moveltrack.controller.action;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.commons.lang3.time.DateFormatUtils;

@Named
@RequestScoped
public class DateHelpper {

	public DateHelpper() {
		// TODO Auto-generated constructor stub
	}
	
	public String ddMMyyyy(){
		return DateFormatUtils.format(GregorianCalendar.getInstance().getTime(), "dd/MM/yyyy");
	}
	
	public String HHmmss(){
		return DateFormatUtils.format(GregorianCalendar.getInstance().getTime(), "HH:mm:ss");
	}
	
	public String ddMMyyyyHHmmss(){
		
		return this.ddMMyyyy() + " AS " + this.HHmmss();
	}
	
	public String getCompetencia(){
		return DateFormatUtils.format(GregorianCalendar.getInstance().getTime(), "MM/yyyy");
	}
	
	public String getMesCompetencia(){
		return DateFormatUtils.format(GregorianCalendar.getInstance().getTime(), "MM");
	}
	
	public String getExercicio(){
		return DateFormatUtils.format(GregorianCalendar.getInstance().getTime(), "yyyy");
	}
	
	public Date today(){
		return GregorianCalendar.getInstance().getTime();
	}
	
	public static void main(String[] args) {
		DateHelpper d = new DateHelpper();
		
		System.out.println(d.ddMMyyyy());
		System.out.println(d.HHmmss());
		System.out.println(d.ddMMyyyyHHmmss());
	}
	
	

}
