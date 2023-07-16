package br.com.ovigia.businessrule.ronda.criar;

import br.com.ovigia.model.Localizacao;

import java.util.Date;
import java.util.List;

public class CriarRondaRequest {
	public Date inicio;
	public Date fim;
	public String idVigia;
	public List<Localizacao> localizacoes;
}
