package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Contrato;

public class ContratoParser {
	private ContratoParser() {
	}

	public static Document toDoc(Contrato contrato) {
		return new Document("_id", contrato.idCliente).append("idVigia", contrato.idVigia)
				.append("dataInicio", contrato.dataInicio).append("dataFim", contrato.dataFim)
				.append("valor", contrato.valor);
	}

	public static Contrato fromDoc(Document doc) {
		var solicitacao = new Contrato();
		solicitacao.idCliente = doc.getString("_id");
		solicitacao.dataInicio = doc.getDate("dataInicio");
		solicitacao.dataFim = doc.getDate("dataFim");
		solicitacao.idVigia = doc.getString("idVigia");
		solicitacao.valor = doc.getDouble("valor");
		return solicitacao;
	}

}
