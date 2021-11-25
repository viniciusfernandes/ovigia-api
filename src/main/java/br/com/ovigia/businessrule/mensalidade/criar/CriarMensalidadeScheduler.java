package br.com.ovigia.businessrule.mensalidade.criar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import reactor.core.publisher.Mono;

public class CriarMensalidadeScheduler implements BusinessRule<Void, Void> {

	@Override
	public Mono<Response<Void>> apply(Void request) {
		return null;
	}

}
