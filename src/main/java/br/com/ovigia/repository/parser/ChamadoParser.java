package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Chamado;

public class ChamadoParser {
	private ChamadoParser() {
	}

	public static Document toDoc(Chamado chamado) {
		var doc = new Document("_id", chamado.id);
		doc.append("idCliente", chamado.idCliente);
		doc.append("data", chamado.data);
		doc.append("idVigia", chamado.idVigia);
		doc.append("situacao", chamado.situacao);
		return doc;
	}

	public static Chamado fromDoc(Document doc) {
		return null;
	}

}
