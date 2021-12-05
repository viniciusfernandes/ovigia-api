package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.IdFrequenciaRonda;
import br.com.ovigia.model.ResumoFrequenciaRonda;

public class ResumoFrequenciaRondaParser {
	private ResumoFrequenciaRondaParser() {
	}

	public static Document toDoc(ResumoFrequenciaRonda frequencia) {
		var doc = new Document("_id",
				new Document("idCliente", frequencia.id.idCliente).append("dataRonda", frequencia.id.dataRonda));
		doc.append("totalRonda", frequencia.totalRonda);
		doc.append("idVigia", frequencia.idVigia);
		return doc;
	}

	public static ResumoFrequenciaRonda fromDoc(Document doc) {
		if (doc == null) {
			return null;
		}
		var frequencia = new ResumoFrequenciaRonda();
		var id = doc.get("_id", Document.class);
		if (id != null) {
			frequencia.id = new IdFrequenciaRonda(id.getString("idCliente"), id.getDate("dataRonda"));
		}
		frequencia.idVigia = doc.getString("idVigia");
		frequencia.totalRonda = doc.getInteger("totalRonda");
		frequencia.nomeVigia = doc.getString("nomeVigia");
		return frequencia;
	}

}
