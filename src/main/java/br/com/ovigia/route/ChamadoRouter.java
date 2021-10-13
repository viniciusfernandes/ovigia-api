package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.chamado.criar.CriarChamadoRequest;
import br.com.ovigia.businessrule.chamado.criar.CriarChamadoRule;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosAtivosRequest;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosAtivosVigiaRule;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosVigiaResponse;
import br.com.ovigia.businessrule.common.info.IdInfo;
import br.com.ovigia.model.repository.ChamadoRepository;

public class ChamadoRouter extends Router {

	public ChamadoRouter(ChamadoRepository chamadoRepository) {

		var criarChamado = Route.<CriarChamadoRequest, IdInfo>post();
		criarChamado.url("/ovigia/clientes/{idCliente}/chamados").contemBody().extractFromPath((mapa, request) -> {
			request.idCliente = mapa.get("idCliente");
			return request;
		}).requestClass(CriarChamadoRequest.class).rule(new CriarChamadoRule(chamadoRepository));

		var obterChamadosAtivosVigia = Route.<ObterChamadosAtivosRequest, List<ObterChamadosVigiaResponse>>get();
		obterChamadosAtivosVigia.url("/ovigia/vigias/{idVigia}/chamadosativos").extractFromPath((mapa, request) -> {
			request.idVigia = mapa.get("idVigia");
			return request;
		}).requestClass(ObterChamadosAtivosRequest.class).rule(new ObterChamadosAtivosVigiaRule(chamadoRepository));

		addRoute(criarChamado);
		addRoute(obterChamadosAtivosVigia);

	}

}
