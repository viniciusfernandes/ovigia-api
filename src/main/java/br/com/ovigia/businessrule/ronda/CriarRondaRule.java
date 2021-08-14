package br.com.ovigia.businessrule.ronda;

import static br.com.ovigia.businessrule.util.DataUtil.gerarData;

import java.util.ArrayList;
import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Mono;

public class CriarRondaRule implements BusinessRule<CriarRondaRequest, Void> {

	private RondaRepository repository;

	public CriarRondaRule(RondaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(CriarRondaRequest request) {
		var ronda = new Ronda();
		IdRonda id = new IdRonda();
		id.setData(gerarData(new Date()));
		id.setIdVigia(request.getIdVigia());
		ronda.setId(id);

		ronda.setLocalizacoes(new ArrayList<>());

		return repository.criar(ronda).thenReturn(Response.nonResult());
	}

}
