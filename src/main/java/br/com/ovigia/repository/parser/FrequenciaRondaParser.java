package br.com.ovigia.repository.parser;

import br.com.ovigia.model.FrequenciaRonda;
import org.bson.Document;

public class FrequenciaRondaParser {
	private FrequenciaRondaParser() {
	}

	public static Document toDoc(FrequenciaRonda frequencia) {
		var doc = new Document("dataUltimaRonda", frequencia.dataUltimaRonda);
		doc.append("totalRonda", frequencia.totalRonda);
		doc.append("idVigia", frequencia.idVigia);
		doc.append("nomeVigia", frequencia.nomeVigia);
		doc.append("dataAtualizacaoRonda", frequencia.dataAtualizacaoRonda);
		return doc;
	}

	public static FrequenciaRonda fromDoc(Document doc) {
		if (doc == null) {
			return null;
		}
		var frequencia = new FrequenciaRonda();
		frequencia.dataUltimaRonda = doc.getDate("dataUltimaRonda");
		frequencia.idVigia = doc.getString("idVigia");
		frequencia.totalRonda = doc.getInteger("totalRonda");
		frequencia.nomeVigia = doc.getString("nomeVigia");
		frequencia.dataAtualizacaoRonda = doc.getDate("dataAtualizacaoRonda");
		return frequencia;
	}

}
