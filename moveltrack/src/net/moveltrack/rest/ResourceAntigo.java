package net.moveltrack.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.EquipamentoDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.SmsDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.ModeloRastreador;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.rest.antigo.ClienteApp;
import net.moveltrack.rest.antigo.Location;
import net.moveltrack.rest.antigo.Movel;
import net.moveltrack.rest.antigo.RequestLocation;
import net.moveltrack.rest.antigo.Sms;
import net.moveltrack.rest.antigo.SmsStatus;
import net.moveltrack.rest.antigo.SmsTipo;
import net.moveltrack.util.Utils;

@Path("/mt")
@RequestScoped
public class ResourceAntigo {
	
	@Inject SmsDao smsDao;
	@Inject PessoaDao pessoaDao;
	@Inject ClienteDao clienteDao;
	@Inject VeiculoDao veiculoDao;
	@Inject LocationDao locationDao;
	@Inject EquipamentoDao equipamentoDao;

    @GET
    @Path("/getSms")
    @Produces("application/json")
    public String getSms(){
    	List<net.moveltrack.domain.Sms> smss = smsDao.findByStatusPastMinute(net.moveltrack.domain.SmsStatus.ESPERANDO,1,25);
    	for (net.moveltrack.domain.Sms novo : smss) {
    		novo.setStatus(net.moveltrack.domain.SmsStatus.ENVIADO);
    		smsDao.merge(novo);
		}

    	List<Sms> smsList = new ArrayList<Sms>();
    	for (net.moveltrack.domain.Sms sms : smss) {
    		Sms smsAntigo = convertSms(sms);
    		smsList.add(smsAntigo);
		}
        return new Gson().toJson(smsList);
    }
    
        
    private Sms convertSms(net.moveltrack.domain.Sms sms) {
    	Sms antigo = new Sms();
    	antigo.setCelular(sms.getCelular());
    	antigo.setDataUltimaAtualizacao(sms.getDataUltimaAtualizacao());
    	antigo.setImei(sms.getImei());
    	antigo.setMensagem(sms.getMensagem());
    	antigo.setStatus(convertStatus(sms.getStatus()));
    	antigo.setTipo(convertTipo(sms.getTipo()));
		return antigo;
	}

	private SmsTipo convertTipo(net.moveltrack.domain.SmsTipo tipo) {
		switch (tipo) {
			case AVISO: return SmsTipo.AVISO;
			case BLOQUEIO: return SmsTipo.BLOQUEIO;
			case CHECAR: return SmsTipo.CHECAR;
			case DESBLOQUEIO: return SmsTipo.DESBLOQUEIO;
			case DNS: return SmsTipo.DNS;
			case REINICIAR: return SmsTipo.REINICIAR;
			default:return null;
		}
	}

	private SmsStatus convertStatus(net.moveltrack.domain.SmsStatus status) {
		switch (status) {
			case DESCARTADO: return SmsStatus.DESCARTADO;
			case ENVIADO: return SmsStatus.ENVIADO;
			case ESPERANDO: return SmsStatus.ESPERANDO;
			case ESPERANDO_SOCKET: return SmsStatus.ESPERANDO_SOCKET;
			case RECEBIDO: return SmsStatus.RECEBIDO;
			default:return null;
		}
	}



