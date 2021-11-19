package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.IdFrequenciaRonda;

public class FrequenciaRondaParser {
	private FrequenciaRondaParser() {
	}

	public static Document toDoc(FrequenciaRonda frequencia) {
		var id = new Document("idCliente", frequencia.id.idCliente).append("data", frequencia.id.data);
		var doc = new Document("_id", id);
		doc.append("totalRonda", frequencia.totalRonda);
		doc.append("idVigia", frequencia.idVigia);
		return doc;
	}

	public static Document toDocFlat(FrequenciaRonda frequencia) {
		var doc = new Document("data", frequencia.id.data);
		doc.append("totalRonda", frequencia.totalRonda);
		doc.append("idVigia", frequencia.idVigia);
		return doc;
	}

	public static FrequenciaRonda fromDoc(Document doc) {
		var frequencia = new FrequenciaRonda();
		if (doc == null) {
			return frequencia;
		}
		var id = doc.get("_id", Document.class);
		frequencia.id = new IdFrequenciaRonda(id.getString("idCliente"), id.getDate("data"));
		frequencia.idVigia = doc.getString("idVigia");
		frequencia.totalRonda = doc.getInteger("totalRonda");
		return frequencia;
	}
}
