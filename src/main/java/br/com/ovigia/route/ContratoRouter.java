package br.com.ovigia.route;

import br.com.ovigia.businessrule.contrato.criar.CriarContratoRequest;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoRule;
import br.com.ovigia.model.repository.ContratoRepository;

public class ContratoRouter extends Router {

	public ContratoRouter(ContratoRepository contratoRepository) {
		var criarContrato = Route.<CriarContratoRequest, Void>post().url("/ovigia/contratos").contemBody()
				.requestClass(CriarContratoRequest.class).rule(new CriarContratoRule(contratoRepository));

		addRoute(criarContrato);
	}

}