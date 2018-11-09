package net.moveltrack.gateway.globalstar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXB;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

import net.moveltrack.dao.Location2Dao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.gateway.utils.Utils;
import net.moveltrack.util.MapaUtil;

@WebServlet("/GSServlet")
public class GSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String CUSTOMER_TOKEN_KEY="UsernameToken Username";
	public static final String NONCE_KEY="Nonce";
	public static final String CREATED_KEY="Created";
	public static final String PASSWORD_DIGEST_KEY="PasswordDigest";
	
	private static final String MY_CUSTOMER_TOKEN="b6f8c9d2-4dbf-3e9e-9f3c-e9fc10d68b4d";
	//private static final String MY_SECRET_TOKEN=" rt0lkUiAMrfsH3iBi767kpG1JdVuZd";
	//private Log log = LogFactory.getLog(this.getClass());

//	private BASE64Decoder decoder = new BASE64Decoder();
//	private BASE64Encoder encoder = new BASE64Encoder();
//	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	@Inject LocationDao locationDao;
	@Inject Location2Dao location2Dao;
	@Inject VeiculoDao veiculoDao;
      
    public GSServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//testaVelocidade();
    	response.sendError(401, "Unauthorized");
   	}


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
    		if (isHeadersValid(request)) {
    			//log.info("Valid WSSE Headers - request accepted");
    		} else {
    			//log.warn("Invalid WSSE Headers - request ignored");
    			response.setHeader("WWW-Authenticate", "WSSE realm=\"SpotService\", profile=\"UsernameToken\"");
    			response.sendError(401, "Unauthorized");
    			return;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	BufferedReader reader = new BufferedReader(request.getReader());
    	String line = null;
    	StringBuffer buffer = new StringBuffer();
    	while ((line = reader.readLine()) != null) {
    		buffer.append(line);
    	}
    	//log.info(buffer.toString());
    	MessageList messageList = JAXB.unmarshal(new StringReader(buffer.toString()),MessageList.class);
    	for (Message message : messageList.getMessage()) {
    		saveMessage(message);
		}
    	response.setContentType("text/xml");
    }
    
    
    
/*    public  void testaVelocidade() {
    	Veiculo veiculo = veiculoDao.findByEquipamento("000000003103985");
    	Calendar c1 = Calendar.getInstance(); c1.set(2018,10,6,0,0);
    	Calendar c2 = Calendar.getInstance(); c2.set(2018,10,6,14,0);
    	List<Object> locs = locationDao.getLocationsFromVeiculo(veiculo, c1.getTime(),c2.getTime());
    	
    	for (Object object : locs) {
			Location loc = MapaUtil.getLocationFromObject(object);
			Message m = buildMessageForTest(loc);
			saveMessage(m);
		}
    }*/

	/*private static Message buildMessageForTest(Location loc) {
		
		Message m = new Message();
    	m.setBatteryState("GOOD");
    	m.setEsn("0-3103985");
    	m.setEsnName("ESNname");
    	m.setLatitude((float)loc.getLatitude());
    	m.setLongitude((float)loc.getLongitude());
    	
    	GregorianCalendar c = new GregorianCalendar();
    	c.setTime(loc.getDateLocation());
    	XMLGregorianCalendar value;
		try {
			value = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			m.setTimestamp(value);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}*/



    @Transactional
    private  void saveMessage(Message message) {

    	/*System.out.println(message.getBatteryState());
		System.out.println(message.getEsn());
		System.out.println(message.getEsnName());
		System.out.println(message.getMessageDetail());
		System.out.println(message.getMessageType());
		System.out.println(message.getId());
		System.out.println(message.getLatitude());
		System.out.println(message.getLongitude());
		System.out.println(message.getTimeInGMTSecond());
		System.out.println(message.getTimestamp());*/
		
		Location location = new Location();
		location.setImei(StringUtils.leftPad(message.getEsn().replaceAll("-",""),15,"0"));
		location.setLatitude(message.getLatitude());
		location.setLongitude(message.getLongitude());
		location.setDateLocationInicio(Utils.toDate(message.getTimestamp()));
		location.setDateLocation(location.getDateLocationInicio());
		location.setComando(message.getMessageType());
		location.setBattery(message.getBatteryState());
		location.setVelocidade(locationDao.getGSSpeed(location));
		location.setMcc(0);
		location.setSatelites(0);
		location.setVersion(0);
		
		if(location.getVelocidade() < 200 && location.getLatitude() > -999) {
			locationDao.salvar(location);
			Location2 location2 = new Location2();
			location2.setImei(location.getImei());
			location2.setLatitude(location.getLatitude());
			location2.setLongitude(location.getLongitude());
			location2.setDateLocationInicio(location.getDateLocationInicio());
			location2.setDateLocation(location.getDateLocation());
			location2.setComando(location.getComando());
			location2.setBattery(location.getBattery());
			location2.setVelocidade(location.getVelocidade());
			location2Dao.salvar(location2);
			//System.out.println(location.getImei() + " - " + location.getDateLocation() + " - "+ location.getVelocidade());
		}
	}

	private boolean isHeadersValid(HttpServletRequest request) throws IOException {
    	String wsseHeaders = request.getHeader("X-WSSE");
    	String[] tokens = wsseHeaders.split("\", ");
    	Map<String, String> keyValueMap = new HashMap<String, String>();
    	for (int i=0; i< tokens.length; i++ ) {
    		String token = tokens[i];
    		String[] keyValue = token.split("=\"");
    		keyValueMap.put(keyValue[0], keyValue[1].replaceAll("\"", ""));
    	}
    	String customerToken = keyValueMap.get(CUSTOMER_TOKEN_KEY);
    	if (!MY_CUSTOMER_TOKEN.equals(customerToken)) {
    		/*System.out.println("Customer token did not match - token: " + MY_CUSTOMER_TOKEN + " header: " + customerToken);*/
    		return false;
    	}
    	return true;
    }
    

}
