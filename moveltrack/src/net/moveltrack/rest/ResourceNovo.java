package net.moveltrack.rest;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
import net.moveltrack.dao.FirebaseDao;
import net.moveltrack.dao.LocationDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.SmsDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.Firebase;
import net.moveltrack.domain.Location;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Sms;
import net.moveltrack.domain.SmsStatus;
import net.moveltrack.domain.SmsTipo;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.util.MapaUtil;
import net.moveltrack.util.Utils;

@Path("/app")
@RequestScoped
public class ResourceNovo {
	

	@Inject PessoaDao pessoaDao;
	@Inject ClienteDao clienteDao;
	@Inject VeiculoDao veiculoDao;
	
    @GET
    @Path("/getClienteByUserLogin/{userLogin}")
    @Produces("application/json")
    public String getClienteByUserLogin(@PathParam("userLogin") String userLogin){
        System.out.println("running getClienteByUserLogin...");	
        Pessoa pessoa = pessoaDao.findByNomeUsuario(userLogin);
        Gson gson  = new GsonBuilder().setExclusionStrategies(new GsonExcludeStrategy()).create();
       	return pessoa!=null?gson.toJson(pessoa):"{\"retorno\":\"usuarioInexistente\"}";
    }
    
    
    
    @GET
    @Path("/getVeiculosByUserLogin/{userLogin}")
    @Produces("application/json")
    public String getVeiculosByUserLogin(@PathParam("userLogin") String userLogin){
        System.out.println("running getVeiculosByUserLogin...");	
        Cliente cliente = clienteDao.findByUsuarioNome(userLogin);
        List<Veiculo> list = veiculoDao.findAtivosByCliente(cliente);
        Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setExclusionStrategies(new GsonExcludeStrategy()).create();
       	return list==null||list.size()==0?"[{\"retorno\":\" semVeiculosParaUsuario\"}]":gson.toJson(list);
    }

    
    @Inject LocationDao  locationDao;
    
    @GET
    @Path("/getLocationsByVeiculoId/{veiculoId}/{inicio}/{fim}")
    @Produces("application/json")
    public String getLocationsByVeiculoId(@PathParam("veiculoId") String veiculoIdStr,@PathParam("inicio") String inicioStr,@PathParam("fim")String fimStr){
    	System.out.println("running getLocationsByVeiculoId...veiculo "+veiculoIdStr);
    	int veiculoId = Integer.valueOf(veiculoIdStr);
    	Date inicio = new Date(Long.valueOf(inicioStr));
    	Date fim = new Date(Long.valueOf(fimStr));
    	Veiculo veiculo = veiculoDao.findById(veiculoId);
    	List<Object> list = locationDao.getLocationsFromVeiculo(veiculo,inicio,fim);
    	Location previous = locationDao.getPreviousLocation(MapaUtil.getLocationFromObject(list.get(0)));
    	List<Location> otimizado = MapaUtil.otimizaPontosDoBanco(list,inicio,fim,previous,veiculo.getEquipamento());
        Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setExclusionStrategies(new GsonExcludeStrategy()).create();
       	String result = otimizado==null||otimizado.size()==0?"[{\"retorno\":\" semLocationsParaVeiculo\"}]":gson.toJson(otimizado);
       	return result;
    }
    
    
    
    @GET
    @Path("/getClientesByNome/{nome}")
    @Produces("application/json")
    public String getClientesByNome(@PathParam("nome") String nome){
    	System.out.println("running getClientesByNome...nome "+nome);
    	List<Cliente> list = clienteDao.findByTerm(nome);
        Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setExclusionStrategies(new GsonExcludeStrategy()).create();
        return list==null||list.size()==0?"[{\"retorno\":\" semRegistros\"}]":gson.toJson(list);
    }        
    
    
    
	@Inject SmsDao smsDao;

	@GET
	@Path("/getSmss")
	@Produces("application/json")
	public String getSmss(){
		List<Sms> smss = smsDao.findByStatusPastMinute(SmsStatus.ESPERANDO,2,-1);
		if(smss==null || smss.size()==0){
			System.out.println("Pegando um conjunto vazio de Sms");
			return "[{\"retorno\":\" semSms\"}]";
		}	
		System.out.println("Pegando um conjunto de "+smss.size()+" Sms's");
		for (Sms sms : smss) {
			sms.setStatus(SmsStatus.ENVIADO);
			smsDao.merge(sms);
		}
        Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(smss);
	}
	
	
	
