package br.com.ovigia.model;

import java.util.Date;

import br.com.ovigia.model.enumeration.TipoSituacaoChamado;

public class Chamado {
	public String id;
	public Date data;
	public String idCliente;
	public String idVigia;
	public TipoSituacaoChamado situacao;
}
