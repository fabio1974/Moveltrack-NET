package net.moveltrack.security;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.Usuario;

public class LoginFilter implements Filter {
	
	@Inject
	LoginBean loginBean;
	
	@Inject
	UsuarioDao dao;
	 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
/*		Usuario userDB = dao.findByNomeUsuario("sincoplema");
		loginBean.setUsuario(userDB);
		loginBean.setLoggedIn(true);*/
    	
    	try{
    		if (loginBean == null || !loginBean.isLoggedIn()) {
    			((HttpServletResponse)response).sendRedirect(getUrl(request));
    		}
    		chain.doFilter(request, response);
    	}catch(Exception e){
    		e.printStackTrace();
    		try{
    			((HttpServletResponse)response).sendRedirect(getUrl(request));
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
    	}
    }
    
    private String getUrl(ServletRequest request){
    	String url = "";
    	try{
	    	String contextPath = ((HttpServletRequest)request).getContextPath(); //   /moveltrack
	    	String remoteAddress = ((HttpServletRequest)request).getRemoteAddr(); //  127.0.0.1
	    	if(contextPath!=null && remoteAddress!=null)
	    		url = "http://"+ (remoteAddress.contains("127.0.0.1")?"127.0.0.1:8080":"www.moveltrack.net") + contextPath + "/login.xhtml";
	    	else
	    		url =  "http://www.moveltrack.net/moveltrack/index.xhtml";
    	}catch(Exception e){
    		url =  "http://www.moveltrack.net/moveltrack/index.xhtml"; 
    	}
		System.out.println("...redirecting to "+url);
    	return url;
    }
 
    public void init(FilterConfig config) throws ServletException {
    }
 
    public void destroy() {
    }  
     
}