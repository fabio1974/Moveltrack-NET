package com.moveltrack.cartaoprograma.model.st500;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@JsonPropertyOrder({"id","data","address","latitude","longitude","alerta","alertaId","speed","course","satellite","gps","distance","volt","online","ignition"})
public class ST500ALT {
	
	
	static String ST500ALTStr = "ST500ALT;205778292;07;844;20180517;20:32:45;-03.806952;-038.497141;000.889;000.00;0;0;6704;14.23;1;0;33;001.702;59;4.1;2960";
	
	/*public static void main(String[] args) {
		
		ST500ALT teste = parse(ST500ALTStr.split(";"));
		teste.getAreaCode();
		
	}*/
		

	public static ST500ALT parse(String[] fields) {
			
			ST500ALT res = new ST500ALT();
			
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
			
			try {res.setAlertaId(Integer.parseInt(fields[16]));}catch(Exception e){}
			
			try {res.setDtcCnt(fields[17]);}catch(Exception e){}
			try {res.setDtc(fields[18]);}catch(Exception e){}
			try {res.setAverageSpeed(Double.parseDouble(fields[19]));}catch(Exception e){}
			
			try {res.setGeofenceId(fields[20]);}catch(Exception e){}
			try {res.setForca(fields[21]);}catch(Exception e){}
			
			try {res.setDriveHourMeter(Integer.parseInt(fields[22]));}catch(Exception e){}
			
			
			try {res.setBackupBatteryVoltage(Double.parseDouble(fields[23]));}catch(Exception e){}
			
			try {res.setMessage(Integer.parseInt(fields[24]));}catch(Exception e){}
			
			try {res.setCellId(Integer.parseInt(fields[25]));}catch(Exception e){}
			try {res.setMcc(Integer.parseInt(fields[26]));}catch(Exception e){}
			try {res.setMnc(Integer.parseInt(fields[27]));}catch(Exception e){}
			try {res.setRxLevel(Integer.parseInt(fields[28]));}catch(Exception e){}
			try {res.setAreaCode(fields[29]);}catch(Exception e){}
			try {res.setTimingAdvance(Integer.parseInt(fields[30]));}catch(Exception e){}
			res.setStoreDate(new Date());
			
			return res;

			
			
		}
	
		
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@JsonIgnore
		private String serial;
		
		@JsonIgnore
		private String model;
		
		@JsonIgnore
		private String swVersion;
		
		private Double latitude;
		private Double longitude;
		
		@JsonIgnore
		private Date deviceDate;
		
		@JsonIgnore
		private Date storeDate;
		private Double speed;
		private Double course;
		
		private Integer satellite;
		private Integer gps;
		private Integer distance;
		private Double volt;
		private Integer online;
		private Integer ignition;
		
		
		private Integer alertaId;
		
		@Transient
		@JsonSerialize
		private String address;
		
		@Transient
		@JsonProperty
		private String data;

		@Transient
		@JsonSerialize
		private String alerta;
		
		@JsonIgnore
		private String dtcCnt;
		
		@JsonIgnore
		private String dtc;

		@JsonIgnore
		private Double averageSpeed;
		
		@JsonIgnore
		private String geofenceId;
		
		@JsonIgnore
		private String forca;
		
		@JsonIgnore
		private Integer driveHourMeter;
		
		@JsonIgnore
		private Double backupBatteryVoltage;
		
		@JsonIgnore
		private Integer message;
		
		@JsonIgnore
		private Integer cellId;
		
		@JsonIgnore
		private Integer mcc;
		
		@JsonIgnore
		private Integer mnc;
		
		@JsonIgnore
		private Integer rxLevel;
		
		@JsonIgnore
		private String areaCode;
		
		@JsonIgnore
		private Integer timingAdvance;

		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		


		public String getData() {
			return data;
		}


		public void setData(String data) {
			this.data = data;
		}


		public String getAddress() {
			return address;
		}


