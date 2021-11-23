package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.contrato.criar.CriarContratoRequest;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoResponse;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoRule;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosResponse;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosRule;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosrRequest;
import br.com.ovigia.businessrule.contrato.remover.RemoverContratoRequest;
import br.com.ovigia.businessrule.contrato.remover.RemoverContratoRule;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;

public class ContratoRouter extends Router {

	public ContratoRouter(ContratoRepository contratoRepository,
			SolicitacaoVisitaRepository solicitacaoVisitaRepository) {
		var criarContrato = Route
				.<CriarContratoRequest, CriarContratoResponse>post().path("/ovigia/contratos").contemBody()
				.requestClass(CriarContratoRequest.class)
				.rule(new CriarContratoRule(contratoRepository, solicitacaoVisitaRepository));

		var obterContratosVencidos = Route.<ObterContratoVencidosrRequest, List<ObterContratoVencidosResponse>>get()
				.path("/ovigia/vigias/{idVigia}/contratos-vencidos").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(ObterContratoVencidosrRequest.class)
				.rule(new ObterContratoVencidosRule(contratoRepository));

		var removerContrato = Route.<RemoverContratoRequest, Void>delete()
				.path("/ovigia/contratos/{idContrato}").extractFromPath((mapa, request) -> {
					request.idContrato = mapa.get("idContrato");
					return request;
				}).rule(new RemoverContratoRule());

		addRoute(criarContrato);
		addRoute(obterContratosVencidos);
		addRoute(removerContrato);
	}

}