package br.com.ovigia.businessrule.ronda.criar;

import java.util.Date;
import java.util.List;

import br.com.ovigia.model.Localizacao;

public class CriarRondaRequest {
	public Date inicio;
	public Date fim;
	public String idVigia;
	public List<Localizacao> localizacoes;
}
