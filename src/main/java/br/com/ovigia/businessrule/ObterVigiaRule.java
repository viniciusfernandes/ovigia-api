package br.com.ovigia.businessrule;

import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterVigiaRule implements BusinessRule<String, Vigia> {

	private VigiaRepository repository;

	public ObterVigiaRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Vigia>> apply(String idVigia) {
		return repository.buscarPorId(idVigia).map(vigia -> Response.ok(vigia));
	}
}
