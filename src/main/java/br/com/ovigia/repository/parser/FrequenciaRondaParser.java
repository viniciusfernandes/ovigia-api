package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.IdFrequenciaRonda;

public class FrequenciaRondaParser {
	private FrequenciaRondaParser() {
	}

	public static Document toDoc(FrequenciaRonda frequencia) {
		var id = new Document("idCliente", frequencia.id.idCliente).append("dataRonda", frequencia.id.dataRonda);
		var doc = new Document("_id", id);
		doc.append("totalRonda", frequencia.totalRonda);
		doc.append("idVigia", frequencia.idVigia);
		return doc;
	}

	public static Document toDocFlat(FrequenciaRonda frequencia) {
		var doc = new Document("data", frequencia.id.dataRonda);
		doc.append("totalRonda", frequencia.totalRonda);
		doc.append("idVigia", frequencia.idVigia);
		return doc;
	}

	public static FrequenciaRonda fromDoc(Document doc) {
		if (doc == null) {
			return null;
		}
		var frequencia = new FrequenciaRonda();
		var id = doc.get("_id", Document.class);
		frequencia.id = new IdFrequenciaRonda(id.getString("idCliente"), id.getDate("dataRonda"));
		frequencia.idVigia = doc.getString("idVigia");
		frequencia.totalRonda = doc.getInteger("totalRonda");
		frequencia.nomeVigia = doc.getString("nomeVigia");
		return frequencia;
	}
}
