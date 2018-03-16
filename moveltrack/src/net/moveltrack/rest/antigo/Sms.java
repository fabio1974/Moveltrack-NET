package net.moveltrack.rest.antigo;

import java.util.Date;



public class Sms {

    private String celular;

    private String mensagem;

    private String imei;

    private SmsTipo tipo;

    private SmsStatus status;

    private Date dataUltimaAtualizacao;

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public SmsTipo getTipo() {
		return tipo;
	}

	public void setTipo(SmsTipo tipo) {
		this.tipo = tipo;
	}

	public SmsStatus getStatus() {
		return status;
	}

	public void setStatus(SmsStatus status) {
		this.status = status;
	}

	public Date getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
    
    
    
    
}
