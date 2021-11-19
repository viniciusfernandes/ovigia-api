package br.com.ovigia.businessrule.contrato.obter;

import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

public class ObterContratoVencidosRule
		implements BusinessRule<ObterContratoVencidosrRequest, List<ObterContratoVencidosResponse>> {
	private ContratoRepository contratoRepository;

	public ObterContratoVencidosRule(ContratoRepository contratoRepository) {
		this.contratoRepository = contratoRepository;
	}

	@Override
	public Mono<Response<List<ObterContratoVencidosResponse>>> apply(ObterContratoVencidosrRequest request) {
		int diaMes = DataUtil.obterDiaMes();
		return contratoRepository.obterContratosDiaVencimentoInferiorByIdVigia(request.idVigia, diaMes)
				.map(contrato -> {
					var response = new ObterContratoVencidosResponse();
					response.dataVenciamento = DataUtil.formatarDataByDia(contrato.diaVencimento);
					response.idCliente = contrato.idCliente;
					response.valor = contrato.valor;
					return response;
				}).collectList().map(responses -> Response.ok(responses));
	}

}
