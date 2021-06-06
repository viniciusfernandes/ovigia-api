package br.com.ovigia.businessrule.vigia;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class AtualizarVigiaClienteRule implements BusinessRule<Cliente, Void> {

	private VigiaRepository vigiaRepository;
	private ClienteRepository clienteRepository;

	public AtualizarVigiaClienteRule(VigiaRepository repository, ClienteRepository clienteRepository) {
		this.vigiaRepository = repository;
		this.clienteRepository = clienteRepository;
	}

	@Override
	public Mono<Response<Void>> apply(Cliente cliente) {
		return vigiaRepository.atualizarCliente(cliente.getIdVigia(), cliente)
				.and(clienteRepository.atualizarVigia(cliente.getIdVigia(), cliente.getId()))
				.thenReturn(Response.nonResult());
	}
}
