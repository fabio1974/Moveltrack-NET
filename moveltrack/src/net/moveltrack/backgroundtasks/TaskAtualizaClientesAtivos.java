package net.moveltrack.backgroundtasks;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.domain.PessoaStatus;
import net.moveltrack.domain.Usuario;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.util.Task;

@ApplicationScoped
public class TaskAtualizaClientesAtivos extends Task {
	
		@PostConstruct
		public void init() {
			System.out.println(this.getClass().getName() + " ...init!");
		}
		
	    public TaskAtualizaClientesAtivos() {
			
		}
	    
	    @Inject ContratoDao contratoDao;
	    @Inject ClienteDao clienteDao;
	    @Inject UsuarioDao usuarioDao;
	    @Inject VeiculoDao veiculoDao;
	    

	    @Override
	    public void run() {
			try{
				
				List<Contrato>  contratos = contratoDao.findAll();
				for (Contrato contrato : contratos) {
					
					Cliente cliente = contrato.getCliente();
					List<Veiculo> veiculos = veiculoDao.findAtivosByCliente(cliente);
					boolean temCarro = veiculos!=null && veiculos.size()>0;
					
					cliente.setStatus(contrato.getStatus()==ContratoStatus.ATIVO && temCarro?PessoaStatus.ATIVO:PessoaStatus.INATIVO);
					clienteDao.merge(cliente);
					Usuario usuario = cliente.getUsuario();
					usuario.setAtivo(contrato.getStatus()==ContratoStatus.ATIVO && temCarro);
					usuarioDao.merge(usuario);
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
	    }
	}