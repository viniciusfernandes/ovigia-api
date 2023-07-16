package br.com.ovigia.businessrule.chamado.criar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.common.info.IdInfo;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import br.com.ovigia.model.repository.ChamadoRepository;
import reactor.core.publisher.Mono;

import java.util.Date;

public class CriarChamadoRule implements BusinessRule<CriarChamadoRequest, IdInfo> {
	private ChamadoRepository chamadoRepository;

	public CriarChamadoRule(ChamadoRepository chamadoRepository) {
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<IdInfo>> apply(CriarChamadoRequest request) {
		var chamado = new Chamado();
		chamado.data = new Date();
		chamado.idCliente = request.idCliente;
		chamado.idRonda = new IdRonda(request.idVigia, DataUtil.ajustarData());
		chamado.nomeCliente = request.nomeCliente;
		chamado.logradouro = request.logradouro;
		chamado.localizacao = new Localizacao(request.localizacao.latitude, request.localizacao.longitude);
		chamado.situacao = TipoSituacaoChamado.ATIVO;
		return chamadoRepository.criarChamado(chamado).map(id -> Response.ok(new IdInfo(id)));
	}

}
