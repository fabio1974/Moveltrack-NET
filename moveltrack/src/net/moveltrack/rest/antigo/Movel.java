package net.moveltrack.rest.antigo;

public class Movel {
	
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	

	int id;
	String placa;
	String marcaModelo;
	String cor;
	String tipo;
	String imei;

	public long getProprietarioId() {
		return proprietarioId;
	}
	public void setProprietarioId(long proprietarioId) {
		this.proprietarioId = proprietarioId;
	}


	long proprietarioId;
	String celular;


	boolean ativo;
	
	boolean corte;
	public boolean isCorte() {
		return corte;
	}
	public void setCorte(boolean corte) {
		this.corte = corte;
	}



	
	@Override
	public String toString(){
		return placa+ "-" + marcaModelo + "-"+ cor;
	}

}
