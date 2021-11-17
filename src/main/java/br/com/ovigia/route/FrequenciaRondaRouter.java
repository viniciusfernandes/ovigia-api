package br.com.ovigia.route;

import br.com.ovigia.businessrule.frequenciaronda.criar.CriarFrequenciaRondaRequest;
import br.com.ovigia.businessrule.frequenciaronda.criar.CriarFrequenciaRondaResponse;
import br.com.ovigia.businessrule.frequenciaronda.criar.CriarFrequenciaRondasRule;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.FrequenciaRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;

public class FrequenciaRondaRouter extends Router {

	public FrequenciaRondaRouter(ClienteRepository clienteRepository, RondaRepository rotaRepository,
			FrequenciaRondaRepository frequenciaRondaRepository, ContratoRepository contratoRepository) {

		var criarFrequencia = Route.<CriarFrequenciaRondaRequest, CriarFrequenciaRondaResponse>post()
				.url("/ovigia/frequenciaronda/clientes").contemBody().requestClass(CriarFrequenciaRondaRequest.class)
				.rule(new CriarFrequenciaRondasRule(clienteRepository, rotaRepository, frequenciaRondaRepository,
						contratoRepository));

		addRoute(criarFrequencia);
	}

}
