package net.moveltrack.rest;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import net.moveltrack.domain.Perfil;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Veiculo;

public class GsonExcludeStrategy implements ExclusionStrategy {

	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		return (
				
				(f.getDeclaringClass() == Perfil.class && f.getName().equals("permissoes"))
				
				|| (f.getDeclaringClass() == Pessoa.class && (f.getName().equals("municipio")
															||f.getName().equals("alterador")
															||f.getName().equals("cep")
															||f.getName().equals("ultimaAlteracao")
															||f.getName().equals("dataCadastro")
															||f.getName().equals("numero")
															||f.getName().equals("endereco")
															||f.getName().equals("complemento")
															||f.getName().equals("bairro")))
				
			
				|| (f.getDeclaringClass() == Veiculo.class && (f.getName().equals("descricao")
						||f.getName().equals("dataInstalacao")
						||f.getName().equals("dataDesinstalacao")
						||f.getName().equals("ultimaAlteracao")
						||f.getName().equals("instalador")
						||f.getName().equals("contrato")
						||f.getName().equals("consumoCombustivel")
						||f.getName().equals("alterador")))
				
				
				
				);
	}
}