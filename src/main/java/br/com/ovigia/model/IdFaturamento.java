package br.com.ovigia.model;

import java.util.Objects;

public class IdFaturamento {
	public String idVigia;
	public Integer mes;
	public Integer ano;

	public IdFaturamento(String idVigia, Integer mes, Integer ano) {
		this.idVigia = idVigia;
		this.mes = mes;
		this.ano = ano;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IdFaturamento that = (IdFaturamento) o;
		return Objects.equals(idVigia, that.idVigia) && Objects.equals(mes, that.mes) && Objects.equals(ano, that.ano);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idVigia, mes, ano);
	}
}
