package com.moveltrack.reactbackend.service;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moveltrack.reactbackend.model.Acidente;
import com.moveltrack.reactbackend.model.ArmaApreendida;
import com.moveltrack.reactbackend.model.Crime;
import com.moveltrack.reactbackend.model.DrogaApreendida;
import com.moveltrack.reactbackend.model.EquipamentoDeApoio;
import com.moveltrack.reactbackend.model.Operacao;
import com.moveltrack.reactbackend.model.PessoaApreendida;
import com.moveltrack.reactbackend.rest.api.repository.AcidenteRepository;
import com.moveltrack.reactbackend.rest.api.repository.ArmaApreendidaRepository;
import com.moveltrack.reactbackend.rest.api.repository.CrimeRepository;
import com.moveltrack.reactbackend.rest.api.repository.DrogaApreendidaRepository;
import com.moveltrack.reactbackend.rest.api.repository.EquipamentoDeApoioRepository;
import com.moveltrack.reactbackend.rest.api.repository.OperacaoRepository;
import com.moveltrack.reactbackend.rest.api.repository.PessoaApreendidaRepository;


@Service
public class OperacaoService {
	
	@Autowired OperacaoRepository operacaoRepository;
	@Autowired EquipamentoDeApoioRepository equipamentoDeApoioRepository;
	@Autowired ArmaApreendidaRepository armaApreendidaRepository;
	@Autowired PessoaApreendidaRepository pessoaApreendidaRepository;
	@Autowired DrogaApreendidaRepository drogaApreendidaRepository;
	@Autowired CrimeRepository crimeRepository;
	@Autowired AcidenteRepository acidenteRepository;
	
	
	@Transactional
	public Operacao insert(Operacao operacao) {
		operacaoRepository.save(operacao);

		for (EquipamentoDeApoio item : operacao.getEquipamentoDeApoios()) {
    		item.setOperacaoId(operacao.getId());
			equipamentoDeApoioRepository.save(item);
		}

		for (ArmaApreendida item : operacao.getArmaApreendidas()) {
			item.setOperacao(operacao);
			armaApreendidaRepository.save(item);
		}
		
		for (PessoaApreendida item : operacao.getPessoaApreendidas()) { 
			item.setOperacao(operacao);
			pessoaApreendidaRepository.save(item);
		}	
		
		for (DrogaApreendida item : operacao.getDrogaApreendidas()) {
			item.setOperacao(operacao);
			drogaApreendidaRepository.save(item);
		}	
		
		for (Crime item : operacao.getCrimes()) {
			item.setOperacao(operacao);
			crimeRepository.save(item);
		}	
		
		for (Acidente item : operacao.getAcidentes()) { 
			item.setOperacao(operacao);
			acidenteRepository.save(item);
		}	
		
    	return operacao;
	}
	
	
	@Transactional
	public Operacao update(Operacao operacao) {

		deleteEquipamentoDeApoio(operacao.getId());
		for (EquipamentoDeApoio item : operacao.getEquipamentoDeApoios()) {
    		item.setOperacaoId(operacao.getId());
			equipamentoDeApoioRepository.save(item);
		}

		armaApreendidaRepository.deleteByOperacao(operacao);
		pessoaApreendidaRepository.deleteByOperacao(operacao);
		drogaApreendidaRepository.deleteByOperacao(operacao);
		crimeRepository.deleteByOperacao(operacao);
		acidenteRepository.deleteByOperacao(operacao);
		
		for (ArmaApreendida item : operacao.getArmaApreendidas()) 
			item.setOperacao(operacao);
		
		for (PessoaApreendida item : operacao.getPessoaApreendidas()) 
			item.setOperacao(operacao);
		
		for (DrogaApreendida item : operacao.getDrogaApreendidas()) 
			item.setOperacao(operacao);
		
		for (Crime item : operacao.getCrimes()) 
			item.setOperacao(operacao);
		
		for (Acidente item : operacao.getAcidentes()) 
			item.setOperacao(operacao);
		
		operacaoRepository.save(operacao);
    	
    	return operacao;
	}
	
	
	
	
	
	
	
	@Transactional
	public void delete(Integer operacaoId) {
		deleteEquipamentoDeApoio(operacaoId);
    	operacaoRepository.deleteById(operacaoId);
	}
	
	
	@PersistenceContext
    EntityManager entityManager;
	private void deleteEquipamentoDeApoio(Integer operacaoId) {
		try {
			entityManager.createNativeQuery("delete from operacao_equipamento_de_apoios where operacao_id="+operacaoId).executeUpdate();
			entityManager.createNativeQuery("delete from equipamento_de_apoio where operacao_id="+ operacaoId).executeUpdate();
			equipamentoDeApoioRepository.deleteByOperacaoId(operacaoId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

}
