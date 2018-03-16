package net.moveltrack.gateway;

public class GpsCommand {
	
	//imei:359587010124900,tracker,0809231929,13554900601,F,112909.397,A,2234.4669,N,11354.3287,E,0.11,;
	

	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getX1() {
		return x1;
	}
	public void setX1(String x1) {
		this.x1 = x1;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getNs() {
		return ns;
	}
	public void setNs(String ns) {
		this.ns = ns;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getEw() {
		return ew;
	}
	public void setEw(String ew) {
		this.ew = ew;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getX2() {
		return x2;
	}
	public void setX2(String x2) {
		this.x2 = x2;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}	
	
	String imei;
	String type;
	String datetime;
	String x1;
	String f;
	String time;
	String a;
	String lat;
	String ns;
	String lon;
	String ew;
	String speed;
	String x2;
	String course;
	

	@Override
	public String toString(){
		return "Imei"+imei+" - Type"+type+" - DateTime"+datetime+" - Lat"+lat+" - NS-"+ns+" - Lon"+lon+" - EW"+ew+" - Speed" +speed+" - Time"+time+" - X1"+x1+" - x2"+x2+"<-";
	}
	
	

}
