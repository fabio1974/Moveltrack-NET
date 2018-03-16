package net.moveltrack.cortacorrente;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.SmsDao;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsTipo;
import net.moveltrack.util.Utils;

@Named
public class CortaCorrente {
	

    String celular;
    Equipamento equipamento;
    

    public CortaCorrente(Equipamento equipamento) {
		super();
		this.equipamento = equipamento;
		celular = equipamento.getChip().getNumero();
		celular = "+55" + celular.replaceAll("[^0-9]","");

	}
    

    public String bloqueia() {
    	Sms sms = smsDao.getUltimoSmsDoChip(celular);
	    if(sms!=null){
		    switch (sms.getStatus()) {
			case ESPERANDO:
					return "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
			case ESPERANDO_SOCKET:
					return  "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
			case ENVIADO:
					return "Aguardando a resposta. O comando de "+sms.getTipo()+" já foi enviado!";
			default:
				break;
			}
	    }
	    
	    Sms sms2 = new Sms();
	    sms2.setCelular(celular);
	    sms2.setImei(equipamento.getImei());
	    sms2.setMensagem(Utils.getComandoBloqueia(equipamento));
	    sms2.setTipo(SmsTipo.BLOQUEIO);
	    sms2.setStatus(Utils.getStatusParaBloqueio(equipamento));
	    sms2.setDataUltimaAtualizacao(new Date());
	    smsDao.salvar(sms2);
	    return "";
	}	    
    
    
    public String desbloqueia() {
    	Sms sms = smsDao.getUltimoSmsDoChip(celular);
	    if(sms!=null){
		    switch (sms.getStatus()) {
			case ESPERANDO:
					return "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
			case ESPERANDO_SOCKET:
					return  "Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!";
			case ENVIADO:
					return "Aguardando a resposta. O comando de "+sms.getTipo()+" já foi enviado!";
			default:
				break;
			}
	    }
	    
	    Sms sms2 = new Sms();
	    sms2.setCelular(celular);
	    sms2.setImei(equipamento.getImei());
	    sms2.setMensagem(Utils.getComandoDesbloqueia(equipamento));
	    sms2.setTipo(SmsTipo.DESBLOQUEIO);
	    sms2.setStatus(Utils.getStatusParaBloqueio(equipamento));
	    sms2.setDataUltimaAtualizacao(new Date());
	    smsDao.salvar(sms2);
	    return "";
	}	    
    
    
    @Inject 
    SmsDao smsDao;
    
	public void restabelece() {

	/*	Sms sms = smsDao.getUltimoSmsDoChip(celular);
	    if(sms!=null){
		    switch (sms.getStatus()) {
			case ESPERANDO:
					showMessage("Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!");
					return;
					
			case ESPERANDO_SOCKET:
				showMessage("Aguarde, o comando de "+sms.getTipo()+" será enviado em instantes!");
				return;
					
			case ENVIADO:
					showMessage("Aguardando a resposta. O comando de "+sms.getTipo()+" já foi enviado!");
					return;
			default:
				break;
			}
	    }
	    */
/*	    app.getMainWindow().addWindow(new YesNoDialog("Essa operação irá restabelecer o motor do seu veículo.","Deseja prosseguir?", new YesNoDialog.Callback() {
            public void onDialogResult(boolean sim) {
               	if(sim){
					Sms sms = new Sms();
					sms.setCelular(celular);
					sms.setImei(equipamento.getImei());
					sms.setMensagem(Utils.getComandoDesbloqueia(equipamento));
					sms.setTipo(SmsTipo.DESBLOQUEIO);
					sms.setStatus(Utils.getStatusParaBloqueio(equipamento));
  					sms.setDataUltimaAtualizacao(new Date());
  					sms.persist();
               	}
            }
        }));	
*/	}
	
	
 


	public void showMessage(String message) {
/*    	Window w = new MessageWindow("Aviso!",message,true);
        app.getMainWindow().addWindow(w);
*/    }	

	
    private String traduzMensagem(String mensagem){
    	if(mensagem.contains("Stop engine fail!")){
    		return "O corte de combustível falhou!";
    	}else if(mensagem.contains("Stop engine Succeed!")){
    		return "Corte de combustível bem sucedido!";
    	}else if(mensagem.contains("Resume engine fail!")){
    		return "O restabelecimento do combustível falhou!";
    	}else if(mensagem.contains("Resume engine Succeed!")){
    		return "Restabelecimento de combustível bem sucedido!";
    	}else
    		return mensagem;
    }
}
