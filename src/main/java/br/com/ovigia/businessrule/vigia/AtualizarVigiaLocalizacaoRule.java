package br.com.ovigia.businessrule.vigia;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class AtualizarVigiaLocalizacaoRule implements BusinessRule<Vigia, Void> {
	private VigiaRepository repository;

	public AtualizarVigiaLocalizacaoRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(Vigia vigia) {
		var localizacao = vigia.getLocalizacao();
		localizacao.setData(new Date());
		return repository.atualizarLocalizacaoPorId(vigia.getId(), localizacao).map(id -> Response.nonResult());
	}
}