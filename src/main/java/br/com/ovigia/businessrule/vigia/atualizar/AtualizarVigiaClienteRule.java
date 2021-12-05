package br.com.ovigia.businessrule.vigia.atualizar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.VigiaRepository;
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
		return vigiaRepository.atualizarCliente(cliente.email, cliente)
				.and(clienteRepository.atualizarIdVigia(cliente.email, cliente.email))
				.thenReturn(Response.noContent());
	}
}
