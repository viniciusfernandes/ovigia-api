package br.com.ovigia.repository.parser;

import java.util.Date;

import org.bson.Document;

import br.com.ovigia.model.IdRonda;

public class IdRondaParser {
	private IdRondaParser() {
	}

	public static Document toDoc(IdRonda id) {
		return toDoc(id.idVigia, id.dataRonda);
	}

	public static Document toDoc(String idVigia, Date dataRonda) {
		var value = new Document().append("idVigia", idVigia).append("dataRonda", dataRonda);
		return new Document("_id", value);
	}

	public static IdRonda fromDoc(Document doc) {
		var docId = doc.get("_id", Document.class);
		return new IdRonda(docId.getString("idVigia"), docId.getDate("dataRonda"));
	}

}
