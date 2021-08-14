package br.com.ovigia.businessrule.vigia;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class AtualizarVigiaLocalizacaoRule implements BusinessRule<AtualizarVigiaLocalizacaoRequest, Void> {
	private VigiaRepository repository;

	public AtualizarVigiaLocalizacaoRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(AtualizarVigiaLocalizacaoRequest request) {
		var localizacao = new Localizacao();
		localizacao.setData(new Date());
		localizacao.setLatitude(request.latitude);
		localizacao.setLongitude(request.longitude);
		return repository.atualizarLocalizacaoPorId(request.email, localizacao).map(id -> Response.nonResult());
	}
}