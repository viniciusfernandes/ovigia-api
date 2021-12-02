package br.com.ovigia.businessrule.solicitavaovisita.remover;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

public class RemoverSolicitacaoVisitaRule implements BusinessRule<RemoverSolicitacaoVisitaRequest, Void> {
	private SolicitacaoVisitaRepository solicitacaoRepository;

	public RemoverSolicitacaoVisitaRule(SolicitacaoVisitaRepository solicitacaoRepository) {
		this.solicitacaoRepository = solicitacaoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(RemoverSolicitacaoVisitaRequest request) {
		return solicitacaoRepository.removerSolicitacao(request.idCliente).thenReturn(Response.noContent());
	}

}
