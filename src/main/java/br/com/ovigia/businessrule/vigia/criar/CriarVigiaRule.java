package br.com.ovigia.businessrule.vigia.criar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

@Deprecated
public class CriarVigiaRule implements BusinessRule<Vigia, Void> {

	private VigiaRepository repository;

	public CriarVigiaRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(Vigia vigia) {
		return repository.criar(vigia).map(id -> Response.accepted());
	}

}
