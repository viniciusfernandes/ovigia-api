package br.com.ovigia.route;

import br.com.ovigia.businessrule.ronda.criar.CriarRondaRequest;
import br.com.ovigia.businessrule.ronda.criar.CriarRondaRule;
import br.com.ovigia.businessrule.ronda.resumo.obter.ObterResumoRondaRequest;
import br.com.ovigia.businessrule.ronda.resumo.obter.ObterResumoRondaResponse;
import br.com.ovigia.businessrule.ronda.resumo.obter.ObterResumoRondaRule;
import br.com.ovigia.model.repository.ChamadoRepository;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;

public class RondaRouter extends Router {

	public RondaRouter(RondaRepository rondaRepository, ResumoRondaRepository resumoRepository,
			ChamadoRepository chamadoRepository) {
		var criarRonda = Route.<CriarRondaRequest, Void>post().url("/ovigia/vigias/{idVigia}/rondas").contemBody()
				.requestClass(CriarRondaRequest.class).extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new CriarRondaRule(rondaRepository, resumoRepository));

		var obterResumoRonda = Route.<ObterResumoRondaRequest, ObterResumoRondaResponse>get()
				.url("ovigia/vigias/{idVigia}/rondas/resumo").requestClass(ObterResumoRondaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new ObterResumoRondaRule(resumoRepository, chamadoRepository));

		addRoute(criarRonda);
		addRoute(obterResumoRonda);
	}

}