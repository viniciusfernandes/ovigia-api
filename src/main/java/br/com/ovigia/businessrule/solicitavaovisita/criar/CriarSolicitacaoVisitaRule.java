package br.com.ovigia.businessrule.solicitavaovisita.criar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.SolicitacaoVisita;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

import java.util.Date;

public class CriarSolicitacaoVisitaRule implements BusinessRule<CriarSolicitacaoVisitaRequest, Void> {
	private SolicitacaoVisitaRepository solicitacaoRepository;

	public CriarSolicitacaoVisitaRule(SolicitacaoVisitaRepository solicitacaoRepository) {
		this.solicitacaoRepository = solicitacaoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CriarSolicitacaoVisitaRequest request) {
		final var solicitacao = new SolicitacaoVisita();
		solicitacao.data = new Date();
		solicitacao.idCliente = request.idCliente;
		solicitacao.idVigia = request.idVigia;
		solicitacao.idVigia = request.idVigia;
		solicitacao.nomeCliente = request.nomeCliente;
		solicitacao.telefoneCliente = request.telefoneCliente;
		solicitacao.localizacaoCliente = request.localizacaoCliente;
		return solicitacaoRepository.removerSolicitacao(request.idCliente)
				.and(solicitacaoRepository.criarSolicitacao(solicitacao)).thenReturn(Response.noContent());
	}

}
