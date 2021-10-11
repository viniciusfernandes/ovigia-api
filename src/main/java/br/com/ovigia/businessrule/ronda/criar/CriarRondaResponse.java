package br.com.ovigia.businessrule.ronda.criar;

public class CriarRondaResponse {
	public double distanciaTotal;
	public double tempoTotal;
	public char escalaTempo;

	public CriarRondaResponse() {
	}

	public CriarRondaResponse(double distanciaTotal, double tempoTotal, char escalaTempo) {
		this.distanciaTotal = distanciaTotal;
		this.tempoTotal = tempoTotal;
		this.escalaTempo = escalaTempo;
	}
}
