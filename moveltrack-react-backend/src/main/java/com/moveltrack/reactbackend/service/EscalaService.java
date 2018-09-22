package com.moveltrack.reactbackend.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moveltrack.reactbackend.model.AreaOrganizacional;
import com.moveltrack.reactbackend.model.Escala;
import com.moveltrack.reactbackend.model.EscalaEquipe;
import com.moveltrack.reactbackend.model.EscalaEquipeSigla;
import com.moveltrack.reactbackend.model.EscalaTipo;
import com.moveltrack.reactbackend.model.Pessoa;
import com.moveltrack.reactbackend.model.Plantao;
import com.moveltrack.reactbackend.rest.api.repository.AreaOrganizacionalRepository;
import com.moveltrack.reactbackend.rest.api.repository.EscalaEquipeRepository;
import com.moveltrack.reactbackend.rest.api.repository.EscalaRepository;
import com.moveltrack.reactbackend.rest.api.repository.EscalaTipoRepository;
import com.moveltrack.reactbackend.rest.api.repository.PlantaoRepository;

@Service
public class EscalaService {

	@Autowired
	EscalaRepository repository;

	@Autowired
	EscalaEquipeRepository escalaEquipeRepository;

	@Autowired
	AreaOrganizacionalRepository areaOrganizacionalRepository;
	
	@Autowired 
	EscalaTipoRepository escalaTipoRepository;

	Calendar ultimoDiaDoMes;

	@Transactional
	public void saveEscala(Escala escala) {

		Optional<AreaOrganizacional> o = areaOrganizacionalRepository.findById(escala.getAreaOrganizacional().getId());

		escala.setAreaOrganizacional(o.isPresent() ? o.get() : null);

		repository.save(escala);

		EscalaTipo escalaTipo = escala.getEscalaTipo();
		Optional<EscalaTipo> op = escalaTipoRepository.findById(escalaTipo.getId());
		escalaTipo = op.isPresent()?op.get():null;

		Calendar inicio = Calendar.getInstance();
		inicio.set(escala.getAno(), escala.getMes() - 1, 1, escala.getRendicaoHora(),	escala.getRendicaoMinuto());
		inicio.set(Calendar.SECOND, 0);
		inicio.set(Calendar.MILLISECOND, 0);

		Integer subCiclo1 = escalaTipo.getHorasDeTrabalho1() + escalaTipo.getHorasDeDescanso1();
		Integer subCiclo2 = escalaTipo.getHorasDeTrabalho2() + escalaTipo.getHorasDeDescanso2();

		Integer ciclo = subCiclo1 + subCiclo2;

		this.ultimoDiaDoMes = getUltimoDiaDoMes(escala);

		System.out.println("Inicio: " + calendarToString(inicio));
		System.out.println("Ultimo dia do mes: " + calendarToString(ultimoDiaDoMes));

		while (!inicio.after(ultimoDiaDoMes)) {

			geraPlantoesDaEquipe(inicio, escala, EscalaEquipeSigla.TURNO1, escalaTipo, subCiclo1, subCiclo2);

			geraPlantoesDaEquipe(inicio, escala, EscalaEquipeSigla.TURNO2, escalaTipo, subCiclo1, subCiclo2);

			geraPlantoesDaEquipe(inicio, escala, EscalaEquipeSigla.TURNO3, escalaTipo, subCiclo1, subCiclo2);

			geraPlantoesDaEquipe(inicio, escala, EscalaEquipeSigla.TURNO4, escalaTipo, subCiclo1, subCiclo2);

			inicio.add(Calendar.HOUR, ciclo);

			System.out.println("Inicio: " + calendarToString(inicio));
		}
	}

