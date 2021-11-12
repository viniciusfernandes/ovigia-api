package br.com.ovigia.businessrule.solicitavaovisita.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

public class ObterIdVigiaSolicitadoRule
		implements BusinessRule<ObterIdVigiaSolicitadoRequest, ObterIdVigiaSolicitadoResponse> {
	private SolicitacaoVisitaRepository solicitacaoRepository;

	public ObterIdVigiaSolicitadoRule(SolicitacaoVisitaRepository solicitacaoRepository) {
		this.solicitacaoRepository = solicitacaoRepository;
	}

	@Override
	public Mono<Response<ObterIdVigiaSolicitadoResponse>> apply(ObterIdVigiaSolicitadoRequest request) {
		return solicitacaoRepository.obterIdVigiaSolicitado(request.idCliente)
				.map(id -> Response.ok(new ObterIdVigiaSolicitadoResponse(id)));
	}
}
