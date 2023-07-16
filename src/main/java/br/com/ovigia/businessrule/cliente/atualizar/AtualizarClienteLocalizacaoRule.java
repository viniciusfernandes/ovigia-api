package br.com.ovigia.businessrule.cliente.atualizar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.repository.ClienteRepository;
import reactor.core.publisher.Mono;

import java.util.Date;

public class AtualizarClienteLocalizacaoRule implements BusinessRule<AtualizarClienteLocalizacaoRequest, Void> {
	private ClienteRepository repository;

	public AtualizarClienteLocalizacaoRule(ClienteRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(AtualizarClienteLocalizacaoRequest clienteLocalizacao) {
		var localizacao = new Localizacao(new Date().getTime(), clienteLocalizacao.latitude,
				clienteLocalizacao.longitude);
		return repository.atualizarLocalizacaoPorId(clienteLocalizacao.idCliente, localizacao)
				.map(id -> Response.noContent());
	}
}
