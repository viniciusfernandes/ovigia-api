package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.model.enumeration.TipoSituacaoContrato;

public class Contrato {
	public Date dataInicio;
	public Date dataFim;
	public Double valor;
	public String idVigia;
	public String idCliente;
	public TipoSituacaoContrato situacaoContrato;
}
