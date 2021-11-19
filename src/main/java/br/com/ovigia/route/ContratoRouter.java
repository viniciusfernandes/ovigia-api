package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.contrato.criar.CriarContratoRequest;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoRule;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosResponse;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosRule;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosrRequest;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;

public class ContratoRouter extends Router {

	public ContratoRouter(ContratoRepository contratoRepository,
			SolicitacaoVisitaRepository solicitacaoVisitaRepository) {
		var criarContrato = Route.<CriarContratoRequest, Void>post().url("/ovigia/contratos").contemBody()
				.requestClass(CriarContratoRequest.class)
				.rule(new CriarContratoRule(contratoRepository, solicitacaoVisitaRepository));

		var obterContratosVencidos = Route.<ObterContratoVencidosrRequest, List<ObterContratoVencidosResponse>>get()
				.url("/ovigia/vigias/{idVigia}/contratos-vencidos").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(ObterContratoVencidosrRequest.class)
				.rule(new ObterContratoVencidosRule(contratoRepository));

		addRoute(criarContrato);
		addRoute(obterContratosVencidos);
	}

}