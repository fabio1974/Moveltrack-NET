package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;

import com.google.gson.JsonElement;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Usuario;

@Stateless
@SuppressWarnings("serial")
public class PessoaDao extends DaoBean<Pessoa>{


	public PessoaDao() { }

	public List<Pessoa> findByTerm(String term, boolean pf){
		List<Pessoa> list = new ArrayList<Pessoa>();
		String pessoaFisica = pf?" and o.cpf is not null ":"";
		Query query = getEm().createQuery("select o from Pessoa o where UPPER(o.nome) like '%"+term.toUpperCase()+"%' "+ pessoaFisica +" order by o.nome");
		try{
			list = (List<Pessoa>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}

	public Pessoa findByNome(String nome) {
		try {
			Query query = getEm().createQuery("select o from Pessoa o where o.nome =:nome");
			query.setParameter("nome",nome);
			return (Pessoa)query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Pessoa findByUsuario(Usuario usuario){
		Query query = getEm().createQuery("select o from Pessoa o where o.usuario.id=:usuarioId");
		query.setParameter("usuarioId",usuario.getId());
		try{
			return (Pessoa)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public Pessoa findByNomeUsuario(String nomeUsuario){
		Query query = getEm().createQuery("select o from Pessoa o where o.usuario.nomeUsuario=:nomeUsuario");
		query.setParameter("nomeUsuario",nomeUsuario);
		try{
			return (Pessoa)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}	


	public Pessoa findByCpf(String cpf){
		Query query = getEm().createQuery("select o from Pessoa o where o.cpf=:cpf or o.cpf=:cpf2");
		query.setParameter("cpf",cpf);
		String cpf2 = cpf.replace(".","").replace("-","");
		query.setParameter("cpf2",cpf2);
		try{
			return (Pessoa)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public Pessoa findByCnpj(String cnpj){
		Query query = getEm().createQuery("select o from Pessoa o where o.cnpj=:cnpj");
		query.setParameter("cnpj",cnpj);
		try{
			return (Pessoa)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	
	
/*	
	public Cliente findByCnpj(String cnpj){
		Query query = getEm().createQuery("select o from Cliente o where o.cnpj:=cnpj");
		query.setParameter("cnpj",cnpj);
		try{
			return (Cliente)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
*/


}
