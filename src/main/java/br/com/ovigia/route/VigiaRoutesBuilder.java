package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import br.com.ovigia.businessrule.vigia.AtualizarVigiaClienteRule;
import br.com.ovigia.businessrule.vigia.CriarVigiaRule;
import br.com.ovigia.businessrule.vigia.ObterVigiaRule;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.VigiaRepository;

public class VigiaRoutesBuilder extends RoutesBuilder {

	public VigiaRoutesBuilder(VigiaRepository vigiaRepository) {
		add(route(POST("/ovigia/vigias"), req -> {
			return toBody(handleRequest(req, Vigia.class, new CriarVigiaRule(vigiaRepository)));
		}));

		add(route(GET("/ovigia/vigias/{idVigia}"), req -> {
			return toBody(handleRequest(req.pathVariable("idVigia"), new ObterVigiaRule(vigiaRepository)));

		}));

		add(route(PATCH("/ovigia/vigias/{idVigia}/clientes"), req -> {
			var idVigia = req.pathVariable("idVigia");
			
			var mAtualizar = req.bodyToMono(Cliente.class).flatMap(cliente -> {
				cliente.setIdVigia(idVigia);
				return new AtualizarVigiaClienteRule(vigiaRepository).apply(cliente);
			});

			return toBody(mAtualizar);
		}));

	}

}