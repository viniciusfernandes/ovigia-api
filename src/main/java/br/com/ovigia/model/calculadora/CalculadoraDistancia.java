package br.com.ovigia.model.calculadora;

import static br.com.ovigia.businessrule.util.NumberUtil.round;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;

public abstract class CalculadoraDistancia {
	public final static double MILISEGUNDOS_TO_HORAS_RATE = 1000.0 * 3600.d;

	public double calcularDistancia(Ronda ronda) {
		var localizacoes = ronda.localizacoes;
		final int size = localizacoes.size();
		if (size <= 1) {
			return 0.0;
		}
		double totDist = 0;
		final int last = size - 2;
		Localizacao l1 = null;
		Localizacao l2 = null;
		for (int i = 0; i < size; i++) {
			l1 = localizacoes.get(i);
			l2 = localizacoes.get(i + 1);
			totDist += distanciaOf(l1, l2);
			if (i >= last) {
				break;
			}
		}

		return round(totDist);
	}

	public static Long calcularTempo(Ronda ronda) {
		return ronda.fim.getTime() - ronda.inicio.getTime();
	}

	public static TempoEscala calcularTempoEscala(Long timestamp) {
		var tempo = timestamp / MILISEGUNDOS_TO_HORAS_RATE;

		var escala = EscalaTemporal.HORAS;
		if (tempo < 0.09) {
			tempo *= 60.0;
			escala = EscalaTemporal.MINUTOS;

			if (tempo < 0.9) {
				tempo *= 60.0;
				escala = EscalaTemporal.SEGUNDOS;
			}
		}

		return new TempoEscala(tempo, escala.getPrefixo());
	}

	public double calcularTempo(Localizacao loc1, Localizacao loc2) {
		return (loc1.timestamp - loc2.timestamp) / MILISEGUNDOS_TO_HORAS_RATE;
	}

	public abstract double distanciaOf(Localizacao loc1, Localizacao loc2);

	public boolean isDistanciaInferior(Localizacao loc1, Localizacao loc2, double limite) {
		return distanciaOf(loc1, loc2) < limite;
	}

	public boolean isDistanciaOk(Localizacao loc1, Localizacao loc2, double distanciaMaxima) {
		return distanciaOf(loc1, loc2) <= distanciaMaxima;
	}

	@Deprecated
	public static CalculadoraDistancia calculadoraEuclidiana() {
		return new CalculadoraDistanciaEuclidiana();
	}

	public static CalculadoraDistancia calculadoraEsferica() {
		return new CalculadoraDistanciaEsferica();
	}

}

enum EscalaTemporal {
	SEGUNDOS('s'), MINUTOS('m'), HORAS('h');

	private char prefixo;

	private EscalaTemporal(char prefixo) {
		this.prefixo = prefixo;
	}

	public char getPrefixo() {
		return prefixo;
	}
}
