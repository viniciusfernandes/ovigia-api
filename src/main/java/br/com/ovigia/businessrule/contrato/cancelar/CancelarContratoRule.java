package br.com.ovigia.businessrule.contrato.cancelar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

import java.util.Date;

public class CancelarContratoRule implements BusinessRule<CancelarContratoRequest, Void> {
	private ContratoRepository contratoRepository;

	public CancelarContratoRule(ContratoRepository contratoRepository) {
		this.contratoRepository = contratoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CancelarContratoRequest request) {
		return contratoRepository.atualizarDataFimContrato(request.idContrato, new Date())
				.thenReturn(Response.noContent());
	}

}
