package br.com.ovigia.repository.parser;

import br.com.ovigia.model.Faturamento;
import br.com.ovigia.model.IdFaturamento;
import org.bson.Document;

public class FaturamentoParser {
	private FaturamentoParser() {
	}

	public static Document toDoc(Faturamento faturamento) {
		var doc = toDoc(faturamento.id).append("quantidadePagamentos", faturamento.quantidadePagamentos).append("valor",
				faturamento.valor);
		return doc;
	}

	public static Document toDoc(IdFaturamento id) {
		return new Document("_id", new Document("idVigia", id.idVigia).append("ano", id.ano).append("mes", id.mes));
	}

	public static Faturamento fromDoc(Document doc) {
		if (doc == null) {
			return null;
		}
		var faturamento = new Faturamento();
		var id = doc.get("_id", Document.class);
		if (id != null) {
			faturamento.id = new IdFaturamento(id.getString("idVigia"), id.getInteger("mes"), id.getInteger("ano"));
		}
		faturamento.quantidadePagamentos = doc.getInteger("quantidadePagamentos");
		faturamento.valor = doc.getDouble("valor");
		return faturamento;
	}

}
