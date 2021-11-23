package br.com.ovigia.businessrule.contrato.remover;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

public class RemoverContratoRule implements BusinessRule<RemoverContratoRequest, Void> {
	private ContratoRepository contratoRepository;

	@Override
	public Mono<Response<Void>> apply(RemoverContratoRequest request) {
		return contratoRepository.atualizarDataFimContrato(request.idContrato, new Date())
				.thenReturn(Response.noContent());
	}

}
