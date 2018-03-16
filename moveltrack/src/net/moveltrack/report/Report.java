package net.moveltrack.report;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.security.LoginBean;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;



public abstract class Report extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9019262404410565109L;
	protected JRDataSource beanCollectionDataSource;
	protected Map<String, Object> parameters = new HashMap<String, Object>();
	protected String designPath;
	protected String fileName;
	
	protected Date inicio;
	protected Date fim;
	
	@Inject 
	GenericReportBean grb;
	

	
	@Inject
	LoginBean loginBean;
	
	public Report() {
		String className = this.getClass().getSimpleName();
		setFileName(className.substring(0, className.indexOf("Bean")));

	}

	@PostConstruct
	public void init() {
		System.out.println("init from SUPER "+this.getClass().getSimpleName());
	}
	
    public void pdf(String tipo) throws JRException, IOException {
    	setDesignPath();
        setParameters();	
        setJRBeanCollectionDataSource();
        setTotalParameters();

        JasperDesign jasperDesign = JRXmlLoader.load(designPath);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream;
        
        switch (tipo) {

        case "EXCEL":
	        httpServletResponse.addHeader("Content-disposition", "attachment; filename="+fileName+".xls");
	        servletOutputStream = httpServletResponse.getOutputStream();
	        JRXlsExporter exporter = new JRXlsExporter();
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(servletOutputStream));
	        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
	        configuration.setOnePagePerSheet(false);
	        configuration.setDetectCellType(true);
	        configuration.setCollapseRowSpan(false);
	        configuration.setIgnoreGraphics(true);
	        exporter.setConfiguration(configuration);
	        exporter.exportReport();
	        servletOutputStream.flush();
	        servletOutputStream.close();
	        FacesContext.getCurrentInstance().responseComplete();
			break;
			
		case "PDF":
			httpServletResponse.addHeader("Content-disposition", "attachment; filename="+fileName+".pdf");
	        servletOutputStream = httpServletResponse.getOutputStream();
	        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
	        servletOutputStream.flush();
	        servletOutputStream.close();
	        FacesContext.getCurrentInstance().responseComplete();
			break;

		default:
			break;
		}
        
    }
    
    
 
  
    
    
    
	protected abstract void setDesignPath();

	protected abstract void setParameters();
	
	protected abstract void setJRBeanCollectionDataSource();
	
	protected abstract void setTotalParameters();
	


	public Date getInicio() {
		return grb.getInicio();
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return grb.getFim();
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

}
