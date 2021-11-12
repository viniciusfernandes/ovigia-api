package br.com.ovigia.route;

import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRequest;
import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRule;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;

public class SolicitacaoVistiaRouter extends Router {

	public SolicitacaoVistiaRouter(SolicitacaoVisitaRepository solicitacaoVisitaRepository) {
		var criarSolicitacao = Route.<CriarSolicitacaoVisitaRequest, Void>post()
				.url("/ovigia/solicitacoes/vigias/{idVigia}/clientes/{idCliente}").contemBody()
				.requestClass(CriarSolicitacaoVisitaRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new CriarSolicitacaoVisitaRule(solicitacaoVisitaRepository));

		addRoute(criarSolicitacao);
	}

}