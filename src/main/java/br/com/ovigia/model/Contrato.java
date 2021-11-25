package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.model.enumeration.TipoSituacaoContrato;

public class Contrato {
	public String id;
	public Date dataInicio;
	public Date dataFim;
	public Date dataVencimento;
	public Double valor;
	public String idVigia;
	public String idCliente;
	public String nomeCliente;
	public String telefoneCliente;
	public TipoSituacaoContrato situacao;

	public boolean isContratoVencido() {
		return dataVencimento != null && !dataVencimento.after(new Date());
	}

}
