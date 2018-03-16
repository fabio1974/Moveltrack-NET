package net.moveltrack.domain;

import java.io.Serializable;
import java.util.List;

public class Mapa  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6414866483868643591L;
	private List<LastLocation> lastLocations;
	private Location centro;
	private int zoom;
	private Location pNE = new Location();
	private Location pSW = new Location();
	
	
	public Location getpNE() {
		return pNE;
	}
	public void setpNE(Location pNE) {
		this.pNE = pNE;
	}
	public Location getpSW() {
		return pSW;
	}
	public void setpSW(Location pSW) {
		this.pSW = pSW;
	}
	
	
	
	public List<LastLocation> getLastLocations() {
		return lastLocations;
	}
	public void setLastLocations(List<LastLocation> lastLocations) {
		this.lastLocations = lastLocations;
	}
	public Location getCentro() {
		return centro;
	}
	public void setCentro(Location centro) {
		this.centro = centro;
	}
	public int getZoom() {
		return zoom;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
}
