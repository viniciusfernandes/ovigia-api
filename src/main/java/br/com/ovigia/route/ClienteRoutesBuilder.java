package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import br.com.ovigia.businessrule.cliente.CriarClienteRule;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.repository.ClienteRepository;

public class ClienteRoutesBuilder extends RoutesBuilder {

	public ClienteRoutesBuilder(ClienteRepository repository) {
		add(route(POST("/ovigia/clientes"), req -> {
			return toBody(handleRequest(req, Cliente.class, new CriarClienteRule(repository)));
		}));

	}

}
