package br.com.ovigia.businessrule.vigia.avaliacao;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.NumberUtil;
import br.com.ovigia.model.calculadora.CalcularoraValorAvaliacao;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class AtualizarAvaliacaoVigiaRule implements BusinessRule<AtualizarAvaliacaoVigiaRequest, Double> {
	private VigiaRepository repository;

	public AtualizarAvaliacaoVigiaRule(VigiaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Double>> apply(AtualizarAvaliacaoVigiaRequest request) {
		return repository.obterAvaliacao(request.idVigia).flatMap(avaliacao -> {
			var novoAvaliacao = CalcularoraValorAvaliacao.calcular(avaliacao, request.valorAvaliacao);
			var novoValor = NumberUtil.round2(novoAvaliacao.valor);
			return repository.atualizarAvaliacao(request.idVigia, novoAvaliacao).thenReturn(Response.ok(novoValor));
		});
	}
}