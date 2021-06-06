package br.com.ovigia.businessrule.cliente;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.repository.ClienteRepository;
import reactor.core.publisher.Mono;

public class CriarClienteRule implements BusinessRule<Cliente, String> {
	private ClienteRepository repository;

	public CriarClienteRule(ClienteRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<String>> apply(Cliente cliente) {
		return repository.criar(cliente).map(id ->  Response.ok(id));
	}
}
