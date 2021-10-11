package br.com.ovigia.businessrule.vigia.obter.localizacao;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterVigiaLocalizacaoRule implements BusinessRule<ObterVigiaLocalizacaoRequest, Vigia> {

	private VigiaRepository repository;

	public ObterVigiaLocalizacaoRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Vigia>> apply(ObterVigiaLocalizacaoRequest request) {
		//return repository.obterPorId(request.email).map(vigia -> Response.ok(vigia));
		return null;
	}
}