	@POST
    @Path("/getLocations")
    @Produces("application/json")
    @Consumes("application/json")
    public String getLocations(String rlJason){
    	
    	System.out.println("running getLocations..."+rlJason);
    	
    	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    	RequestLocation req = gson.fromJson(rlJason,RequestLocation.class);
    	

    	
    	Veiculo veiculo = veiculoDao.findByEquipamento(req.getImei());
    	
    	if(veiculo==null || veiculo.getEquipamento()==null)
    		return null;
    	
		
		String wInicio = " ";
		if(req.getInicio()!=null)
			wInicio = "and l.dateLocationInicio >=:inicio ";
		
		String wFim = " ";
		if(req.getFim()!=null)
			wFim = "and l.dateLocationInicio <=:fim ";
		
		String orderby = " order by l.id desc ";
		if(veiculo.getEquipamento().getModelo() == ModeloRastreador.GT06 
					||veiculo.getEquipamento().getModelo() == ModeloRastreador.GT06N
					||veiculo.getEquipamento().getModelo() == ModeloRastreador.GT06B
					||veiculo.getEquipamento().getModelo() == ModeloRastreador.TR02)
			orderby = " order by l.dateLocation desc ";

		
		String sql = "select l from Location l where l.imei=:imei  " + wInicio + wFim + orderby;
		
    	Query q = locationDao.getEm().createQuery(sql);
    	q.setParameter("imei",req.getImei());

    	if(req.getInicio()!=null)
    		q.setParameter("inicio",req.getInicio());
   		
    	if(req.getFim()!=null)
    		q.setParameter("fim",req.getFim());
   		
    	if(req.getFim()==null && req.getInicio()==null)
    		q.setMaxResults(50);
   		
    	List<net.moveltrack.domain.Location> locs = q.getResultList();
    	
    	List<Location> velhas = new ArrayList<Location>();
    	
    	for (net.moveltrack.domain.Location nova : locs) {
    		Location velha = new Location();
    		velha.setAlarm(nova.getAlarm());
    		velha.setAlarmType(nova.getAlarmType());
    		velha.setBattery(velha.getBattery());
    		velha.setBloqueio(nova.getBloqueio());
    		velha.setComando(nova.getComando());
    		velha.setDateLocation(nova.getDateLocation());
    		velha.setDateLocationInicio(nova.getDateLocationInicio());
    		velha.setGps(nova.getGps());
    		velha.setGsm(nova.getGsm());
    		velha.setIgnition(nova.getIgnition());
    		velha.setImei(nova.getImei());
    		velha.setLatitude(nova.getLatitude());
    		velha.setLongitude(nova.getLongitude());
    		velha.setMcc(nova.getMcc());
    		velha.setSatelites(nova.getSatelites());
    		velha.setSos(nova.getSos());
    		velha.setVelocidade(nova.getVelocidade());
    		velha.setVolt(nova.getVolt());
    		velhas.add(velha);
		}

    	System.out.println("get locations - meio..."+locs.size());
    	
    	if(locs.size()>0){
    		System.out.println("criando Gson");
    		String r = gson.toJson(velhas);
    		System.out.println("get locations - fim...");
    		return r;
    	}    
        return null;
    }    
    
    
    
    @GET
    @Path("/getClienteByUserLogin/{userLogin}")
    @Produces("application/json")
    public String getClientesByUserLogin(@PathParam("userLogin") String userLogin){
        System.out.println("running getClienteByUserLogin...");
        Cliente cliente = clienteDao.findByUsuarioNome(userLogin);

        if(cliente != null){
        
        ClienteApp cli = new ClienteApp();
			cli.setAtivo(true);
			cli.setNome(cliente.getNome());
			cli.setSenha(cliente.getUsuario().getSenha());
			cli.setId(cliente.getId().intValue());
			cli.setUserLogin(cliente.getUsuario().getNomeUsuario());
			cli.setCpf(cliente.getCpf());
			cli.setTelefone(cliente.getCelular1());
			cli.setMoveis(getMoveisPorProprietarioId(cliente.getId()));
			cli.setPessoaTipo(cliente.getUsuario().getPerfil().getTipo().toString());
			return new Gson().toJson(cli);
			
		}else{
			return null;
		}
    }

    
    
