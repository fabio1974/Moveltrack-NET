package com.moveltrack.cartaoprograma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moveltrack.cartaoprograma.model.EscalaEquipe;
import com.moveltrack.cartaoprograma.rest.api.repository.EscalaEquipeRepository;

@Service
public class EscalaEquipeService {

	@Autowired
	EscalaEquipeRepository repository;

	@Transactional
	public void saveEscalaEquipe(List<EscalaEquipe> escalaEquipes) {
		if (!escalaEquipes.isEmpty())
			repository.deleteByAreaOrganizacional(escalaEquipes.get(0).getAreaOrganizacional());
		for (EscalaEquipe escalaEquipe : escalaEquipes) {
			repository.save(escalaEquipe);
		}
	}

}
