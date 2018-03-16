package net.moveltrack.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;


@Entity
public class Vertice extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1323004107026182545L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

    private double lat;

    private double lng;

    @ManyToOne
    private Poligono poligono;

	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public Poligono getPoligono() {
		return poligono;
	}
	public void setPoligono(Poligono poligono) {
		this.poligono = poligono;
	}

    
	
}
