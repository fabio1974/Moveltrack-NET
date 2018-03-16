package net.moveltrack.gateway;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.moveltrack.domain.Location;

/**
 * Servlet implementation class Tester
 */
public class Tester extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tester() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ReadingProtocolHandlerCRX1 p = new ReadingProtocolHandlerCRX1();
		
		byte[] alarme = {0x78, 0x78,0x25,0x16, 0x0f , 0x02 , 0x06 , 0x0d , 0x0d , 0x3b , (byte)0xc4 , 0x00 , 0x66 , (byte)0x88 , 0x39 , 0x04 , 0x21 , (byte)0xa1 , 0x28 
		, 0x00 , 0x58 , 0x30 , 0x09 , 0x02 , (byte)0xd4 , 0x0b , 0x4f , (byte)0xa1 , 0x00 , 0x62 , 0x21 , 0x50 , 0x02 , 0x04 , 0x02 , 0x02 , 0x00 , 0x0e , (byte)0xde ,(byte)0x9f , 0x0d , 0x0a};
		
		
		//p.readCommand(message);
		//p.serial1 = message[message.length-6]; 
		//p.serial2 = message[message.length-5];
		//p.printBuff(message);
		//p.printBuff(p.serial1,p.serial2);
		//byte[] hbeat = {0x78 ,0x78 ,0x0A ,0x13 ,0x44 ,0x06 ,0x04 ,0x00 ,0x02 ,0x01 ,0x46 ,(byte)0x92 ,(byte)0xB7 ,0x0D ,0x0A};
		Location loc = p.getLocationFromAlarmPackage(alarme);
		loc.setImei("358899050793028");
		p.enviaEmailDoAlarme(loc);
	}

}
