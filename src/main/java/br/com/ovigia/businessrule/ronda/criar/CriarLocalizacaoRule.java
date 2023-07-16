package br.com.ovigia.businessrule.ronda.criar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Mono;

import java.util.Date;

import static br.com.ovigia.businessrule.util.DataUtil.ajustarData;

public class CriarLocalizacaoRule implements BusinessRule<Localizacao, Void> {

	private RondaRepository repository;

	public CriarLocalizacaoRule(RondaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(Localizacao localizacao) {
		var dataAtual = new Date();
		localizacao.timestamp = dataAtual.getTime();
		return repository.criarLocalizacao(localizacao.idVigia, ajustarData(dataAtual), localizacao)
				.map(id -> Response.ok(id));
	}

}
