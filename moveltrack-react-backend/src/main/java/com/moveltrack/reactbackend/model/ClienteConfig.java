package com.moveltrack.reactbackend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClienteConfig {

	/**
	 * 
	 */

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    private String emailAlarme;
	private boolean alarmeVelocidade;
	private boolean alarmeBateria;
	private boolean alarmeCerca;
	


	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
		
	}

	public String getEmailAlarme() {
		return emailAlarme;
	}

	public void setEmailAlarme(String emailAlarme) {
		this.emailAlarme = emailAlarme;
	}

	public boolean isAlarmeVelocidade() {
		return alarmeVelocidade;
	}

	public void setAlarmeVelocidade(boolean alarmeVelocidade) {
		this.alarmeVelocidade = alarmeVelocidade;
	}

	public boolean isAlarmeBateria() {
		return alarmeBateria;
	}

	public void setAlarmeBateria(boolean alarmeBateria) {
		this.alarmeBateria = alarmeBateria;
	}

	public boolean isAlarmeCerca() {
		return alarmeCerca;
	}

	public void setAlarmeCerca(boolean alarmeCerca) {
		this.alarmeCerca = alarmeCerca;
	}
	
	

}
