package br.com.ovigia.route;

import br.com.ovigia.businessrule.exception.DataMalFormatadaException;
import br.com.ovigia.businessrule.ronda.criar.CriarRondaRequest;
import br.com.ovigia.businessrule.ronda.criar.CriarRondaResponse;
import br.com.ovigia.businessrule.ronda.criar.CriarRondaRule;
import br.com.ovigia.businessrule.ronda.obter.ObterRondaRule;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Id;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.repository.RondaRepository;

public class RondaRouter extends Router {

	public RondaRouter(RondaRepository repository) {
		var criarRonda = Route.<CriarRondaRequest, CriarRondaResponse>post();
		criarRonda.url("/ovigia/vigias/{idVigia}/rondas").contemBody().requestClass(CriarRondaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new CriarRondaRule(repository));

		var obterRonda = Route.<Id, Ronda>get();
		obterRonda.url("ovigia/vigias/{idVigia}/rondas/{data}").requestClass(Id.class)
				.extractFromPath((mapa, request) -> {
					try {
						request.data = DataUtil.parseToDataRota(mapa.get("data"));
						request.idVigia = mapa.get("idVigia");
					} catch (DataMalFormatadaException e) {
						e.printStackTrace();
					}
					return request;
				}).rule(new ObterRondaRule(repository));

		addRoute(criarRonda);
		addRoute(obterRonda);
	}

}