    public List<Movel> getMoveisPorProprietarioId(long id){
 		List<Veiculo> veiculos = veiculoDao.findAtivosByCliente(clienteDao.findById((int)id));
 		List<Movel> moveis = new ArrayList<Movel>();
 		for (Veiculo veiculo : veiculos) {
 			moveis.add(getMovelFromVeiculo(veiculo)); 
 		}
 		return moveis;
 	} 
 
    
    private Movel getMovelFromVeiculo(Veiculo veiculo){
 		Movel m = new Movel();
 		m.setAtivo(true);
 		m.setProprietarioId(veiculo.getContrato().getCliente().getId());
 		m.setId(veiculo.getId()!=null?veiculo.getId().intValue():0);
 		m.setImei(veiculo.getEquipamento()!=null?veiculo.getEquipamento().getImei():null);
 		m.setMarcaModelo(veiculo.getMarcaModelo());
 		m.setTipo(veiculo.getTipo()!=null?veiculo.getTipo().toString():null);
 		m.setPlaca(veiculo.getPlaca());
 		m.setCor(veiculo.getCor()!=null?veiculo.getCor().toString():null);
 		m.setCelular(veiculo.getEquipamento()!=null&&veiculo.getEquipamento().getChip()!=null?veiculo.getEquipamento().getChip().getNumero():"");
 		m.setCorte(veiculo.isComBloqueio());
 		return m;
     }
     
     
     @GET
     @Path("/getUltimoSmsDoChip/{celular}")
     @Produces("application/json")
     public String getUltimoSmsDoChip(@PathParam("celular") String celular){
    	 net.moveltrack.domain.Sms ultimoNovo = smsDao.findLastByCelularStatusNoUltimoMinuto(celular,net.moveltrack.domain.SmsStatus.RECEBIDO);
         System.out.println("running getUltimoSmsDoChip...");
         if(ultimoNovo==null)
          	return null;
         Sms sms = convertSms(ultimoNovo);
         return new Gson().toJson(sms);
     }
     
     
     
     
     
