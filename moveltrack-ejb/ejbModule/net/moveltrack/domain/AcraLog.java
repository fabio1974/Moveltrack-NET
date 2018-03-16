package net.moveltrack.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AcraLog extends BaseEntity{
	

	private static final long serialVersionUID = -7741564459486615018L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	private String REPORT_ID;	 
	private String APP_VERSION_CODE;	 
	private String APP_VERSION_NAME;	 
	private String PACKAGE_NAME;	 
	private String FILE_PATH;	 
	private String PHONE_MODEL;	 
	private String BRAND;	 
	private String PRODUCT;	 
	private String ANDROID_VERSION;	 
	private String BUILD;	 
	private String TOTAL_MEM_SIZE;	 
	private String AVAILABLE_MEM_SIZE;	 
	private String CUSTOM_DATA;	 
	private String IS_SILENT;	 
	private String STACK_TRACE;	 
	private String INITIAL_CONFIGURATION;	 
	private String CRASH_CONFIGURATION;	 
	private String DISPLAY;	 
	private String USER_COMMENT;	 
	private String USER_EMAIL;	 
	private String USER_APP_START_DATE;	 
	private String USER_CRASH_DATE;	 
	private String DUMPSYS_MEMINFO;	 
	private String LOGCAT;

	public String getREPORT_ID() {
		return REPORT_ID;
	}
	public void setREPORT_ID(String rEPORT_ID) {
		REPORT_ID = rEPORT_ID;
	}
	public String getAPP_VERSION_CODE() {
		return APP_VERSION_CODE;
	}
	public void setAPP_VERSION_CODE(String aPP_VERSION_CODE) {
		APP_VERSION_CODE = aPP_VERSION_CODE;
	}
	public String getAPP_VERSION_NAME() {
		return APP_VERSION_NAME;
	}
	public void setAPP_VERSION_NAME(String aPP_VERSION_NAME) {
		APP_VERSION_NAME = aPP_VERSION_NAME;
	}
	public String getPACKAGE_NAME() {
		return PACKAGE_NAME;
	}
	public void setPACKAGE_NAME(String pACKAGE_NAME) {
		PACKAGE_NAME = pACKAGE_NAME;
	}
	public String getFILE_PATH() {
		return FILE_PATH;
	}
	public void setFILE_PATH(String fILE_PATH) {
		FILE_PATH = fILE_PATH;
	}
	public String getPHONE_MODEL() {
		return PHONE_MODEL;
	}
	public void setPHONE_MODEL(String pHONE_MODEL) {
		PHONE_MODEL = pHONE_MODEL;
	}
	public String getBRAND() {
		return BRAND;
	}
	public void setBRAND(String bRAND) {
		BRAND = bRAND;
	}
	public String getPRODUCT() {
		return PRODUCT;
	}
	public void setPRODUCT(String pRODUCT) {
		PRODUCT = pRODUCT;
	}
	public String getANDROID_VERSION() {
		return ANDROID_VERSION;
	}
	public void setANDROID_VERSION(String aNDROID_VERSION) {
		ANDROID_VERSION = aNDROID_VERSION;
	}
	public String getBUILD() {
		return BUILD;
	}
	public void setBUILD(String bUILD) {
		BUILD = bUILD;
	}
	public String getTOTAL_MEM_SIZE() {
		return TOTAL_MEM_SIZE;
	}
	public void setTOTAL_MEM_SIZE(String tOTAL_MEM_SIZE) {
		TOTAL_MEM_SIZE = tOTAL_MEM_SIZE;
	}
	public String getAVAILABLE_MEM_SIZE() {
		return AVAILABLE_MEM_SIZE;
	}
	public void setAVAILABLE_MEM_SIZE(String aVAILABLE_MEM_SIZE) {
		AVAILABLE_MEM_SIZE = aVAILABLE_MEM_SIZE;
	}
	public String getCUSTOM_DATA() {
		return CUSTOM_DATA;
	}
	public void setCUSTOM_DATA(String cUSTOM_DATA) {
		CUSTOM_DATA = cUSTOM_DATA;
	}
	public String getIS_SILENT() {
		return IS_SILENT;
	}
	public void setIS_SILENT(String iS_SILENT) {
		IS_SILENT = iS_SILENT;
	}
	public String getSTACK_TRACE() {
		return STACK_TRACE;
	}
	public void setSTACK_TRACE(String sTACK_TRACE) {
		STACK_TRACE = sTACK_TRACE;
	}
	public String getINITIAL_CONFIGURATION() {
		return INITIAL_CONFIGURATION;
	}
	public void setINITIAL_CONFIGURATION(String iNITIAL_CONFIGURATION) {
		INITIAL_CONFIGURATION = iNITIAL_CONFIGURATION;
	}
	public String getCRASH_CONFIGURATION() {
		return CRASH_CONFIGURATION;
	}
	public void setCRASH_CONFIGURATION(String cRASH_CONFIGURATION) {
		CRASH_CONFIGURATION = cRASH_CONFIGURATION;
	}
	public String getDISPLAY() {
		return DISPLAY;
	}
	public void setDISPLAY(String dISPLAY) {
		DISPLAY = dISPLAY;
	}
	public String getUSER_COMMENT() {
		return USER_COMMENT;
	}
	public void setUSER_COMMENT(String uSER_COMMENT) {
		USER_COMMENT = uSER_COMMENT;
	}
	public String getUSER_EMAIL() {
		return USER_EMAIL;
	}
	public void setUSER_EMAIL(String uSER_EMAIL) {
		USER_EMAIL = uSER_EMAIL;
	}
	public String getUSER_APP_START_DATE() {
		return USER_APP_START_DATE;
	}
	public void setUSER_APP_START_DATE(String uSER_APP_START_DATE) {
		USER_APP_START_DATE = uSER_APP_START_DATE;
	}
	public String getUSER_CRASH_DATE() {
		return USER_CRASH_DATE;
	}
	public void setUSER_CRASH_DATE(String uSER_CRASH_DATE) {
		USER_CRASH_DATE = uSER_CRASH_DATE;
	}
	public String getDUMPSYS_MEMINFO() {
		return DUMPSYS_MEMINFO;
	}
	public void setDUMPSYS_MEMINFO(String dUMPSYS_MEMINFO) {
		DUMPSYS_MEMINFO = dUMPSYS_MEMINFO;
	}
	public String getLOGCAT() {
		return LOGCAT;
	}
	public void setLOGCAT(String lOGCAT) {
		LOGCAT = lOGCAT;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	 
	
	
	
}
