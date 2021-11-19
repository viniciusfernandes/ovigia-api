package br.com.ovigia.businessrule.cliente.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.ClienteRepository;
import reactor.core.publisher.Mono;

public class ObterFrequenciaRondaRule
		implements BusinessRule<ObterFrequenciaRondaRequest, ObterFrequenciaRondaResponse> {
	public ObterFrequenciaRondaRule(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	private ClienteRepository clienteRepository;

	@Override
	public Mono<Response<ObterFrequenciaRondaResponse>> apply(ObterFrequenciaRondaRequest request) {
		return clienteRepository.obterFrequenciaRondaPorIdCliente(request.idCliente).map(frequencia -> {
			var response = new ObterFrequenciaRondaResponse();
			response.data = DataUtil.formatarData(frequencia.id.data);
			response.totalRonda = frequencia.totalRonda;
			return Response.ok(response);
		}).switchIfEmpty(Mono.just(Response.ok(new ObterFrequenciaRondaResponse())));
	}

}
