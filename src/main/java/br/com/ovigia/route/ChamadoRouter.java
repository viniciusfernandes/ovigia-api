package br.com.ovigia.route;

import br.com.ovigia.businessrule.chamado.CriarChamadoRequest;
import br.com.ovigia.businessrule.chamado.CriarChamadoRule;
import br.com.ovigia.businessrule.common.IdResponse;
import br.com.ovigia.model.repository.ChamadoRepository;

public class ChamadoRouter extends Router {

	public ChamadoRouter(ChamadoRepository chamadoRepository) {

		var criarChamadoRoute = Route.<CriarChamadoRequest, IdResponse>post();
		criarChamadoRoute.url("/ovigia/clientes/{idCliente}/chamados")
		.contemBody()
		.extractFromPath((mapa, request) -> {
			request.idCliente = mapa.get("idCliente");
			return request;
		})
		.requestClass(CriarChamadoRequest.class)
		.rule(new CriarChamadoRule(chamadoRepository));

		addRoute(criarChamadoRoute);

	}

}
