package br.com.ovigia.businessrule.chamado;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.common.IdResponse;
import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.repository.ChamadoRepository;
import reactor.core.publisher.Mono;

public class CriarChamadoRule implements BusinessRule<CriarChamadoRequest, IdResponse> {
	private ChamadoRepository chamadoRepository;

	public CriarChamadoRule(ChamadoRepository chamadoRepository) {
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<IdResponse>> apply(CriarChamadoRequest request) {
		var chamado = new Chamado();
		chamado.data = new Date();
		chamado.idCliente = request.idCliente;
		chamado.idVigia = request.idVigia;

		return chamadoRepository.criar(chamado).map(id -> Response.ok(new IdResponse(id)));
	}

}
