package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Localizacao;

public class LocalizacaoParser {
	private LocalizacaoParser() {
	}

	public static Document toDoc(Localizacao localizacao) {
		var docLocalizacao = new Document("latitude", localizacao.latitude);
		docLocalizacao.append("longitude", localizacao.longitude);
		docLocalizacao.append("data", localizacao.data);
		return docLocalizacao;
	}

	public static Localizacao fromNestedDoc(Document doc) {
		var docLoc = doc.get("localizacao", Document.class);
		if (docLoc != null) {
			return fromDoc(docLoc);
		}

		return null;
	}

	public static Localizacao fromDoc(Document doc) {
		var localizacao = new Localizacao();
		localizacao.latitude = doc.getDouble("latitude");
		localizacao.longitude = doc.getDouble("longitude");
		localizacao.data = doc.getDate("data");
		return localizacao;
	}

}
