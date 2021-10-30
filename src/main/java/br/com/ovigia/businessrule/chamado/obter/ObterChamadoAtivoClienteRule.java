package br.com.ovigia.businessrule.chamado.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.ChamadoRepository;
import reactor.core.publisher.Mono;

public class ObterChamadoAtivoClienteRule
		implements BusinessRule<ObterChamadoAtivoClienteRequest, ObterChamadoAtivoClienteResponse> {
	private ChamadoRepository chamadoRepository;

	public ObterChamadoAtivoClienteRule(ChamadoRepository chamadoRepository) {
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<ObterChamadoAtivoClienteResponse>> apply(ObterChamadoAtivoClienteRequest request) {
		return chamadoRepository.obterChamadosAtivoByIdCliente(request.idCliente).map(chamado -> {
			var response = new ObterChamadoAtivoClienteResponse();
			var dataHora = DataUtil.gerarDataHora(chamado.data);
			response.id = chamado.id;
			response.data = dataHora.data;
			response.hora = dataHora.hora;
			return Response.ok(response);
		});
	}

}