		public void setAddress(String address) {
			this.address = address;
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


		public Integer getAlertaId() {
			return alertaId;
		}


		public void setAlertaId(Integer alertaId) {
			this.alertaId = alertaId;
		}


		public String getDtcCnt() {
			return dtcCnt;
		}


		public void setDtcCnt(String dtcCnt) {
			this.dtcCnt = dtcCnt;
		}


		public String getDtc() {
			return dtc;
		}


		public void setDtc(String dtc) {
			this.dtc = dtc;
		}


		public Double getAverageSpeed() {
			return averageSpeed;
		}


		public void setAverageSpeed(Double averageSpeed) {
			this.averageSpeed = averageSpeed;
		}


		public String getGeofenceId() {
			return geofenceId;
		}


		public void setGeofenceId(String geofenceId) {
			this.geofenceId = geofenceId;
		}


	


		public String getForca() {
			return forca;
		}


		public void setForca(String forca) {
			this.forca = forca;
		}


		public Integer getDriveHourMeter() {
			return driveHourMeter;
		}


		public void setDriveHourMeter(Integer driveHourMeter) {
			this.driveHourMeter = driveHourMeter;
		}


		public Double getBackupBatteryVoltage() {
			return backupBatteryVoltage;
		}


		public void setBackupBatteryVoltage(Double backupBatteryVoltage) {
			this.backupBatteryVoltage = backupBatteryVoltage;
		}


		public Integer getMessage() {
			return message;
		}


		public void setMessage(Integer message) {
			this.message = message;
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


		public String getAlerta() {
			return alerta;
		}


		public void setAlerta(String alerta) {
			this.alerta = alerta.toUpperCase();
		}


		public void setAlerta() {
			
			switch (this.alertaId) {
			case 1:
				setAlerta("Start driving faster than SPEED_LIMIT");
				break;
			
			case 2:
				setAlerta("Ended over speed condition");
				break;

			case 5:
				setAlerta("The vehicle went out from the geo-fence that has following ID");
				break;

			case 6:
				setAlerta("The vehicle entered into the geo- fence that has following ID");
				break;

			case 9:
				setAlerta("Enter to deep sleep mode");
				break;

			case 10:
				setAlerta("Exit from deep sleep mode");
				break;

			case 13:
				setAlerta("Backup battery error");
				break;

			case 14:
				setAlerta("Vehicle battery goes down to so low level");
				break;

			case 15:
				setAlerta("shocked");
				break;
				
			case 16:
				setAlerta("occurred some collision");
				break;
				
			case 33:
				setAlerta("new movement!");
				break;

			case 34:
				setAlerta("stopping!");
				break;
				
			
			case 40:
				setAlerta("Connected main power");
				break;

			case 41:
				setAlerta("Disconnected main power");
				break;

			case 44:
				setAlerta("Connected Backup battery");
				break;

			case 45:
				setAlerta("Disconnected Backup battery");
				break;

			case 46:
				setAlerta("Alert of fast acceleration from Driver Pattern Analysis");
				break;

			case 47:
				setAlerta("Alert of harsh brake from Driver Pattern Analysis");
				break;

			case 48:
				setAlerta("Alert of sharp turn from Driver Pattern Analysis");
				break;

		
				
			case 50:
				setAlerta("Jamming detected");
				break;

			case 68:
				setAlerta("Completed automatic Driver Pattern calibration");
				break;

			case 80:
				setAlerta("OBD Disconnected with ECU");
				break;

			case 81:
				setAlerta("OBD Connected with ECU");
				break;

			case 83:
				setAlerta("OBD DTC Detected");
				break;

			case 84:
				setAlerta("OBD DTC Cleared");
				break;

			case 85:
				setAlerta("Driving is started with High RPM");
				break;

			case 86:
				setAlerta("Driving is ended with High RPM");
				break;

			case 87:
				setAlerta("Alert of High Speed with Low RPM");
				break;

			case 88:
				setAlerta("Alert of Low Speed with High RPM");
				break;
				
				


			default:
				break;
			}
			
		}




	}