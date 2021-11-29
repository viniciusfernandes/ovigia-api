package br.com.ovigia.businessrule.frequenciaronda.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.frequenciaronda.commom.business.CriarFrequenciaRondasBusiness;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.repository.ClienteRepository;
import reactor.core.publisher.Mono;

public class ObterFrequenciaRondaRule
		implements BusinessRule<ObterFrequenciaRondaRequest, ObterFrequenciaRondaResponse> {
	private ClienteRepository clienteRepository;
	private CriarFrequenciaRondasBusiness criarFrequenciaRondasBusiness;

	public ObterFrequenciaRondaRule(ClienteRepository clienteRepository,
			CriarFrequenciaRondasBusiness criarFrequenciaRondasBusiness) {
		this.clienteRepository = clienteRepository;
		this.criarFrequenciaRondasBusiness = criarFrequenciaRondasBusiness;
	}

	@Override
	public Mono<Response<ObterFrequenciaRondaResponse>> apply(ObterFrequenciaRondaRequest request) {
		return obterFrequenciaRonda(request).map(frequencia -> {
			var response = new ObterFrequenciaRondaResponse();
			response.dataRonda = DataUtil.formatarData(frequencia.id.dataRonda);
			response.totalRonda = frequencia.totalRonda;
			return Response.ok(response);
		}).switchIfEmpty(Mono.just(Response.ok(new ObterFrequenciaRondaResponse())));
	}

	private Mono<FrequenciaRonda> obterFrequenciaRonda(ObterFrequenciaRondaRequest request) {
		return clienteRepository.obterFrequenciaRondaPorIdCliente(request.idCliente)
				.switchIfEmpty(criarFrequenciaRondasBusiness.apply(request.idCliente));
	}

}