	private String calendarToString(Calendar c) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(c.getTime());
	}

	private void geraPlantoesDaEquipe(Calendar inicio, Escala escala, EscalaEquipeSigla sigla, EscalaTipo escalaTipo,Integer subCiclo1, Integer subCiclo2) {
		List<EscalaEquipe> equipe = escalaEquipeRepository.findAllByAreaOrganizacionalAndSigla(escala.getAreaOrganizacional(), sigla);
		for (EscalaEquipe escalaEquipe : equipe) {
			geraPlantoesPorPessoa(inicio, escala, escalaEquipe.getPessoa(), sigla, escalaTipo, subCiclo1, subCiclo2);
		}
	}

	@Autowired
	PlantaoRepository plantaoRepository;

	private void geraPlantoesPorPessoa(Calendar inicio, Escala escala, Pessoa pessoa, EscalaEquipeSigla sigla,
			EscalaTipo escalaTipo, Integer subCiclo1, Integer subCiclo2) {

		Plantao p1 = new Plantao();
		p1.setEscala(escala);
		p1.setPessoa(pessoa);

		Date inicioExpediente = inicio.getTime();

		switch (sigla) {
		case TURNO1:
			p1.setInicioExpediente(inicioExpediente);
			break;

		case TURNO2:
			p1.setInicioExpediente(addHourToDate(inicioExpediente, escalaTipo.getHorasDeTrabalho1()));
			break;

		case TURNO3:
			p1.setInicioExpediente(addHourToDate(inicioExpediente, 2 * escalaTipo.getHorasDeTrabalho1()));
			break;

		case TURNO4:
			p1.setInicioExpediente(addHourToDate(inicioExpediente, 3 * escalaTipo.getHorasDeTrabalho1()));
			break;

		default:
			break;
		}

		p1.setSigla(sigla);
		p1.setFimExpediente(addHourToDate(p1.getInicioExpediente(), escalaTipo.getHorasDeTrabalho1()));
		p1.setFimExpediente(subSecondFromDate(p1.getFimExpediente()));

		if (p1.getInicioExpediente().before(this.ultimoDiaDoMes.getTime()))
			plantaoRepository.save(p1);

		if (subCiclo2 > 0) {
			Plantao p2 = new Plantao();
			p2.setEscala(escala);
			p2.setPessoa(pessoa);

			Date inicioExpediente2 = addHourToDate(inicioExpediente, subCiclo1);

			switch (sigla) {
			case TURNO1:
				p2.setInicioExpediente(inicioExpediente2);
				break;

			case TURNO2:
				p2.setInicioExpediente(addHourToDate(inicioExpediente2, escalaTipo.getHorasDeTrabalho2()));
				break;

			case TURNO3:
				p2.setInicioExpediente(addHourToDate(inicioExpediente2, 2 * escalaTipo.getHorasDeTrabalho2()));
				break;

			case TURNO4:
				p2.setInicioExpediente(addHourToDate(inicioExpediente2, 3 * escalaTipo.getHorasDeTrabalho2()));
				break;

			default:
				break;
			}

			p2.setSigla(sigla);
			p2.setFimExpediente(addHourToDate(p2.getInicioExpediente(), escalaTipo.getHorasDeTrabalho2()));
			p2.setFimExpediente(subSecondFromDate(p2.getFimExpediente()));
			if (p2.getInicioExpediente().before(this.ultimoDiaDoMes.getTime()))
				plantaoRepository.save(p2);
		}
	}

	private Calendar getUltimoDiaDoMes(Escala escala) {
		Calendar ultimoDiaDoMes = Calendar.getInstance();
		ultimoDiaDoMes.set(Calendar.MONTH, escala.getMes() - 1);
		ultimoDiaDoMes.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes.getActualMaximum(Calendar.DAY_OF_MONTH));
		ultimoDiaDoMes.set(Calendar.HOUR_OF_DAY, 23);
		ultimoDiaDoMes.set(Calendar.MINUTE, 59);
		ultimoDiaDoMes.set(Calendar.SECOND, 59);
		ultimoDiaDoMes.set(Calendar.MILLISECOND, 999);
		return ultimoDiaDoMes;
	}

	private Date addHourToDate(Date date, Integer hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, hours);
		return c.getTime();
	}

	private Date subSecondFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, -1);
		return c.getTime();
	}

	private Calendar addHourToCalendar(Calendar date, Integer hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(date.getTime());
		c.add(Calendar.HOUR, hours);
		return c;
	}

	@Transactional
	public void deleteEscala(Escala escala) {
		plantaoRepository.deleteAllByEscala(escala);
		repository.delete(escala);
	}

}
