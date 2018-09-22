package com.moveltrack.cartaoprograma.model.st500;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ST500PID {

	static String ST500PIDStr = "ST500PID;205778292;07;844;20180514;20:48:01;-03.807297;-038.497518;018.490;329.53;5;1;320;023;";
	
	/*public static void main(String[] args) {
		
		ST500PID teste = parse(ST500PIDStr.split(";"));
		teste.getCourse();
		
	}*/
	
	public static ST500PID parse(String[] fields) {

		ST500PID res = new ST500PID();
		try {res.setSerial(fields[1]);}catch(Exception e){}
		try {res.setModel(fields[2]);}catch(Exception e){}
		try {res.setSwVersion(fields[3]);}catch(Exception e){}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			Date d1= format.parse(fields[4]+" "+fields[5]);
			Calendar c = Calendar.getInstance();
			c.setTime(d1);
			c.add(Calendar.MINUTE,-180);
			res.setDeviceDate(c.getTime());
		}catch(Exception e){}
		try {res.setLatitude(Double.parseDouble(fields[6]));}catch(Exception e){}
		try {res.setLongitude(Double.parseDouble(fields[7]));}catch(Exception e){}
		try {res.setSpeed(Double.parseDouble(fields[8]));}catch(Exception e){}
		try {res.setCourse(Double.parseDouble(fields[9]));}catch(Exception e){}
		try {res.setSattellite(Integer.parseInt(fields[10]));}catch(Exception e){}
		try {res.setGps(Integer.parseInt(fields[11]));}catch(Exception e){}
		try {res.setDistance(Integer.parseInt(fields[12]));}catch(Exception e){}
		try {res.setPidCount(Integer.parseInt(fields[13]));}catch(Exception e){}

		res.setPids(new ArrayList<PID>());

		for (int i = 14; i < fields.length; i++) {
			String[] pidStr = fields[i].split("\\|");   //pidStr = 01|0007E180
			byte hex = Byte.decode("0x"+pidStr[0]);
			PID pid = new PID();
			pid.setHex(hex);
			pid.setValue(pidStr[1]);
			res.getPids().add(pid);
		}
		res.setStoreDate(new Date());
		return res;
	}	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="serial")
	private Long id;
	private String serial;
	
	@JsonIgnore
	private String model;
	private String swVersion;
	private double latitude;
	private double longitude;
	private Date deviceDate;
	
	@JsonIgnore
	private Date storeDate;
	private double speed;
	private double course;
	private int sattellite;
	private int gps;
	private int distance;
	private int pidCount;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<PID> pids = new ArrayList<PID>();




	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}


	public String getSerial() {
		return serial;
	}


	public void setSerial(String serial) {
		this.serial = serial;
	}



	public String getModel() {
		return model;
	}



	public void setModel(String model) {
		this.model = model;
	}



	public String getSwVersion() {
		return swVersion;
	}



	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
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



	public Date getDeviceDate() {
		return deviceDate;
	}



	public void setDeviceDate(Date deviceDate) {
		this.deviceDate = deviceDate;
	}



	public Date getStoreDate() {
		return storeDate;
	}



	public void setStoreDate(Date storeDate) {
		this.storeDate = storeDate;
	}



	public double getSpeed() {
		return speed;
	}



	public void setSpeed(double speed) {
		this.speed = speed;
	}



	public double getCourse() {
		return course;
	}



	public void setCourse(double course) {
		this.course = course;
	}



	public int getSattellite() {
		return sattellite;
	}



	public void setSattellite(int sattellite) {
		this.sattellite = sattellite;
	}



	public int getGps() {
		return gps;
	}



	public void setGps(int gps) {
		this.gps = gps;
	}



	public int getDistance() {
		return distance;
	}



	public void setDistance(int distance) {
		this.distance = distance;
	}



	public int getPidCount() {
		return pidCount;
	}



	public void setPidCount(int pidCount) {
		this.pidCount = pidCount;
	}



	public List<PID> getPids() {
		return pids;
	}



	public void setPids(List<PID> pids) {
		this.pids = pids;
	}



}