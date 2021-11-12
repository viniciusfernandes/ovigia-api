package br.com.ovigia.businessrule.solicitavaovisita.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

public class ObterSolicitacaoVisitaRule
		implements BusinessRule<ObterSolicitacaoVisitaRequest, ObterSolicitacaoVisitaResponse> {
	private SolicitacaoVisitaRepository solicitacaoRepository;

	@Override
	public Mono<Response<ObterSolicitacaoVisitaResponse>> apply(ObterSolicitacaoVisitaRequest request) {
		return solicitacaoRepository.obterSolicitacaoByIdVigia(request.idVigia)

				.collectList().map(solicitacoes -> {
					var response = new ObterSolicitacaoVisitaResponse();
					SolicitacaoVisitaResponse solResponse = null;
					for (var sol : solicitacoes) {
						solResponse = new SolicitacaoVisitaResponse();
						solResponse.data = sol.data;
						solResponse.idCliente = sol.idCliente;
						solResponse.idVigia = sol.idVigia;
						solResponse.localizacaoCliente = sol.localizacaoCliente;
						solResponse.nomeCliente = sol.nomeCliente;
						solResponse.telefoneCliente = sol.telefoneCliente;
						response.add(solResponse);
					}
					return Response.ok(response);
				});
	}
}
