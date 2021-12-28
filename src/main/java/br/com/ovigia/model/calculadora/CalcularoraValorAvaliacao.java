package br.com.ovigia.model.calculadora;

import br.com.ovigia.model.Avaliacao;

public class CalcularoraValorAvaliacao {
	private CalcularoraValorAvaliacao() {

	}

	public static Avaliacao calcular(double valorMedio, int quantidade, double valorNovaAvaliacao) {
		double vm = valorMedio;
		int q1 = quantidade;
		int q2 = q1 + 1;
		double v = valorNovaAvaliacao;
		double valorAvaliacao = vm * q1 / q2 + v / q2;
		return new Avaliacao(valorAvaliacao, q2);
	}

	public static Avaliacao calcular(Avaliacao avaliacao, double valorNovaAvaliacao) {
		return calcular(avaliacao.valor, avaliacao.quantidade, valorNovaAvaliacao);
	}
}
