package br.com.ovigia.businessrule.contrato.obter;

import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

public class ObterContratoVencidosRule
		implements BusinessRule<ObterContratoVencidosRequest, List<ObterContratoVencidosResponse>> {
	private ContratoRepository contratoRepository;

	public ObterContratoVencidosRule(ContratoRepository contratoRepository) {
		this.contratoRepository = contratoRepository;
	}

	@Override
	public Mono<Response<List<ObterContratoVencidosResponse>>> apply(ObterContratoVencidosRequest request) {
		var dataAtual = DataUtil.ajustarData();
		return contratoRepository.obterContratosDataVencimentoInferiorByIdVigia(request.idVigia, dataAtual)
				.map(contrato -> {
					var response = new ObterContratoVencidosResponse();
					response.id = contrato.id;
					response.dataVencimento = DataUtil.formatarData(contrato.dataVencimento);
					response.idCliente = contrato.idCliente;
					response.valor = contrato.valor;
					response.nomeCliente = contrato.nomeCliente;
					response.telefoneCliente = contrato.telefoneCliente;
					return response;
				}).collectList().map(responses -> Response.ok(responses));
	}

}
