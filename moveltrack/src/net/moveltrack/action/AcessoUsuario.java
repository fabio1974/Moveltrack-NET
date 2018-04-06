package net.moveltrack.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.PermissaoDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.Permissao;
import net.moveltrack.domain.Usuario;


@Named
@SessionScoped
public class AcessoUsuario extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;


	@Inject
	UsuarioDao usuarioDao;
	
	@Inject
	PermissaoDao permissaoDao;


	Usuario usuario;
	
	String nomeCliente;
 
	
	private List<Permissao> leftSelected;
	private List<Permissao> leftAvailable;
	private List<Permissao> rightSelected;
	private List<Permissao> rightAvailable;

	
	

	public AcessoUsuario() {
		System.out.println("Constructor for "+ this.getClass().getName());
	}

	@PostConstruct
	public void init() {
		System.out.println("Init for "+ this.getClass().getName());
		//updateView();
	}
	
	
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
		usuario.setPermissoes(permissoes);
		usuarioDao.merge(usuario);
		operacaoSucesso();
		updateView();
	}
	
	public void updateView(){
		System.out.println("updating view");
		leftAvailable = permissaoDao.findAll();
		
		usuario = usuarioDao.findById(usuario.getId()); 
		
		rightAvailable =  usuario!=null && usuario.getPermissoes()!=null?new ArrayList<Permissao>(usuario.getPermissoes()):new ArrayList<Permissao>();
		leftAvailable.removeAll(rightAvailable);
	}
	

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	//	updateView();
		
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

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	


}