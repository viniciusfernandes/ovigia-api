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
	private final double distanciaMinima = 0.02d;
	// INTERVALO DE TOLERANCIA EM MILISEGUNDOS PARA TERMINAR A FREQUENCIA DA RONDA E
	// ELIMINAR O CENARIO EM QUE O VIGIA ESTA PARADO EM FRENTE A PROPRIEDADE DO
	// CLIENTE EM 5 MINUTOS
	private final long tolerancia = 5 * 60 * 1000;
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
		int totalRondas = 0;
		Date dataInicio = null;
		boolean isDistanciaOK = false;
		boolean encontrou = false;
		// AQUI ESTAMOS CONSIDERANDO QUE AS ROTAS ESTAO ORDENADAS PELA DATA E HORA DE
		// INCLUSAO
		for (var locVigia : localizacoesVigia) {
			isDistanciaOK = localizacaoCliente.distanciaOf(locVigia) <= distanciaMinima;
			if (isDistanciaOK && dataInicio == null) {
				dataInicio = locVigia.getData();
				encontrou = true;
				totalRondas++;
			} else if (isDistanciaOK && isDataDentroIntervalo(dataInicio, locVigia.getHora())) {
				dataInicio = locVigia.getData();
				totalRondas++;
			} else if (encontrou) {
				return totalRondas;
			}
		}
		return 0;
	}

	private boolean isDataDentroIntervalo(Date dataAntes, Date dataDepois) {
		return Math.abs(dataAntes.getTime() - dataDepois.getTime()) <= tolerancia;
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
