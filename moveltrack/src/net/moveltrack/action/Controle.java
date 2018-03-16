package net.moveltrack.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.PerfilDao;
import net.moveltrack.dao.PermissaoDao;
import net.moveltrack.domain.Perfil;
import net.moveltrack.domain.PerfilTipo;
import net.moveltrack.domain.Permissao;


@Named
public class Controle extends ConversationBaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;


	@Inject
	PerfilDao perfilDao;
	
	@Inject
	PermissaoDao permissaoDao;


	Perfil perfil;
	//private HtmlSelectOneRadio typeSelectOne; 
	
	private List<Permissao> leftSelected;
	private List<Permissao> leftAvailable;
	private List<Permissao> rightSelected;
	private List<Permissao> rightAvailable;

	
	

	public Controle() {
		System.out.println("controle form constructor ... ");
	}

	@PostConstruct
	public void init() {
		if(perfil==null)
			perfil = perfilDao.findByTipo(PerfilTipo.ADMINISTRADOR);
		System.out.println("controle form  Init ... ");

		update();
/*		leftAvailable = permissaoDao.findAll();
	    rightAvailable =  perfil!=null && perfil.getPermissoes()!=null?new ArrayList<Permissao>(perfil.getPermissoes()):new ArrayList<Permissao>();
	    leftAvailable.removeAll(rightAvailable);
*/	}
	
	
    public void leftToRight() {
        leftAvailable.removeAll(leftSelected);
        rightAvailable.addAll(leftSelected);
        leftSelected = null;
    }

    public void rightToLeft() {
        rightAvailable.removeAll(rightSelected);
        leftAvailable.addAll(rightSelected);
        rightSelected = null;
    }
    
	@Transactional
	public void salvar() {
		System.out.println("Salvando perfil!");
		Set<Permissao> permissoes = new HashSet<Permissao>(rightAvailable);
		perfil.setPermissoes(permissoes);
		perfilDao.merge(perfil);
		operacaoSucesso();
	}
	
	public void update(){
		System.out.println("updating view");
		leftAvailable = permissaoDao.findAll();
		rightAvailable =  perfil!=null && perfil.getPermissoes()!=null?new ArrayList<Permissao>(perfil.getPermissoes()):new ArrayList<Permissao>();
		leftAvailable.removeAll(rightAvailable);
	}
	

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}


	public Perfil[] getPerfis(){
		List<Perfil> list = perfilDao.findAll();
		return list.toArray(new Perfil[list.size()]);
	}
	


	public List<Permissao> getLeftSelected() {
		return leftSelected;
	}

	public void setLeftSelected(List<Permissao> leftSelected) {
		this.leftSelected = leftSelected;
	}

	public List<Permissao> getLeftAvailable() {
		leftAvailable.sort(new Comparator<Permissao>() {
			@Override
			public int compare(Permissao o1, Permissao o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return leftAvailable;
	}

	public void setLeftAvailable(List<Permissao> leftAvailable) {
		this.leftAvailable = leftAvailable;
	}

	public List<Permissao> getRightSelected() {
		return rightSelected;
	}

	public void setRightSelected(List<Permissao> rightSelected) {
		this.rightSelected = rightSelected;
	}

	public List<Permissao> getRightAvailable() {
		rightAvailable.sort(new Comparator<Permissao>() {
			@Override
			public int compare(Permissao o1, Permissao o2) {
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});
		return rightAvailable;
	}

	public void setRightAvailable(List<Permissao> rightAvailable) {
		this.rightAvailable = rightAvailable;
	}

/*	public HtmlSelectOneRadio getTypeSelectOne() {
		return typeSelectOne;
	}

	public void setTypeSelectOne(HtmlSelectOneRadio typeSelectOne) {
		this.typeSelectOne = typeSelectOne;
	}*/




	


}