  /*   @GET
     @Path("/getClienteByCpf/{cpf}")
     @Produces("application/json")
     public String getClientesByCpf(@PathParam("cpf") String cpf){
         System.out.println("running getClienteByCpf...");	
         
 		EntityManager em = EntityProviderUtil.get().getEntityManager(Pessoa.class);
 		Query query = em.createQuery("select P from Pessoa P where P.cpf=:cpf");
 		List<Pessoa> list = query.setParameter("cpf",cpf).getResultList();
 		if(list.size()>0){
 			Pessoa pessoa = list.get(0);
 			ClienteApp cli = new ClienteApp();
 			cli.setAtivo(true);
 			cli.setNome(pessoa.getNome());
 			cli.setSenha(pessoa.getSenha());
 			cli.setId(pessoa.getId().intValue());
 			cli.setUserLogin(pessoa.getUsuario());
 			cli.setCpf(pessoa.getCpf());
 			cli.setTelefone(pessoa.getTelefoneDeContato());
 			cli.setMoveis(getMoveisPorProprietarioId(pessoa.getId()));
 			cli.setPessoaTipo(pessoa.getPessoaTipo()!=null&&pessoa.getPessoaTipo().getDescricao()!=null?pessoa.getPessoaTipo().getDescricao().name():null);
 			return new Gson().toJson(cli);
 			
 		}else{
 			return null;
 		}
     }
     
  
     
     
     @Transactional
     private void atualizaSms(Sms sms) {
 		sms.setStatus(SmsStatus.ENVIADO);
 		sms.merge();
 	}
     
     
     @Transactional
     private void atualizaCerca(Cerca cerca) {
 		cerca.setStatus(SmsStatus.ENVIADO);
 		cerca.merge();
 	}
     
     


 	@POST
     @Path("/enviarSmsList")
     @Produces("application/json")
     @Consumes("application/json")
     public String enviarSmsList(String smsListJson) {
     	System.out.println("enviarSmsList...");
         Gson gson = new Gson();
         JsonParser parser = new JsonParser();
         JsonArray array = parser.parse(smsListJson).getAsJsonArray();
         for (int i = 0; i < array.size(); i++) {
             Sms sms = (Sms)gson.fromJson(array.get(i),Sms.class);
             sms.setStatus(SmsStatus.RECEBIDO);
             sms.setDataUltimaAtualizacao(new Date());
             sms.persist();
         }       
         return "true";
     }*/
 	
 	
 	@POST
     @Path("/enviaSms")
     @Produces("application/json")
     @Consumes("application/json")
     public String enviaSms(String smsJason) {
     	System.out.println("enviaSms...");
        	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
     	Sms sms = gson.fromJson(smsJason,Sms.class);
     	
     	net.moveltrack.domain.Sms novo = new net.moveltrack.domain.Sms();
     	novo.setDataUltimaAtualizacao(new Date());
     	Equipamento equipamento = equipamentoDao.findByImei(sms.getImei());
    	novo.setMensagem(sms.getTipo()==SmsTipo.BLOQUEIO?Utils.getComandoBloqueia(equipamento):Utils.getComandoDesbloqueia(equipamento));
    	novo.setStatus(Utils.getStatusParaBloqueio(equipamento));
    	smsDao.salvar(novo);
        return "true";
     }
 	
 	
 /*	
 	@POST
     @Path("/getClientes")
     @Produces("application/json")
     @Consumes("application/json")
     public String getClientes(String listaClientesJson) {
 		List<ClienteApp> clientes = new ArrayList<ClienteApp>();
 		System.out.println("getClientes...");
         Gson gson = new Gson();
         JsonParser parser = new JsonParser();
         JsonArray array = parser.parse(listaClientesJson).getAsJsonArray();
         for (int i = 0; i < array.size(); i++) {
         	ClienteApp cliente = (ClienteApp)gson.fromJson(array.get(i),ClienteApp.class);
     		EntityManager em = EntityProviderUtil.get().getEntityManager(Pessoa.class);
     		Query query = em.createQuery("select P from Pessoa P where P.cpf=:cpf");

     		List<Pessoa> pessoas = query.setParameter("cpf",cliente.getCpf()).getResultList();
     		for (Pessoa pessoa : pessoas) {
 				
     			ClienteApp cli = new ClienteApp();
     			cli.setAtivo(true);
     			cli.setNome(pessoa.getNome());
     			cli.setSenha(pessoa.getSenha());
     			cli.setId(pessoa.getId().intValue());
     			cli.setCpf(pessoa.getCpf());
     			cli.setTelefone(pessoa.getTelefoneDeContato());
     			cli.setMoveis(getMoveisPorProprietarioId(pessoa.getId()));
     			cli.setNivelDeAcesso(pessoa.getNivelDeAcesso().ordinal());
     			clientes.add(cli);
     		}
     		
         }
 		if(clientes.size()>0)
 			return new Gson().toJson(clientes);
         return null;
     }    */
 	
 	
 /*	@GET
     @Path("/sincronizaAllClientes")
     @Produces("application/json")
     public String sincronizaAllClientes() {
 		 List<ClienteApp> clientes = new ArrayList<ClienteApp>();
 		System.out.println("sincronizaAllClientes...");  //8871-5896 
         Gson gson = new Gson();
         
     		EntityManager em = EntityProviderUtil.get().getEntityManager(Pessoa.class);
     		Query query = em.createQuery("select c from Cliente c , Veiculo v where v.proprietario=c.id and v.contratoStatus=:contratoStatus");

     		List<Pessoa> pessoas = query.setParameter("contratoStatus",ContratoStatus.ATIVO).getResultList();
     		for (Pessoa pessoa : pessoas) {
 				
     			ClienteApp cli = new ClienteApp();
     			cli.setAtivo(true);
     			cli.setNome(pessoa.getNome());
     			cli.setSenha(pessoa.getSenha());
     			cli.setId(pessoa.getId().intValue());
     			cli.setCpf(pessoa.getCpf());
     			cli.setTelefone(pessoa.getTelefoneDeContato());
     			cli.setMoveis(getMoveisPorProprietarioId(pessoa.getId()));
     			cli.setPessoaTipo(pessoa.getPessoaTipo()!=null&&pessoa.getPessoaTipo().getDescricao()!=null?pessoa.getPessoaTipo().getDescricao().name():null);
     			clientes.add(cli);
     		}
      
 		if(clientes.size()>0)
 			return new Gson().toJson(clientes);
         return null;
     }    	
 	*/
	
	
	
    
    
}