package br.com.ovigia.businessrule.vigia;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterVigiaRule implements BusinessRule<ObterVigiaRequest, Vigia> {

	private VigiaRepository repository;

	public ObterVigiaRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Vigia>> apply(ObterVigiaRequest request) {
		return repository.obterPorId(request.email).map(vigia -> Response.ok(vigia));
	}
}
