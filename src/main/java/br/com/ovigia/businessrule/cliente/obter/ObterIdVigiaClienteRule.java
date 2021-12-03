package br.com.ovigia.businessrule.cliente.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.ClienteRepository;
import reactor.core.publisher.Mono;

public class ObterIdVigiaClienteRule implements BusinessRule<ObterIdVigiaClienteRequest, ObterIdVigiaClienteResponse> {
	private ClienteRepository clienteRepository;

	public ObterIdVigiaClienteRule(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Override
	public Mono<Response<ObterIdVigiaClienteResponse>> apply(ObterIdVigiaClienteRequest request) {
		return clienteRepository.obterIdVigiaELocalizacaoByIdCliente(request.idCliente)
				.map(cliente -> Response.ok(new ObterIdVigiaClienteResponse(cliente.idVigia)))
				.switchIfEmpty(Mono.just(Response.noContent()));
	}

}
