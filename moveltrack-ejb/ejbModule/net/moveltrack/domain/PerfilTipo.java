package net.moveltrack.domain;

public enum PerfilTipo {
    ADMINISTRADOR,
    FRANQUEADO,
    CLIENTE_PJ,
    CLIENTE_PF,
    CLIENTE_DEMONSTRACAO,
    VENDEDOR, 
    FINANCEIRO,
    GERENTE_GERAL,
    GERENTE_ADM,
    INSTALADOR;

	public boolean isCliente() {
		return this == CLIENTE_PF || this==CLIENTE_PJ;
	}
}
