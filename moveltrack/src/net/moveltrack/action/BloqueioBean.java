package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.dao.SmsDao;
import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsStatus;
import net.moveltrack.domain.SmsTipo;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.util.Utils;


@Named
@ViewScoped
public class BloqueioBean implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

    @Inject 
    SmsDao smsDao;
	

	@PostConstruct
	public void init() {
		System.out.println(" Init ... "+ this.getClass().getName());
	}
	

	private Veiculo veiculo;
	private String celular;
	private String titulo1 = "Comando";
	private String conteudo1 = "---";
	private String conteudo2 = "---";
	private String classe1 = "gray-bg";
	private String classe2 = "gray-bg";
	private boolean stop = true;
	
	public void manda(){
		int x = 0;
		x++;
		System.out.println(x);
	}
	

	@Transactional
    public void bloquear(Veiculo veiculo){
    	setStop(false);
    	Sms sms = getSmsFromVeiculo(veiculo); 
	    if(sms!=null){
		    switch (sms.getStatus()) {
			case ESPERANDO:
				conteudo1 = "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
				break;
			case ESPERANDO_SOCKET:
				conteudo1 = "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
				break;
			case ENVIADO:
				conteudo1 = "Aguardando a resposta. O comando de "+sms.getTipo()+" já foi enviado!";
				break;
			default:
				break;
			}
	    }else{
		    sms = new Sms();
		    sms.setCelular(celular);
		    sms.setImei(veiculo.getEquipamento().getImei());
		    sms.setMensagem(Utils.getComandoBloqueia(veiculo.getEquipamento()));
		    sms.setTipo(SmsTipo.BLOQUEIO);
		    sms.setStatus(Utils.getStatusParaBloqueio(veiculo.getEquipamento()));
		    sms.setDataUltimaAtualizacao(new Date());
		    smsDao.salvar(sms);
		    conteudo2 =SmsTipo.BLOQUEIO + " enviado!";
	    }
	    updateStatus();
    }
    
    
    @Transactional
    public void desbloquear(Veiculo veiculo){
    	setStop(false);
    	Sms sms = getSmsFromVeiculo(veiculo);
	    if(sms!=null){
		    switch (sms.getStatus()) {
			case ESPERANDO:
				conteudo1 = "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
				break;
			case ESPERANDO_SOCKET:
				conteudo1=  "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
				break;
			case ENVIADO:
				conteudo1= "Aguardando a resposta. O comando de "+sms.getTipo()+" já foi enviado!";
				break;
			default:
				break;
			}
	    }else{
		    sms = new Sms();
		    sms.setCelular(celular);
		    sms.setImei(veiculo.getEquipamento().getImei());
		    sms.setMensagem(Utils.getComandoDesbloqueia(veiculo.getEquipamento()));
		    sms.setTipo(SmsTipo.DESBLOQUEIO);
		    sms.setStatus(Utils.getStatusParaBloqueio(veiculo.getEquipamento()));
		    sms.setDataUltimaAtualizacao(new Date());
		    smsDao.salvar(sms);
	    	conteudo2 =SmsTipo.DESBLOQUEIO + " enviado!";
	    }
	    updateStatus();
    }

    int count = 0;

	public void updateStatus(){
		System.out.println(celular + " passando pelo updateStatus inicio!");
		if(veiculo!=null && !stop){
			   Sms sms = getSmsFromVeiculo(veiculo);
			   titulo1=(sms!=null && sms.getStatus()==SmsStatus.RECEBIDO)?"Resposta":"Comando";
			   conteudo1=(sms==null?"---":(sms.getStatus()==SmsStatus.RECEBIDO)?sms.getMensagem():veiculo.getPlaca()+" - "+sms.getTipo().toString());
			   if(sms!=null){
				   if(sms.getTipo()==SmsTipo.BLOQUEIO && sms.getStatus()==SmsStatus.RECEBIDO)
				   		classe1 = "bg-danger";
				   if(sms.getTipo()==SmsTipo.DESBLOQUEIO && sms.getStatus()==SmsStatus.RECEBIDO)
				   		classe1 = "bg-success";
				   if(sms.getTipo()==SmsTipo.AVISO && sms.getStatus()==SmsStatus.RECEBIDO)
				   		classe1 = "bg-warning";
				   classe2 = (sms.getStatus()==SmsStatus.ESPERANDO||sms.getStatus()==SmsStatus.ESPERANDO_SOCKET)?"bg-info":(sms.getStatus()==SmsStatus.ENVIADO)?"bg-warning":classe1;
				   conteudo2 = sms.getStatus()==SmsStatus.ESPERANDO_SOCKET?"ESPERANDO...":sms.getStatus().toString();
			   }else{
				   conteudo2 = "---";
				   classe2 = "gray-bg";
				   count++;
				   setStop(count>=2);
			   }
			   
		   }else
			   setStop(true);
			   
	}
	
	
    private Sms getSmsFromVeiculo(Veiculo veiculo){
    	setVeiculo(veiculo);
		setCelular("+55" + veiculo.getEquipamento().getChip().getNumero().replaceAll("[^0-9]",""));
		return smsDao.getUltimoSmsDoChip(celular);
    }

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	   
	public String getClasse1() {
		return classe1;
	}

	public void setClasse1(String classe1) {
		this.classe1 = classe1;
	}

	public String getClasse2() {
		return classe2;
	}

	public void setClasse2(String classe2) {
		this.classe2 = classe2;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTitulo1() {
		return titulo1;
	}

	public void setTitulo1(String titulo1) {
		this.titulo1 = titulo1;
	}

	public String getConteudo1() {
		return conteudo1;
	}

	public void setConteudo1(String conteudo1) {
		this.conteudo1 = conteudo1;
	}


	public String getConteudo2() {
		return conteudo2;
	}

	public void setConteudo2(String conteudo2) {
		this.conteudo2 = conteudo2;
	}
	


	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		if(stop)
			System.err.println("PAROU O STATUS LOOP!!!");
		this.stop = stop;
	}

	public BloqueioBean() {

	}

}