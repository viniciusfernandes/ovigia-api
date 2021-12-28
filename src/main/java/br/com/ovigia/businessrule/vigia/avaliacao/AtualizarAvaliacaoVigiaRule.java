package br.com.ovigia.businessrule.vigia.avaliacao;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.calculadora.CalcularoraValorAvaliacao;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class AtualizarAvaliacaoVigiaRule
		implements BusinessRule<AtualizarAvaliacaoVigiaRequest, AtualizarAvaliacaoVigiaResponse> {
	private VigiaRepository repository;

	public AtualizarAvaliacaoVigiaRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<AtualizarAvaliacaoVigiaResponse>> apply(AtualizarAvaliacaoVigiaRequest request) {
		return repository.obterAvaliacao(request.idVigia).flatMap(avaliacao -> {
			var novoAvaliacao = CalcularoraValorAvaliacao.calcular(avaliacao, request.valorAvaliacao);
			var response = Response.ok(new AtualizarAvaliacaoVigiaResponse(novoAvaliacao.valor));
			return repository.atualizarAvaliacao(request.idVigia, novoAvaliacao).thenReturn(response);
		});
	}
}