	@POST
    @Path("/smsCelularToServer")
    @Produces("application/json")
    @Consumes("application/json")
    public String enviaSms(String smsJason) {
		
		try{
	    	System.out.println("smsCelularToServer...");
	       	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	    	Sms sms = gson.fromJson(smsJason,Sms.class);
	    	sms.setDataUltimaAtualizacao(new Date());
	    	sms.setStatus(SmsStatus.RECEBIDO);
	    	sms.setTipo(SmsTipo.AVISO);
	    	smsDao.salvar(sms);
	    	return "{\"retorno\":\" Ok\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"retorno\":\" Erro\"}";
		}
        
    }
	
	
	@Inject FirebaseDao firebaseDao;

	
	@POST
    @Path("/sendFirebase")
    @Produces("application/json")
    @Consumes("application/json")
    public String sendFirebase(String fireBaseJason) {
		
		try{
	    	System.out.println("sendFirebase...");
	       	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	    	Firebase firebase = gson.fromJson(fireBaseJason,Firebase.class);

	    	Firebase aux = firebaseDao.findBySerial(firebase);
	    	if(aux!=null){
	    		aux.setToken(firebase.getToken());
	    		aux.setUltimaAtualizacao(new Date());
	    		if(firebase.getUsuario()!=null)
	    			aux.setUsuario(firebase.getUsuario());
	    		firebaseDao.merge(aux);
	    	}else{
		    	firebase.setUltimaAtualizacao(new Date());
	    		firebaseDao.salvar(firebase);
	    	}
	    	
	    	return "{\"retorno\":\" Ok\"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"retorno\":\" Erro\"}";
		}
        
    }
	
	
	
	@Inject EquipamentoDao equipamentoDao;
	
	@POST
    @Path("/enviaComandoRele")
    @Produces("application/json")
    @Consumes("application/json")
    public String enviaComandoRele(String smsJason) {
		
		try{
	    	System.out.println("enviaComandoRele...");
	       	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	    	Sms sms = gson.fromJson(smsJason,Sms.class);
	    	sms.setDataUltimaAtualizacao(new Date());
	    	Equipamento equipamento = equipamentoDao.findByImei(sms.getImei());
	    	sms.setMensagem(sms.getTipo()==SmsTipo.BLOQUEIO?Utils.getComandoBloqueia(equipamento):Utils.getComandoDesbloqueia(equipamento));
	    	smsDao.salvar(sms);
	    	Calendar c = Calendar.getInstance();
	    	c.add(Calendar.MINUTE,1);
	    	Date timeOut = c.getTime();
	    	Date time = new Date();
	    	Sms resposta = null;
	    	while(resposta == null && time.before(timeOut)){
	    		resposta = smsDao.findLastByCelularStatusNoUltimoMinuto(sms.getCelular(),SmsStatus.RECEBIDO);
	    		System.out.println(resposta!=null?resposta.getMensagem():"resposta nula");
	    		Thread.sleep(2000);
	    		time = new Date();
	    	}
	    	if(resposta!=null){
	    		resposta.setStatus(SmsStatus.DESCARTADO);
	    		smsDao.merge(resposta);
	    		return gson.toJson(resposta);
	    	}else
	    		return "{\"retorno\":\"semRetorno\"}";
	    		
		}catch(Exception e){
			e.printStackTrace();
			return "{\"retorno\":\"semRetorno\"}";
		}
        
    }


	private void saveResposta(String celular) {
		Sms resposta = new Sms();
		resposta.setMensagem("Your vehicle power has been cut!");
		resposta.setCelular(celular);
		resposta.setDataUltimaAtualizacao(new Date());
		resposta.setStatus(SmsStatus.RECEBIDO);
		smsDao.salvar(resposta);
	}
	
	
	
	
	
