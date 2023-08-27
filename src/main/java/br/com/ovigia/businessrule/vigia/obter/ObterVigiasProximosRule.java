package br.com.ovigia.businessrule.vigia.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.resquest.common.LocalizacaoResponse;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public class ObterVigiasProximosRule
		implements BusinessRule<ObterVigiasProximosRequest, List<ObterVigiasProximosResponse>> {
	private VigiaRepository vigiaRepository;
	private CalculadoraDistancia calculadora;
	// distancia em kilometros
	private final double distanciaMaxima = 2d;

	public ObterVigiasProximosRule(VigiaRepository vigiaRepository, CalculadoraDistancia calculadora) {
		this.vigiaRepository = vigiaRepository;
		this.calculadora = calculadora;
	}

	@Override
	public Mono<Response<List<ObterVigiasProximosResponse>>> apply(ObterVigiasProximosRequest request) {
		final var localizacao = new Localizacao(request.latitude, request.longitude);
		return vigiaRepository.obterLocalizacaoVigias()
				.filter(vigia -> isDistanciaInferior(localizacao, vigia.localizacao))
				.sort((v1, v2) -> distanciaOf(v1, v2, localizacao))
				.flatMap(vigia -> vigiaRepository.obterVigiaPorId(vigia.id)).map(vigia -> {
					var response = new ObterVigiasProximosResponse();
					response.id = vigia.id;
					response.nome = vigia.nome;
					response.dataInicio = DataUtil.formatarData(vigia.dataInicio);
					response.avaliacao = vigia.avaliacao == null ? 2.54 : vigia.avaliacao.valor;
					response.telefone = vigia.formatarTelefone();
					if (vigia.localizacao != null) {
						var loc = new LocalizacaoResponse();
						loc.latitude = vigia.localizacao.latitude;
						loc.longitude = vigia.localizacao.longitude;
						response.localizacao = loc;
					}
					return response;
				}).collectList().map(Response::ok);
	}

	private boolean isDistanciaInferior(Localizacao loc1, Localizacao loc2) {
		if (loc1 == null || loc2 == null) {
			return false;
		}
		return calculadora.isDistanciaInferior(loc1, loc2, distanciaMaxima);
	}

	private int distanciaOf(Vigia v1, Vigia v2, Localizacao localizacao) {
		double d1 = calculadora.distanciaOf(v1.localizacao, localizacao);
		double d2 = calculadora.distanciaOf(v2.localizacao, localizacao);
		if (d1 > d2) {
			return 1;
		} else if (d1 < d2) {
			return -1;
		}
		return 0;
	}
}
