package net.moveltrack.financeiro.cnabcaixa.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.moveltrack.domain.MBoleto;



public class ArquivoRemessa {
	
	private List<Registro> registros = new ArrayList<Registro>();

	public ArquivoRemessa(String nsa, List<MBoleto> mBoletos, Date horaDoArquivo) {
		HeaderArquivo headerArquivo = new HeaderArquivo(Integer.parseInt(nsa),horaDoArquivo);
		int codigoLote = 1;
		HeaderLote headerLote = new HeaderLote(codigoLote,Integer.parseInt(nsa),horaDoArquivo);
		registros.add(headerArquivo);
		registros.add(headerLote);
		
		SegmentoP sp;
		SegmentoQ sq;
		SegmentoR sr;
		
		Double valorTotalDosTitulos = 0d;
		
		int numeroSequencialLote = 0;
		for (MBoleto mBoleto : mBoletos) {
			 sp = new SegmentoP(mBoleto,codigoLote,++numeroSequencialLote);
			 sq = new SegmentoQ(mBoleto,codigoLote,++numeroSequencialLote);
			 sr = new SegmentoR(mBoleto,codigoLote,++numeroSequencialLote);
			 registros.add(sp);
			 registros.add(sq);
			 registros.add(sr);
			 valorTotalDosTitulos+=mBoleto.getValor();
		}

		Integer quantidadeDeTitulos = mBoletos.size();
		Integer quantidadeDeRegistrosNoLote = 3 * quantidadeDeTitulos +2;
		
		TrailerLote trailerLote = new TrailerLote(quantidadeDeRegistrosNoLote,quantidadeDeTitulos,valorTotalDosTitulos,codigoLote);
		registros.add(trailerLote);
		
		TrailerArquivo trailerArquivo =  new TrailerArquivo(1,quantidadeDeRegistrosNoLote+2); //Só tem um lote
		registros.add(trailerArquivo);
	}


	public void grava(PrintWriter writer) {
		for (Registro registro : registros) {
			registro.printRegistro(writer);
		}
	}
	
	
}
