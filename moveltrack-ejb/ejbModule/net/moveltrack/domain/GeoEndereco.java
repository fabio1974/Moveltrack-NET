package net.moveltrack.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;



@Entity
public class GeoEndereco extends BaseEntity {
	

	private static final long serialVersionUID = 3753342465178454888L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    private String endereco;

    @NotNull
    private Boolean confiavel;

	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public Boolean getConfiavel() {
		return confiavel;
	}
	public void setConfiavel(Boolean confiavel) {
		this.confiavel = confiavel;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
	
}
