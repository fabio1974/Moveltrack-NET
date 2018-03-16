package net.moveltrack.financeiro.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import net.moveltrack.action.TableBean;
import net.moveltrack.dao.CarneConteudoDao;
import net.moveltrack.dao.CarneDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.domain.Carne;
import net.moveltrack.domain.CarneConteudo;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ContratoGeraCarne;
import net.moveltrack.domain.TipoDeCobranca;


@Named
@SessionScoped
public class CarneTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	CarneDao carneDao;
	
	@Inject 
	MBoletoDao mBoletoDao;
	
	List<Carne> carnes;

	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = carneDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        setFiltrosAcesso();
		refreshPage();
	}
	
	
	
	public List<Carne> getCarnes() {
        return carnes;
	}
	
	private void setFiltrosAcesso() {
	}
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		try {
			setFilters();
			
			carnes = carneDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = carneDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}

	@Inject BoletoUtils boletoUtils;
	@Inject CarneConteudoDao carneConteudoDao;;
	
	public void geraPdf(Carne carne) throws IOException {
		
		Date horaDoArquivo = carne.getDataEmissao();
		
		SimpleDateFormat sdf = new SimpleDateFormat("-yyyyMMdd-HHmmss");
		
		String myFileName = "CarneMoveltrack_"+carne.getContrato().getCliente().getNome()+sdf.format(horaDoArquivo)+".pdf";
	    FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    ec.responseReset(); 
	    ec.setResponseContentType(ec.getMimeType(myFileName));
	    ec.setResponseHeader("Content-Disposition","attachment; filename=\"" + myFileName + "\""); 
	    
	    OutputStream output = ec.getResponseOutputStream();
	    //criaConteudo(carne,ec);

	    carne.getContrato().setContratoGeraCarne(ContratoGeraCarne.GERADO);
	    
	    /*if(carne.getConteudo().getConteudo()==null){
	    	operacaoErro("Carne ainda sem conteúdo! aguarde!");
	    	return;
	    }*/
	    
	    output.write(boletoUtils.geraCarne(carne,(ServletContext)ec.getContext()));
	    
	    output.flush();
	    output.close();
	    fc.responseComplete(); 
	}
	
	
/*	private void criaConteudo(Carne carne, ExternalContext ec) {
		CarneConteudo carneConteudo = carne.getConteudo();
		if (carneConteudo==null){
			carneConteudo = new CarneConteudo();
			carneConteudoDao.salvar(carneConteudo);
		}
		ServletContext sc = (ServletContext)ec.getContext();
		carneConteudo.setConteudo(boletoUtils.geraCarne(carne,sc));
		carneConteudoDao.merge(carneConteudo);
		carne.setConteudo(carneConteudo);
		carneDao.merge(carne);
	}*/

	private Cliente cliente;
	private TipoDeCobranca tipoDeCobranca;
	
	public TipoDeCobranca[] getTipoDeCobrancas(){
		return TipoDeCobranca.values();
	}

	
	@Override
	public void setFilters() {
		preFilters.put("modalidade",tipoDeCobranca);
		preFilters.put("contrato.clienteId",cliente);
		normalizeFilters();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setCarnes(List<Carne> carnes) {
		this.carnes = carnes;
	}

	
	public String getEnderecoHtml(Cliente cliente){
		if(cliente==null)
			return null;
		String htmlDescription = "<br>&nbsp;&nbsp;&nbsp;<font size=\"2\" face=\"Times New Roman, Times, serif\"><b>"+cliente.getNome().toUpperCase()+"</b>"+
                "<br>&nbsp;&nbsp;&nbsp;&nbsp;"+cliente.getEndereco().toUpperCase()+", nº "+cliente.getNumero()+ (cliente.getComplemento()!=null && !cliente.getComplemento().equals("")?", "+cliente.getComplemento().toUpperCase():"")+
       		 "<br>&nbsp;&nbsp;&nbsp;&nbsp;"+cliente.getBairro().toUpperCase()+", "+cliente.getMunicipio().getDescricao().toUpperCase()+" - "+cliente.getMunicipio().getUf().getSigla().toUpperCase()+
       		 "<br>&nbsp;&nbsp;&nbsp;&nbsp;"+(cliente.getCep()!=null?"CEP "+cliente.getCep():"")+"</font>";
		return htmlDescription;
	}


	public TipoDeCobranca getTipoDeCobranca() {
		return tipoDeCobranca;
	}



	public void setTipoDeCobranca(TipoDeCobranca tipoDeCobranca) {
		this.tipoDeCobranca = tipoDeCobranca;
	}



	

}
