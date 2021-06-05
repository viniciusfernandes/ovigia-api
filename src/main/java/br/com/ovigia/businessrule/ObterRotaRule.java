package br.com.ovigia.businessrule;

import br.com.ovigia.model.IdRota;
import br.com.ovigia.model.Rota;
import br.com.ovigia.repository.RotaRepository;
import reactor.core.publisher.Mono;

public class ObterRotaRule implements BusinessRule<IdRota, Rota> {
	private RotaRepository repository;

	public ObterRotaRule(RotaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Rota>> apply(IdRota idRota) {
		return repository.obterRotaPorId(idRota).map(rota ->  Response.ok(rota));
	}

}