	/*@Inject CercaDao cercaDao;
	
	@GET
	@Path("/getCercas")
	@Produces("application/json")
	public String getCercas(){
		List<Cerca> cercas = cercaDao.findByStatusPastMinute(CercaSSmsStatus.ESPERANDO,1,100);
		for (Sms sms : smss) {
			sms.setStatus(SmsStatus.ENVIADO);
			smsDao.merge(sms);
		}
		return new Gson().toJson(smss);
	}*/
	
	
	
/*	
	@GET
	@Path("/getCercaVirtual")
	@Produces("application/json")
	public String getCercaVirtual(){
		EntityManager em = EntityProviderUtil.get().getEntityManager(Cerca.class);
		Query q = em.createQuery("select c from Cerca c where c.status=:status ");
		q.setParameter("status",SmsStatus.ESPERANDO);
		List<Cerca> cercaList = q.getResultList();
		for (Cerca cerca : cercaList) {
			atualizaCerca(cerca);
		}
		return new Gson().toJson(cercaList);
	}
	
	
	
    
    
    @GET
    @Path("/getSmsDns")
    @Produces("application/json")
    public String getSmsDns(){
    	EntityManager em = EntityProviderUtil.get().getEntityManager(Sms.class);
    	Query q = em.createQuery("select s from Sms s where s.tipo=:tipo "); 
    	q.setParameter("tipo",SmsTipo.DNS);
    	List<Sms> smsIn = (List<Sms>)q.getResultList();
        return new Gson().toJson(smsIn);
    }    
    
    
     
    private Movel getMovelFromVeiculo(Veiculo veiculo,long id){
		Movel m = new Movel();
		m.setAtivo(veiculo.getContratoStatus()==ContratoStatus.ATIVO);
		m.setProprietarioId(id);
		m.setId(veiculo.getId()!=null?veiculo.getId().intValue():0);
		m.setImei(veiculo.getEquipamento()!=null?veiculo.getEquipamento().getImei():null);
		m.setMarcaModelo(veiculo.getMarcaModelo());
		m.setTipo(veiculo.getTipo()!=null?veiculo.getTipo().toString():null);
		m.setPlaca(veiculo.getPlaca());
		m.setCor(veiculo.getCor()!=null?veiculo.getCor().toString():null);
		m.setCelular(veiculo.getEquipamento()!=null&&veiculo.getEquipamento().getChip()!=null?veiculo.getEquipamento().getChip().getNumero():"");
		m.setCorte(veiculo.isCortaCombustivel());
		return m;
    }
    
    
    public List<Movel> getMoveisPorProprietarioId(long id){
		EntityManager em = EntityProviderUtil.get().getEntityManager(Veiculo.class);

		Query query = em.createQuery("select V from Veiculo V where V.contratoStatus=:contratoStatus  and V.proprietario.id=:id ");
		query.setParameter("contratoStatus",ContratoStatus.ATIVO);
		query.setParameter("id",id);
		
		List<Veiculo> veiculos = query.getResultList();

		List<Movel> moveis = new ArrayList<Movel>();
		for (Veiculo veiculo : veiculos) {
			moveis.add(getMovelFromVeiculo(veiculo,id)); 
		}
		return moveis;
	}    
    
    
    
    
    @GET
    @Path("/getUltimoSmsDoChip/{celular}")
    @Produces("application/json")
    public String getUltimoSmsDoChip(@PathParam("celular") String celular){
        System.out.println("running getUltimoSmsDoChip...");	
        Sms sms = Utils.getUltimoSmsDoChip(celular);
        if(sms==null)
        	return null;
		return new Gson().toJson(sms);

    }
    

    @GET
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
    }
	
	
	@POST
    @Path("/enviaSms")
    @Produces("application/json")
    @Consumes("application/json")
    public String enviaSms(String smsJason) {
    	System.out.println("enviaSms...");
       	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    	Sms sms = gson.fromJson(smsJason,Sms.class);
    	Equipamento equipamento = Utils.getEquipamentoByImei(sms.getImei());
    	sms.setMensagem(sms.getTipo().equals(SmsTipo.BLOQUEIO)?Utils.getComandoBloqueia(equipamento):sms.getTipo().equals(SmsTipo.DESBLOQUEIO)?Utils.getComandoDesbloqueia(equipamento):null);
        sms.setDataUltimaAtualizacao(new Date());
        sms.setStatus(Utils.getStatusParaBloqueio(equipamento));
        sms.persist();
        return "true";
    }
	
	

	
	
	@GET
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