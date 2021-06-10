package br.com.ovigia.businessrule.cliente;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.ClienteRepository;
import reactor.core.publisher.Mono;

public class AtualizarClienteLocalizacaoRule implements BusinessRule<AtualizarClienteLocalizacaoRequest, Void> {
	private ClienteRepository repository;

	public AtualizarClienteLocalizacaoRule(ClienteRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(AtualizarClienteLocalizacaoRequest clienteLocalizacao) {
		var localizacao = new Localizacao(new Date(), clienteLocalizacao.latitude, clienteLocalizacao.longitude);
		return repository.atualizarLocalizacaoPorId(clienteLocalizacao.idCliente, localizacao)
				.map(id -> Response.nonResult());
	}
}
