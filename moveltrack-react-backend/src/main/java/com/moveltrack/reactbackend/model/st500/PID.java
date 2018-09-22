package com.moveltrack.reactbackend.model.st500;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;





@Entity
@JsonPropertyOrder({"id", "#PID", "Descrição", "Valor", "Unidade"})
public class PID {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="serial")
	private Long id;
	
	@JsonIgnore
	private byte hex;
	
	@Transient
	@JsonSerialize

	private String hexStr;

    @JsonGetter("#PID")
	public String getHexStr() {
		return hexStr;
	}
	

	public void setHexStr(String hexStr) {
		this.hexStr = hexStr;
	}


	private String value;
	
	
	@Transient
	@JsonSerialize
	private String descryption;
	

	
	@Transient
	@JsonSerialize
	private String unit;
	
			
	public byte getHex() {
		return hex;
	}
	public void setHex(byte hex) {
		this.hex = hex;
	}
	
	@JsonGetter("Descrição")
	public String getDescryption() {
		return descryption;
	}
	public void setDescryption(String descryption) {
		this.descryption = descryption;
	}
	
	@JsonGetter("Valor")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@JsonGetter("Unidade")
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	


	
	public static PID getPid(Byte hex) {
		PID pid = new PID();
		pid.setHex(hex);
		
		switch (hex) {
		
		case 0x01:
			pid.setDescryption("Monitors/MIL status/DTC count");
			pid.setUnit(null);
			break;
			
		case 0x02:
			pid.setDescryption("Freeze DTC");
			pid.setUnit(null);
			break;

		case 0x03:
			pid.setDescryption("Fuel system status");
			pid.setUnit(null);
			break;

		case 0x04:
			pid.setDescryption("Calculated engine load value");
			pid.setUnit("%");
			break;

		case 0x05:
			pid.setDescryption("Engine coolant temperature");
			pid.setUnit("°C");
			break;

		case 0x06:
			pid.setDescryption("Short term fuel % trim—Bank 1");
			pid.setUnit("%");
			break;

		case 0x07:
			pid.setDescryption("Long term fuel % trim—Bank 1");
			pid.setUnit("%");
			break;

		case 0x08:
			pid.setDescryption("Short term fuel % trim—Bank 2");
			pid.setUnit("%");
			break;

		case 0x09:
			pid.setDescryption("Long term fuel % trim—Bank 2");
			pid.setUnit("%");
			break;

		case 0x0a:
			pid.setDescryption("Fuel pressure");
			pid.setUnit("kPa (gauge)");
			break;

		case 0x0b:
			pid.setDescryption("Intake manifold absolute pressure");
			pid.setUnit("kPa (absolute)");
			break;

		case 0x0c:
			pid.setDescryption("Engine RPM");
			pid.setUnit("rpm");
			break;

		case 0x0d:
			pid.setDescryption("Vehicle speed");
			pid.setUnit("km/h");
			break;

		case 0x0e:
			pid.setDescryption("Timing advance");
			pid.setUnit("° relative to #1 cylinder");
			break;

		case 0x0f:
			pid.setDescryption("Intake air temperature");
			pid.setUnit("°C");
			break;

		case 0x10:
			pid.setDescryption("MAF air flow rate");
			pid.setUnit("grams/sec");
			break;

		case 0x11:
			pid.setDescryption("Throttle position");
			pid.setUnit("%");
			break;

		case 0x12:
			pid.setDescryption("Commanded secondary air status");
			pid.setUnit(null);
			break;

		case 0x13:
			pid.setDescryption("Oxygen sensors present");
			pid.setUnit(null);
			break;

		case 0x14:
			pid.setDescryption("O2S1:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;
			
		case 0x15:
			pid.setDescryption("O2S2:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;

		case 0x16:
			pid.setDescryption("O2S3:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;

		case 0x17:
			pid.setDescryption("O2S4:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;
			
		case 0x18:
			pid.setDescryption("O2S5:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;
			
		case 0x19:
			pid.setDescryption("O2S6:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;

		case 0x1a:
			pid.setDescryption("O2S7:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;

		case 0x1b:
			pid.setDescryption("O2S8:Oxygen sensor voltage,Short term fuel trim");
			pid.setUnit("Volts,%");
			break;
			
		case 0x1c:
			pid.setDescryption("OBD Type");
			pid.setUnit(null);
			break;

		case 0x1d:
			pid.setDescryption("Oxygen sensors present");
			pid.setUnit(null);
			break;

		case 0x1e:
			pid.setDescryption("Auxiliary input status");
			pid.setUnit(null);
			break;

		case 0x1f:
			pid.setDescryption("Run time since engine start");
			pid.setUnit("seconds");
			break;

		case 0x20:
			pid.setDescryption("PIDs supported [21-40]");
			pid.setUnit(null);
			break;

		case 0x21:
			pid.setDescryption("Distance traveled with malfunction indicator lamp (MIL) on");
			pid.setUnit("km");
			break;

		case 0x22:
			pid.setDescryption("Fuel Rail Pressure (relative to manifold vacuum)");
			pid.setUnit("kPa");
			break;

		case 0x23:
			pid.setDescryption("Fuel Rail Pressure (diesel, or gasoline direct inject)");
			pid.setUnit("kPa(gauge)");
			break;

		case 0x24:
			pid.setDescryption("O2S1_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;
			
		case 0x25:
			pid.setDescryption("O2S2_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;

		case 0x26:
			pid.setDescryption("O2S3_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;

		case 0x27:
			pid.setDescryption("O2S4_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;
			
		case 0x28:
			pid.setDescryption("O2S5_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;
			
		case 0x29:
			pid.setDescryption("O2S6_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;

		case 0x2a:
			pid.setDescryption("O2S7_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;

		case 0x2b:
			pid.setDescryption("O2S8_WR_lambda(1):Equivalence Ratio, Voltage");
			pid.setUnit("N/A V");
			break;				
			

		case 0x2c:
			pid.setDescryption("Commanded EGR");
			pid.setUnit("%");
			break;

		case 0x2d:
			pid.setDescryption("EGR Error");
			pid.setUnit("%");
			break;

		case 0x2e:
			pid.setDescryption("Commanded evaporative purge");
			pid.setUnit("%");
			break;

		case 0x2f:
			pid.setDescryption("Fuel Level Input");
			pid.setUnit("%");
			break;

		case 0x30:
			pid.setDescryption("# of warm-ups since codes cleared");
			pid.setUnit("N/A");
			break;

		case 0x31:
			pid.setDescryption("Distance traveled since codes cleared");
			pid.setUnit("km");
			break;

		case 0x32:
			pid.setDescryption("Evap. System Vapor Pressure");
			pid.setUnit("Pa");
			break;

		case 0x33:
			pid.setDescryption("Barometric pressure");
			pid.setUnit("kPa(Absolute)");
			break;

		case 0x34:
			pid.setDescryption("O2S1_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;
			
		case 0x35:
			pid.setDescryption("O2S2_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;

		case 0x36:
			pid.setDescryption("O2S3_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;

		case 0x37:
			pid.setDescryption("O2S4_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;
			
		case 0x38:
			pid.setDescryption("O2S5_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;
			
		case 0x39:
			pid.setDescryption("O2S6_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;

		case 0x3a:
			pid.setDescryption("O2S7_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;

		case 0x3b:
			pid.setDescryption("O2S8_WR_lambda(1):Equivalence Ratio, Current");
			pid.setUnit("N/A mA");
			break;
			
		case 0x3c:
			pid.setDescryption("Catalyst Temperature Bank 1, Sensor 1");
			pid.setUnit("°C");
			break;

		case 0x3d:
			pid.setDescryption("Catalyst Temperature Bank 2, Sensor 1");
			pid.setUnit("°C");
			break;

		case 0x3e:
			pid.setDescryption("Catalyst Temperature Bank 1, Sensor 2");
			pid.setUnit("°C");
			break;

		case 0x3f:
			pid.setDescryption("Catalyst Temperature Bank 2, Sensor 2");
			pid.setUnit("°C");
			break;

		case 0x40:
			pid.setDescryption("PIDs supported [41 - 60]");
			pid.setUnit(null);
			break;

		case 0x41:
			pid.setDescryption("Monitor status this drive cycle");
			pid.setUnit(null);
			break;

		case 0x42:
			pid.setDescryption("Control module voltage");
			pid.setUnit("V");
			break;

		case 0x43:
			pid.setDescryption("Absolute load value");
			pid.setUnit("%");
			break;

		case 0x44:
			pid.setDescryption("Fuel/Air commanded equivalence ratio");
			pid.setUnit("N/A");
			break;
			
		case 0x45:
			pid.setDescryption("Relative throttle position");
			pid.setUnit("%");
			break;

		case 0x46:
			pid.setDescryption("Ambient air temperature");
			pid.setUnit("°C");
			break;

		case 0x47:
			pid.setDescryption("Accelerator pedal position B");
			pid.setUnit("%");
			break;
			
		case 0x48:
			pid.setDescryption("Accelerator pedal position C");
			pid.setUnit("%");
			break;
			
		case 0x49:
			pid.setDescryption("Accelerator pedal position D");
			pid.setUnit("%");
			break;

		case 0x4a:
			pid.setDescryption("Accelerator pedal position E");
			pid.setUnit("%");
			break;

		case 0x4b:
			pid.setDescryption("Accelerator pedal position F");
			pid.setUnit("%");
			break;				
			
			
		case 0x4c:
			pid.setDescryption("Commanded throttle actuator");
			pid.setUnit("%");
			break;

		case 0x4d:
			pid.setDescryption("Time run with MIL on");
			pid.setUnit("minutes");
			break;

		case 0x4e:
			pid.setDescryption("Time since trouble codes cleared");
			pid.setUnit("minutes");
			break;

		case 0x4f:
			pid.setDescryption("Maximum value for equivalence ratio,oxygen sensor voltage,oxygen sensor current,intake manifold absolute pressure");
			pid.setUnit("kPa");
			break;

		case 0x50:
			pid.setDescryption("Maximum value for air flow rate from mass air flow sensor");
			pid.setUnit("g/s");
			break;

		case 0x51:
			pid.setDescryption("Fuel Type");
			pid.setUnit(null);
			break;

		case 0x52:
			pid.setDescryption("Ethanol fuel %");
			pid.setUnit("%");
			break;

		case 0x53:
			pid.setDescryption("Absolute Evap system Vapor Pressure");
			pid.setUnit("kPa");
			break;

		case 0x54:
			pid.setDescryption("Evap system vapor pressure");
			pid.setUnit("Pa");
			break;
			
		case 0x55:
			pid.setDescryption("Short term secondary oxygen sensor trim bank 1 and bank 3");
			pid.setUnit("%");
			break;

		case 0x56:
			pid.setDescryption("Long term secondary oxygen sensor trim bank 1 and bank 3");
			pid.setUnit("%");
			break;

		case 0x57:
			pid.setDescryption("Short term secondary oxygen sensor trim bank 2 and bank 4");
			pid.setUnit("%");
			break;
			
		case 0x58:
			pid.setDescryption("Long term secondary oxygen sensor trim bank 2 and bank 4");
			pid.setUnit("%");
			break;
			
		case 0x59:
			pid.setDescryption("Fuel rail pressure (absolute)");
			pid.setUnit("kPa");
			break;

		case 0x5a:
			pid.setDescryption("Relative accelerator pedal position");
			pid.setUnit("%");
			break;

		case 0x5b:
			pid.setDescryption("Hybrid battery pack remaining life");
			pid.setUnit("%");
			break;				
			
		case 0x5c:
			pid.setDescryption("Engine oil temperature");
			pid.setUnit("°C");
			break;

		case 0x5d:
			pid.setDescryption("Fuel injection timing");
			pid.setUnit("°");
			break;

		case 0x5e:
			pid.setDescryption("Engine fuel rate");
			pid.setUnit("l/h");
			break;

		case 0x5f:
			pid.setDescryption("Emission requirements to which vehicle is designed");
			pid.setUnit(null);
			break;

		case 0x60:
			pid.setDescryption("PIDs supported [61 - 80]");
			pid.setUnit(null);
			break;

		case 0x61:
			pid.setDescryption("Driver's demand engine - percent torque");
			pid.setUnit("%");
			break;

		case 0x62:
			pid.setDescryption("Actual engine - percent torque");
			pid.setUnit("%");
			break;

		case 0x63:
			pid.setDescryption("Engine reference torque");
			pid.setUnit("Nm");
			break;

		default:
			pid.setDescryption("UNKNOW");
			pid.setUnit("UNKNOW");
			break;
		}// TODO Auto-generated method stub

		return pid;
	}
	

}
