package br.com.ovigia.route;

import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRequest;
import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRule;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterIdVigiaSolicitadoRequest;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterIdVigiaSolicitadoResponse;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterIdVigiaSolicitadoRule;
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

		var obterIdVigiaSolicitado = Route.<ObterIdVigiaSolicitadoRequest, ObterIdVigiaSolicitadoResponse>get()
				.url("/ovigia/solicitacoes/clientes/{idCliente}/vigiasolicitado")
				.requestClass(ObterIdVigiaSolicitadoRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new ObterIdVigiaSolicitadoRule(solicitacaoVisitaRepository));

		addRoute(criarSolicitacao);
		addRoute(obterIdVigiaSolicitado);
	}

}