package br.com.ovigia.businessrule.cliente;

import java.util.Date;
import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.RotaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CalcularFrequenciaRondasRule implements BusinessRule<CalculoFrequencia, List<FrequenciaRonda>> {
	// Distancia em metros
	private final double distanciaMinima = 0.015d;
	private ClienteRepository clienteRepository;
	private RotaRepository rotaRepository;

	public CalcularFrequenciaRondasRule(ClienteRepository clienteRepository, RotaRepository rotaRepository) {
		this.clienteRepository = clienteRepository;
		this.rotaRepository = rotaRepository;
	}

	@Override
	public Mono<Response<List<FrequenciaRonda>>> apply(CalculoFrequencia calculoFrequencia) {
		var data = DataUtil.gerarData(calculoFrequencia.getDataRonda());

		return clienteRepository.obterVigiasELocalizacao(calculoFrequencia.getIdCliente())
				.flatMap(cliente -> processarRotasPorId(cliente.getVigias(), data, cliente.getLocalizacao()))
				.map(frequencias -> Response.ok(frequencias));
	}

	private int calcularNumeroRodas(Localizacao localizacaoCliente, List<Localizacao> localizacoesVigia) {
		if (localizacoesVigia == null || localizacoesVigia.isEmpty()) {
			return 0;
		}
		int total = 0;
		for (var locVigia : localizacoesVigia) {
			if (localizacaoCliente.distanciaOf(locVigia) <= distanciaMinima) {
				total++;
			}
		}
		return total;
	}

	private Mono<List<FrequenciaRonda>> processarRotasPorId(List<String> idsVigias, Date data,
			Localizacao localizacao) {
		return Flux.fromIterable(idsVigias).flatMap(idVigia -> {
			return calcularFrequenciaRonda(idVigia, data, localizacao);
		}).collectList();

	}

	private Mono<FrequenciaRonda> calcularFrequenciaRonda(String idVigia, Date data, Localizacao localizacao) {
		return rotaRepository.obterRotaPorId(idVigia, data).map(rota -> {
			var frequenciaRonda = new FrequenciaRonda();
			var totalRondas = calcularNumeroRodas(localizacao, rota.getLocalizacoes());
			frequenciaRonda.setIdVigia(rota.getId().getIdVigia());
			frequenciaRonda.setTotalRonda(totalRondas);
			return frequenciaRonda;
		});

	}
}

