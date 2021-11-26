package br.com.ovigia.businessrule.mensalidade;

import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.MensalidadeRepository;
import reactor.core.publisher.Mono;

public class ObterMensalidadesVencidasRule
		implements BusinessRule<ObterMensalidadesVencidasRequest, List<ObterMensalidadesVencidasResponse>> {
	private MensalidadeRepository mensalidadeRepository;

	public ObterMensalidadesVencidasRule(MensalidadeRepository mensalidadeRepository) {
		this.mensalidadeRepository = mensalidadeRepository;
	}

	@Override
	public Mono<Response<List<ObterMensalidadesVencidasResponse>>> apply(ObterMensalidadesVencidasRequest request) {
		return mensalidadeRepository.obterMensalidadesVencidasByIdVigia(request.idVigia).map(mensalidade -> {
			var response = new ObterMensalidadesVencidasResponse();
			request.idVigia = mensalidade.idVigia;
			response.dataVencimento = mensalidade.dataVencimento;
			response.id = mensalidade.id;
			response.nomeCliente = mensalidade.nomeCliente;
			response.telefoneCliente = mensalidade.telefoneCliente;
			response.valor = mensalidade.valor;
			return response;
		}).collectList().map(Response::ok);
	}

}
