package com.moveltrack.reactbackend.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity
public class Cerca {
	/**
	 * 
	 */


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

    private double lat1;

    private double lon1;

    private double lat2;

    private double lon2;

    private float raio;
    
    @Transient
    private int zoom;


	public double getLat1() {
		return lat1;
	}
	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}
	public double getLon1() {
		return lon1;
	}
	public void setLon1(double lon1) {
		this.lon1 = lon1;
	}
	public double getLat2() {
		return lat2;
	}
	public void setLat2(double lat2) {
		this.lat2 = lat2;
	}
	public double getLon2() {
		return lon2;
	}
	public void setLon2(double lon2) {
		this.lon2 = lon2;
	}
	public float getRaio() {
		return raio;
	}

	public void setRaio(float raio) {
		this.raio = raio;
	}
	public int getZoom() {
		return (int) Math.round(24-Math.log(1000*this.raio)/Math.log(2));
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	
	
}
