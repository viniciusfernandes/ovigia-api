package br.com.ovigia.businessrule.ronda;

import static br.com.ovigia.businessrule.util.DataUtil.gerarData;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.RondaRepository;
import reactor.core.publisher.Mono;

public class CriarLocalizacaoRule implements BusinessRule<Localizacao, Void> {

	private RondaRepository repository;

	public CriarLocalizacaoRule(RondaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(Localizacao localizacao) {
		var dataAtual = new Date();
		localizacao.setHora(dataAtual);
		return repository.criarLocalizacao(localizacao.getIdVigia(), gerarData(dataAtual), localizacao)
				.map(id -> Response.ok(id));
	}

}
