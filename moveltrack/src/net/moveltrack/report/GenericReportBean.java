package net.moveltrack.report;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class GenericReportBean implements Serializable {
	private static final long serialVersionUID = -1;
	
	
	
	public GenericReportBean() {
		Calendar c = Calendar.getInstance();
		setFim(c.getTime());
		c.add(Calendar.MONTH, -3);
		c.set(Calendar.DAY_OF_MONTH,1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		setInicio(c.getTime());	
		
		c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		
		setAnteOntem(c.getTime());
		
		
		c = Calendar.getInstance();
		c.add(Calendar.MONTH,3);
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		c.set(Calendar.MILLISECOND,999);
		
		setFimExpiracao(c.getTime());
		
	
	}

	
	@PostConstruct
	public void init() {
		System.out.println("init from "+this.getClass().getName());
	}

	protected Date inicio;
	protected Date fim;
	protected Date fimExpiracao;
	protected Date anteOntem;

	public Date getAnteOntem() {
		return anteOntem;
	}

	public void setAnteOntem(Date anteOntem) {
		this.anteOntem = anteOntem;
	}

	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFim() {
		return fim;
	}
	public void setFim(Date fim) {
		Calendar c = Calendar.getInstance();
		c.setTime(fim);
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		c.set(Calendar.MILLISECOND,999);
		this.fim = c.getTime();
	}


	public Date getFimExpiracao() {
		return fimExpiracao;
	}


	public void setFimExpiracao(Date fimExpiracao) {
		this.fimExpiracao = fimExpiracao;
	}
	
	
	

}