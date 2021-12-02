package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRequest;
import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRule;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterIdVigiaSolicitadoRequest;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterIdVigiaSolicitadoResponse;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterIdVigiaSolicitadoRule;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterSolicitacaoVisitaRequest;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterSolicitacaoVisitaResponse;
import br.com.ovigia.businessrule.solicitavaovisita.obter.ObterSolicitacaoVisitaRule;
import br.com.ovigia.businessrule.solicitavaovisita.remover.RemoverSolicitacaoVisitaRule;
import br.com.ovigia.businessrule.solicitavaovisita.remover.RemoverSolicitacaoVisitaRequest;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;

public class SolicitacaoVistiaRouter extends Router {

	public SolicitacaoVistiaRouter(SolicitacaoVisitaRepository solicitacaoVisitaRepository) {
		var criarSolicitacao = Route.<CriarSolicitacaoVisitaRequest, Void>post()
				.path("/ovigia/solicitacoes-visitas/vigias/{idVigia}/clientes/{idCliente}").contemBody()
				.requestClass(CriarSolicitacaoVisitaRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new CriarSolicitacaoVisitaRule(solicitacaoVisitaRepository));

		var obterIdVigiaSolicitado = Route.<ObterIdVigiaSolicitadoRequest, ObterIdVigiaSolicitadoResponse>get()
				.path("/ovigia/solicitacoes-visitas/clientes/{idCliente}/vigia-solicitado")
				.requestClass(ObterIdVigiaSolicitadoRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new ObterIdVigiaSolicitadoRule(solicitacaoVisitaRepository));

		var obterSolicitacaoVisita = Route.<ObterSolicitacaoVisitaRequest, List<ObterSolicitacaoVisitaResponse>>get()
				.path("/ovigia/solicitacoes-visitas/vigias/{idVigia}").requestClass(ObterSolicitacaoVisitaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new ObterSolicitacaoVisitaRule(solicitacaoVisitaRepository));

		var removerSolicitacaoVisita = Route.<RemoverSolicitacaoVisitaRequest, Void>delete()
				.path("/ovigia/solicitacoes-visitas/clientes/{idCliente}").requestClass(RemoverSolicitacaoVisitaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new RemoverSolicitacaoVisitaRule(solicitacaoVisitaRepository));

		addRoute(criarSolicitacao);
		addRoute(obterIdVigiaSolicitado);
		addRoute(obterSolicitacaoVisita);
		addRoute(removerSolicitacaoVisita);
	}

}