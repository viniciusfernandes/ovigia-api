package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Contrato;

public class ContratoParser {
	private ContratoParser() {
	}

	public static Document toDoc(Contrato contrato) {
		return new Document("_id", contrato.idCliente).append("idVigia", contrato.idVigia)
				.append("dataInicio", contrato.dataInicio).append("dataFim", contrato.dataFim)
				.append("valor", contrato.valor).append("diaVencimento", contrato.diaVencimento);
	}

	public static Contrato fromDoc(Document doc) {
		var contrato = new Contrato();
		contrato.idCliente = doc.getString("_id");
		contrato.dataInicio = doc.getDate("dataInicio");
		contrato.dataFim = doc.getDate("dataFim");
		contrato.idVigia = doc.getString("idVigia");
		contrato.valor = doc.getDouble("valor");
		contrato.diaVencimento = doc.getInteger("diaVencimento");
		return contrato;
	}

}
