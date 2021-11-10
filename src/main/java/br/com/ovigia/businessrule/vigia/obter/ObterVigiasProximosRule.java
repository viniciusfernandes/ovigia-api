package br.com.ovigia.businessrule.vigia.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.resquest.common.LocalizacaoResponse;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.calculadora.CalculadoraRonda;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterVigiasProximosRule implements BusinessRule<ObterVigiasProximosRequest, ObterVigiasProximosResponse> {
	private VigiaRepository vigiaRepository;
	private CalculadoraRonda calculadora;
	// distancia em kilometros
	private final double distanciaMaxima = 2d;

	public ObterVigiasProximosRule(VigiaRepository vigiaRepository, CalculadoraRonda calculadora) {
		this.vigiaRepository = vigiaRepository;
		this.calculadora = calculadora;
	}

	@Override
	public Mono<Response<ObterVigiasProximosResponse>> apply(ObterVigiasProximosRequest request) {
		final var localizacao = new Localizacao(request.latitude, request.longitude);
		return vigiaRepository.obterLocalizacaoVigias()
				.filter(vigia -> calculadora.isDistanciaInferior(localizacao, vigia.localizacao, distanciaMaxima))
				.map(vigia -> {
					var response = new VigiaProximoResponse();
					response.id = vigia.id;
					response.nome = vigia.nome;
					var loc = new LocalizacaoResponse();
					loc.latitude = vigia.localizacao.latitude;
					loc.latitude = vigia.localizacao.latitude;

					response.localizacao = loc;
					return response;
				}).collectList().map(responses -> Response.ok(new ObterVigiasProximosResponse(responses)));
	}

}
