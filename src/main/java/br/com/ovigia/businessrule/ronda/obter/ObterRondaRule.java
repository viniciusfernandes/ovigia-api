package br.com.ovigia.businessrule.ronda.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Id;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Mono;

public class ObterRondaRule implements BusinessRule<Id, Ronda> {
	private RondaRepository repository;

	public ObterRondaRule(RondaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Ronda>> apply(Id idRota) {
		return repository.obterRondaPorId(idRota).map(rota ->  Response.ok(rota));
	}

}
