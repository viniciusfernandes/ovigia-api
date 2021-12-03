package br.com.ovigia.model;

public class Faturamento {
	public IdFaturamento id;
	public Integer quantidadePagamentos;
	public Double valor;

	public Faturamento() {
	}

	public Faturamento(IdFaturamento id, Double valor) {
		this.id = id;
		this.quantidadePagamentos = 1;
		this.valor = valor;
	}
}
