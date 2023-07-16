package br.com.ovigia.route;

import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRequest;
import br.com.ovigia.businessrule.solicitavaovisita.criar.CriarSolicitacaoVisitaRule;
import br.com.ovigia.businessrule.solicitavaovisita.obter.*;
import br.com.ovigia.businessrule.solicitavaovisita.remover.RemoverSolicitacaoVisitaRequest;
import br.com.ovigia.businessrule.solicitavaovisita.remover.RemoverSolicitacaoVisitaRule;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import br.com.ovigia.model.repository.VigiaRepository;

import java.util.List;

public class SolicitacaoVistiaRouter extends Router {

	public SolicitacaoVistiaRouter(SolicitacaoVisitaRepository solicitacaoVisitaRepository,
			VigiaRepository vigiaRepository) {
		var criarSolicitacao = Route.<CriarSolicitacaoVisitaRequest, Void>post()
				.path("/ovigia/solicitacoes-visitas/vigias/{idVigia}/clientes/{idCliente}").contemBody()
				.requestClass(CriarSolicitacaoVisitaRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new CriarSolicitacaoVisitaRule(solicitacaoVisitaRepository));

		var obterVigiaSolicitado = Route.<ObterVigiaSolicitadoRequest, ObterVigiaSolicitadoResponse>get()
				.path("/ovigia/solicitacoes-visitas/clientes/{idCliente}/vigia-solicitado")
				.requestClass(ObterVigiaSolicitadoRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new ObterVigiaSolicitadoRule(solicitacaoVisitaRepository, vigiaRepository));

		var obterSolicitacaoVisita = Route.<ObterSolicitacaoVisitaRequest, List<ObterSolicitacaoVisitaResponse>>get()
				.path("/ovigia/solicitacoes-visitas/vigias/{idVigia}").requestClass(ObterSolicitacaoVisitaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new ObterSolicitacaoVisitaRule(solicitacaoVisitaRepository));

		var removerSolicitacaoVisita = Route.<RemoverSolicitacaoVisitaRequest, Void>delete()
				.path("/ovigia/solicitacoes-visitas/clientes/{idCliente}")
				.requestClass(RemoverSolicitacaoVisitaRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new RemoverSolicitacaoVisitaRule(solicitacaoVisitaRepository));

		addRoute(criarSolicitacao);
		addRoute(obterVigiaSolicitado);
		addRoute(obterSolicitacaoVisita);
		addRoute(removerSolicitacaoVisita);
	}

}