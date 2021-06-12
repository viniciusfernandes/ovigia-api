package br.com.ovigia.businessrule.rota;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.repository.RondaRepository;
import reactor.core.publisher.Mono;

public class ObterRotaRule implements BusinessRule<IdRonda, Ronda> {
	private RondaRepository repository;

	public ObterRotaRule(RondaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Ronda>> apply(IdRonda idRota) {
		return repository.obterRondaPorId(idRota).map(rota ->  Response.ok(rota));
	}

}
