package net.moveltrack.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.moveltrack.util.DateAdapter;

@Entity
public class Sms extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4393855948758545071L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


    private String celular;

    @NotNull
    @Size(max = 500)
    private String mensagem;

    private String imei;

    @Enumerated(EnumType.STRING)
    private SmsTipo tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SmsStatus status;

    @NotNull
	@XmlJavaTypeAdapter(DateAdapter.class)
    @Temporal(TemporalType.TIMESTAMP)
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
