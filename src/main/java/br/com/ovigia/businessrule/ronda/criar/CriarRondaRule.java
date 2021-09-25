package br.com.ovigia.businessrule.ronda.criar;

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
		ronda.id = new IdRonda(request.idVigia, request.data);
		ronda.localizacoes = request.localizacoes;

		return repository.criar(ronda).thenReturn(Response.nonResult());
	}

}
