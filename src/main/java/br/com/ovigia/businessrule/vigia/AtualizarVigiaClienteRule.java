package br.com.ovigia.businessrule.vigia;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class AtualizarVigiaClienteRule implements BusinessRule<Cliente, Void> {

	private VigiaRepository repository;

	public AtualizarVigiaClienteRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(Cliente cliente) {
		return repository.atualizarCliente(cliente.getIdVigia(), cliente).thenReturn(Response.nonResult());
	}
}
