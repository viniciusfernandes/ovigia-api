package br.com.ovigia.businessrule;

import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class CriarVigiaRule implements BusinessRule<Vigia, String> {

	private VigiaRepository repository;

	public CriarVigiaRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<String>> apply(Vigia vigia) {
		return repository.criar(vigia).map(id -> new Response<>(id));
	}

}
