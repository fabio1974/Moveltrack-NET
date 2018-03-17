package net.moveltrack.gateway.globalstar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.moveltrack.dao.Location2Dao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Location2;
import net.moveltrack.gateway.utils.Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
      
    public GSServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    
    



    @Transactional
    private void saveMessage(Message message) {

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
