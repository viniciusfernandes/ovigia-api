package br.com.ovigia.businessrule.frequenciaronda.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.frequenciaronda.commom.business.CriarFrequenciaRondasBusiness;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterFrequenciaRondaRule
		implements BusinessRule<ObterFrequenciaRondaRequest, ObterFrequenciaRondaResponse> {
	private ClienteRepository clienteRepository;
	private CriarFrequenciaRondasBusiness criarFrequenciaRondasBusiness;
	private VigiaRepository vigiaRepository;

	public ObterFrequenciaRondaRule(ClienteRepository clienteRepository, VigiaRepository vigiaRepository,
			CriarFrequenciaRondasBusiness criarFrequenciaRondasBusiness) {
		this.clienteRepository = clienteRepository;
		this.vigiaRepository = vigiaRepository;
		this.criarFrequenciaRondasBusiness = criarFrequenciaRondasBusiness;
	}

	@Override
	public Mono<Response<ObterFrequenciaRondaResponse>> apply(ObterFrequenciaRondaRequest request) {
		return obterFrequenciaRonda(request).map(frequencia -> {
			var response = new ObterFrequenciaRondaResponse();
			response.dataUltimaRonda = DataUtil.formatarData(frequencia.dataUltimaRonda);
			response.totalRonda = frequencia.totalRonda;
			return Response.ok(response);
		}).switchIfEmpty(Mono.just(Response.ok(new ObterFrequenciaRondaResponse())));
	}

	private Mono<FrequenciaRonda> obterFrequenciaRonda(ObterFrequenciaRondaRequest request) {
		return clienteRepository.obterFrequenciaRondaPorIdCliente(request.idCliente).flatMap(frequencia -> {
			if (frequencia.isExistente()) {
				return recalcularFrequenciaRonda(frequencia, request.idCliente);
			}
			return calcularFrequenciaRonda(request.idCliente);
		});

	}

	private Mono<FrequenciaRonda> recalcularFrequenciaRonda(FrequenciaRonda frequencia, String idCliente) {
		return vigiaRepository.obterDataUltimaRonda(frequencia.idVigia).flatMap(vigia -> {
			// Essa condicao existe pois o vigia pode incluir as rondas parcialmente, assim
			// temos que garantir que, se a ronda
			// foi atualizada, devemos recalcular a frequencia de ronda para o cliente
			if (frequencia.isDesatualizada(vigia.dataAtualizacaoRonda)) {
				return criarFrequenciaRondasBusiness.apply(idCliente, vigia.dataAtualizacaoRonda);
			}
			return Mono.just(frequencia);
		});
	}

	private Mono<FrequenciaRonda> calcularFrequenciaRonda(String idCliente) {
		return clienteRepository.obterIdVigiaELocalizacaoByIdCliente(idCliente)
				.flatMap(cliente -> vigiaRepository.obterDataUltimaRonda(cliente.idVigia))
				.flatMap(vigia -> criarFrequenciaRondasBusiness.apply(idCliente, vigia.dataAtualizacaoRonda));

	}

}
