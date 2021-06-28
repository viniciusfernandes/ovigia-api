package br.com.ovigia.route;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Cliente;
import reactor.core.publisher.Mono;

public class TesteRouter extends Router {

	public TesteRouter() {

		var testeRoute = Route.<Cliente, String>get();
		testeRoute.url("/teste/{email}").contemBody().requestClass(Cliente.class).extractFromPath((mapa, cliente) -> {
			cliente.setEmail(mapa.get("email").toString());
			return cliente;
		}).rule(new BusinessRule<Cliente, String>() {

			@Override
			public Mono<Response<String>> apply(Cliente t) {

				return Mono.just(Response.ok(t.getNome()));
			}
		});

		add(testeRoute);
	}
}
