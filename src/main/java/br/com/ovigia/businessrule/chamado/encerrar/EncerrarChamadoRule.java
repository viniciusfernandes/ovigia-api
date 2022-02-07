package br.com.ovigia.businessrule.chamado.encerrar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import br.com.ovigia.model.repository.ChamadoRepository;
import reactor.core.publisher.Mono;

public class EncerrarChamadoRule implements BusinessRule<EncerrarChamadoRequest, Void> {
	private ChamadoRepository chamadoRepository;

	public EncerrarChamadoRule(ChamadoRepository chamadoRepository) {
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(EncerrarChamadoRequest request) {
		return chamadoRepository.atualizarSituacao(request.idChamado, TipoSituacaoChamado.ENCERRADO)
				.thenReturn(Response.noContent());
	}	
}
