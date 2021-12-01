package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;

public class Mensalidade {
	public String id;
	public String idContrato;
	public String idVigia;
	public Date dataVencimento;
	public Date dataPagamento;
	public Double valor;
	public String nomeCliente;
	public String telefoneCliente;
	public TipoSituacaoMensalidade situacao;
}
