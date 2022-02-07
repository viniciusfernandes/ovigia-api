package br.com.ovigia.businessrule.chamado.obter;

import java.util.ArrayList;
import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataHora;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.repository.ChamadoRepository;
import reactor.core.publisher.Mono;

public class ObterChamadosAbertosVigiaRule
		implements BusinessRule<ObterChamadosAbertosRequest, List<ObterChamadosVigiaResponse>> {
	private ChamadoRepository chamadoRepository;

	public ObterChamadosAbertosVigiaRule(ChamadoRepository chamadoRepository) {
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<List<ObterChamadosVigiaResponse>>> apply(ObterChamadosAbertosRequest request) {
		return chamadoRepository.obterChamadosAbertosByIdVigia(request.idVigia).map(chamados -> {
			chamados.sort((c1, c2) -> {
				if (c2.situacao.isAceito() && !c1.situacao.isAceito()) {
					return 1;
				}
				return -1;
			});

			var response = new ArrayList<ObterChamadosVigiaResponse>();

			ObterChamadosVigiaResponse chamadoVigia = null;
			DataHora dataHora = null;
			Localizacao localizacao = null;
			for (Chamado chamado : chamados) {
				chamadoVigia = new ObterChamadosVigiaResponse();

				dataHora = DataUtil.obterDataHora(chamado.data);
				chamadoVigia.data = dataHora.data;
				chamadoVigia.id = chamado.id;
				chamadoVigia.hora = dataHora.hora;
				chamadoVigia.idCliente = chamado.idCliente;
				chamadoVigia.nomeCliente = chamado.nomeCliente;
				chamadoVigia.logradouro = chamado.logradouro;
				chamadoVigia.situacao = chamado.situacao.toString();

				localizacao = chamado.localizacao;
				chamadoVigia.latitude = localizacao.latitude;
				chamadoVigia.longitude = localizacao.longitude;

				response.add(chamadoVigia);
			}
			return Response.ok(response);
		});
	}

}
