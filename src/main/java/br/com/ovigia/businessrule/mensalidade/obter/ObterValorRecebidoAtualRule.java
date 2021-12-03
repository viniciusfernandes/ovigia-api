package br.com.ovigia.businessrule.mensalidade.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdFaturamento;
import br.com.ovigia.model.repository.FaturamentoRepository;
import reactor.core.publisher.Mono;

public class ObterValorRecebidoAtualRule
		implements BusinessRule<ObterValorRecebidoAtualRequest, ObterValorRecebidoAtualResponse> {
	private FaturamentoRepository faturamentoRepository;

	public ObterValorRecebidoAtualRule(FaturamentoRepository faturamentoRepository) {
		this.faturamentoRepository = faturamentoRepository;
	}

	@Override
	public Mono<Response<ObterValorRecebidoAtualResponse>> apply(ObterValorRecebidoAtualRequest request) {
		var mesAno = DataUtil.obterMesAno();
		var id = new IdFaturamento(request.idVigia, mesAno.mes, mesAno.ano);
		return faturamentoRepository.obterValoresFaturamento(id).map(faturamento -> {
			var valor = faturamento.valor;
			return Response.ok(new ObterValorRecebidoAtualResponse(valor));
		});
	}

}
