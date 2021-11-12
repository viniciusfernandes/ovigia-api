package br.com.ovigia.businessrule.solicitavaovisita.obter;

import java.util.ArrayList;
import java.util.List;

public class ObterSolicitacaoVisitaResponse {
	public List<SolicitacaoVisitaResponse> solicitacoes = new ArrayList<>();

	public void add(SolicitacaoVisitaResponse reponse) {
		solicitacoes.add(reponse);
	}
}
