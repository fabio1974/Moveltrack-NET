package com.moveltrack.cartaoprograma.model.st500;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ST500STT {
	
	static String ST500STTStr = "ST500STT;205778292;07;844;20180514;06:41:36;-03.806526;-038.496626;000.646;000.00;0;0;0;5.00;0;0;1;7106;005.410;0;005.410;22;0.0";
	
	/*public static void main(String[] args) {
		
		ST500STT teste = parse(ST500STTStr.split(";"));
		teste.getAreaCode();
		
	}*/
		

	public static ST500STT parse(String[] fields) {
			
			ST500STT res = new ST500STT();
			
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
			try {res.setSatellite(Integer.parseInt(fields[10]));}catch(Exception e){}
			try {res.setGps(Integer.parseInt(fields[11]));}catch(Exception e){}
			try {res.setDistance(Integer.parseInt(fields[12]));}catch(Exception e){}
			try {res.setVolt(Double.parseDouble(fields[13]));}catch(Exception e){}
			try {res.setOnline(Integer.parseInt(fields[14]));}catch(Exception e){}
			try {res.setIgnition(Integer.parseInt(fields[15]));}catch(Exception e){}
			try {res.setMode(fields[16]);}catch(Exception e){}
			try {res.setMessage(fields[17]);}catch(Exception e){}
			try {res.setMaxSpeed(Double.parseDouble(fields[18]));}catch(Exception e){}
			try {res.setOverSpeedTime(Integer.parseInt(fields[19]));}catch(Exception e){}
			try {res.setAverageSpeed(Double.parseDouble(fields[20]));}catch(Exception e){}
			try {res.setDriveHourMeter(Integer.parseInt(fields[21]));}catch(Exception e){}
			try {res.setBatteryVoltage(Double.parseDouble(fields[22]));}catch(Exception e){}
			try {res.setCellId(Integer.parseInt(fields[23]));}catch(Exception e){}
			try {res.setMcc(Integer.parseInt(fields[24]));}catch(Exception e){}
			try {res.setMnc(Integer.parseInt(fields[25]));}catch(Exception e){}
			try {res.setRxLevel(Integer.parseInt(fields[26]));}catch(Exception e){}
			try {res.setAreaCode(fields[27]);}catch(Exception e){}
			try {res.setTimingAdvance(Integer.parseInt(fields[28]));}catch(Exception e){}
			res.setStoreDate(new Date());
			
			return res;

			
			
		}
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="serial")
	private Long id;
		private String serial;
		private String model;
		private String swVersion;
		
		private Double latitude;
		private Double longitude;
		private Date deviceDate;
		private Date storeDate;
		private Double speed;
		private Double course;
		
		private Integer satellite;
		private Integer gps;
		private Integer distance;
		private Double volt;
		private Integer online;
		private Integer ignition;
		private String mode;
		private String message;
		private Double maxSpeed;
		private Integer overSpeedTime;
		private Double averageSpeed;
		private Integer driveHourMeter;
		private Double batteryVoltage;
		private Integer cellId;
		private Integer mcc;
		private Integer mnc;		
		private Integer rxLevel;
		private String areaCode;
		private Integer timingAdvance;

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


		public Double getLatitude() {
			return latitude;
		}


		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}


		public Double getLongitude() {
			return longitude;
		}


		public void setLongitude(Double longitude) {
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


		public Double getSpeed() {
			return speed;
		}


		public void setSpeed(Double speed) {
			this.speed = speed;
		}


		public Double getCourse() {
			return course;
		}


		public void setCourse(Double course) {
			this.course = course;
		}


		public Integer getSatellite() {
			return satellite;
		}


		public void setSatellite(Integer satellite) {
			this.satellite = satellite;
		}


		public Integer getGps() {
			return gps;
		}


		public void setGps(Integer gps) {
			this.gps = gps;
		}


		public Integer getDistance() {
			return distance;
		}


		public void setDistance(Integer distance) {
			this.distance = distance;
		}


		public Double getVolt() {
			return volt;
		}


		public void setVolt(Double volt) {
			this.volt = volt;
		}


		public Integer getOnline() {
			return online;
		}


		public void setOnline(Integer online) {
			this.online = online;
		}


		public Integer getIgnition() {
			return ignition;
		}


		public void setIgnition(Integer ignition) {
			this.ignition = ignition;
		}


		public String getMode() {
			return mode;
		}


		public void setMode(String mode) {
			this.mode = mode;
		}


		public String getMessage() {
			return message;
		}


		public void setMessage(String message) {
			this.message = message;
		}


		public Double getMaxSpeed() {
			return maxSpeed;
		}


		public void setMaxSpeed(Double maxSpeed) {
			this.maxSpeed = maxSpeed;
		}


		public Integer getOverSpeedTime() {
			return overSpeedTime;
		}


		public void setOverSpeedTime(Integer overSpeedTime) {
			this.overSpeedTime = overSpeedTime;
		}


		public Double getAverageSpeed() {
			return averageSpeed;
		}


		public void setAverageSpeed(Double averageSpeed) {
			this.averageSpeed = averageSpeed;
		}


		public Integer getDriveHourMeter() {
			return driveHourMeter;
		}


		public void setDriveHourMeter(Integer driveHourMeter) {
			this.driveHourMeter = driveHourMeter;
		}


		public Double getBatteryVoltage() {
			return batteryVoltage;
		}


		public void setBatteryVoltage(Double batteryVoltage) {
			this.batteryVoltage = batteryVoltage;
		}


		public Integer getCellId() {
			return cellId;
		}


		public void setCellId(Integer cellId) {
			this.cellId = cellId;
		}


		public Integer getMcc() {
			return mcc;
		}


		public void setMcc(Integer mcc) {
			this.mcc = mcc;
		}


		public Integer getMnc() {
			return mnc;
		}


		public void setMnc(Integer mnc) {
			this.mnc = mnc;
		}


		public Integer getRxLevel() {
			return rxLevel;
		}


		public void setRxLevel(Integer rxLevel) {
			this.rxLevel = rxLevel;
		}


		public String getAreaCode() {
			return areaCode;
		}


		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}


		public Integer getTimingAdvance() {
			return timingAdvance;
		}


		public void setTimingAdvance(Integer timingAdvance) {
			this.timingAdvance = timingAdvance;
		}
		
		




	}