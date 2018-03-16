package net.moveltrack.firebase;

public class FirebaseMessageData {
	private String smallMessage;
	private String longMessage;
	private String parametro;
	private Modulos modulo;

	public String getSmallMessage() {
		return smallMessage;
	}

	public void setSmallMessage(String smallMessage) {
		this.smallMessage = smallMessage;
	}

	public String getParametro() {
		return parametro;
	}

	public String getLongMessage() {
		return longMessage;
	}

	public void setLongMessage(String longMessage) {
		this.longMessage = longMessage;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public Modulos getModulo() {
		return modulo;
	}

	public void setModulo(Modulos modulo) {
		this.modulo = modulo;
	}

	public enum Modulos {
		ALERTA("Alerta"), ALERTABRASIL("Alerta Brasil"), COMANDOS("Comandos"), EFV("EFV"), MENSAGENS(""), SPIA("SPIA");
		private String descricao;

		Modulos(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
}