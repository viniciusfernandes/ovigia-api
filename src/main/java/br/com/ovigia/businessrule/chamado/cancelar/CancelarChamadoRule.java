package br.com.ovigia.businessrule.chamado.cancelar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import br.com.ovigia.model.repository.ChamadoRepository;
import reactor.core.publisher.Mono;

public class CancelarChamadoRule implements BusinessRule<CancelarChamadoRequest, Void> {
	private ChamadoRepository chamadoRepository;

	public CancelarChamadoRule(ChamadoRepository chamadoRepository) {
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CancelarChamadoRequest request) {
		return chamadoRepository.atualizarSituacao(request.idChamado, TipoSituacaoChamado.CANCELADO_CLIENTE)
				.thenReturn(Response.nonResult());
	}

}
