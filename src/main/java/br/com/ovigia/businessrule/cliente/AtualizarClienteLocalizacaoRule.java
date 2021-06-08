package br.com.ovigia.businessrule.cliente;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.repository.ClienteRepository;
import reactor.core.publisher.Mono;

public class AtualizarClienteLocalizacaoRule implements BusinessRule<Cliente, Void> {
	private ClienteRepository repository;

	public AtualizarClienteLocalizacaoRule(ClienteRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(Cliente cliente) {
		var localizacao = cliente.getLocalizacao();
		localizacao.setData(new Date());
		return repository.atualizarLocalizacaoPorId(cliente.getId(), localizacao).map(id -> Response.nonResult());
	}
}