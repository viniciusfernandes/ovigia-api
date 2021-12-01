package br.com.ovigia.businessrule.mensalidade.pagar;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.MensalidadeRepository;
import reactor.core.publisher.Mono;

public class PagarMensalidadeRule implements BusinessRule<PagarMensalidadeRequest, Void> {
	private MensalidadeRepository mensalidadeRepository;

	public PagarMensalidadeRule(MensalidadeRepository mensalidadeRepository) {
		this.mensalidadeRepository = mensalidadeRepository;
	}

	@Override
	public Mono<Response<Void>> apply(PagarMensalidadeRequest request) {
		return mensalidadeRepository
				.atualizaDataPagamentoMensalidade(request.idMensalidade, new Date(), TipoSituacaoMensalidade.PAGO)
				.thenReturn(Response.noContent());
	}

}
