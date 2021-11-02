package br.com.ovigia.businessrule.chamado.aceitar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import br.com.ovigia.model.repository.ChamadoRepository;
import reactor.core.publisher.Mono;

public class AceitarChamadoRule implements BusinessRule<AceitarChamadoRequest, Void> {
	private ChamadoRepository chamadoRepository;

	public AceitarChamadoRule(ChamadoRepository chamadoRepository) {
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(AceitarChamadoRequest request) {
		return chamadoRepository.atualizarSituacao(request.idChamado, TipoSituacaoChamado.ACEITO)
				.thenReturn(Response.nonResult());
	}

}
