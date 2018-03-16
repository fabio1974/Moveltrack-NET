package net.moveltrack.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import net.moveltrack.util.Utils;

@Entity
public class Iugu extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	private String codigoBarras;

	
	private String invoiceId;
	
    @Lob
    private byte[] codigoBarrasImagem;



	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	public byte[] getCodigoBarrasImagem() {
		return codigoBarrasImagem;
	}
	public void setCodigoBarrasImagem(byte[] codigoBarrasImagem) {
		this.codigoBarrasImagem = codigoBarrasImagem;
	}
	
	
	
	

	

}
