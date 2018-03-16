package net.moveltrack.domain;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.moveltrack.util.DateAdapter;

@Entity
public class Location extends BaseEntity {
 
	private static final long serialVersionUID = -1286737355484126863L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

    @XmlJavaTypeAdapter(DateAdapter.class)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLocation;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLocationInicio;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private double velocidade;

    @NotNull
    private String imei;

    @Size(max = 30)
    private String comando;

   
    private int satelites;

   
    private int mcc;

    @Size(max = 10)
    private String bloqueio;

    @Size(max = 10)
    private String gps;

    @Size(max = 10)
    private String gsm;

    @Size(max = 10)
    private String sos;

    @Size(max = 10)
    private String battery;

    @Size(max = 10)
    private String volt;

    @Size(max = 10)
    private String ignition;

    @Size(max = 10)
    private String alarm;

    @Size(max = 30)
    private String alarmType;
    
    private int version;
    
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Date getDateLocation() {
		return dateLocation;
	}
	public void setDateLocation(Date dateLocation) {
		this.dateLocation = dateLocation;
	}
	public Date getDateLocationInicio() {
		return dateLocationInicio;
	}
	public void setDateLocationInicio(Date dateLocationInicio) {
		this.dateLocationInicio = dateLocationInicio;
	}
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
	public double getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(double velocidade) {
		this.velocidade = velocidade;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getComando() {
		return comando;
	}
	public void setComando(String comando) {
		this.comando = comando;
	}
	public int getSatelites() {
		return satelites;
	}
	public void setSatelites(int satelites) {
		this.satelites = satelites;
	}
	public int getMcc() {
		return mcc;
	}
	public void setMcc(int mcc) {
		this.mcc = mcc;
	}
	public String getBloqueio() {
		return bloqueio;
	}
	public void setBloqueio(String bloqueio) {
		this.bloqueio = bloqueio;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public String getGsm() {
		return gsm;
	}
	public void setGsm(String gsm) {
		this.gsm = gsm;
	}
	public String getSos() {
		return sos;
	}
	public void setSos(String sos) {
		this.sos = sos;
	}
	public String getBattery() {
		return battery;
	}
	public void setBattery(String battery) {
		this.battery = battery;
	}
	public String getVolt() {
		return volt;
	}
	public void setVolt(String volt) {
		this.volt = volt;
	}
	public String getIgnition() {
		return ignition;
	}
	public void setIgnition(String ignition) {
		this.ignition = ignition;
	}
	public String getAlarm() {
		return alarm;
	}
	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
    
    
}
