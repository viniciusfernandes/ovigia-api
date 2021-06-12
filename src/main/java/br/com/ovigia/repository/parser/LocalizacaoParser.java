package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Localizacao;

public class LocalizacaoParser {
	private LocalizacaoParser() {
	}

	public static Document toDoc(Localizacao localizacao) {

		var docLocalizacao = new Document("latitude", localizacao.getLatitude());
		docLocalizacao.append("longitude", localizacao.getLongitude());
		docLocalizacao.append("data", localizacao.getData());

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
		localizacao.setLatitude(doc.getDouble("latitude"));
		localizacao.setLongitude(doc.getDouble("longitude"));
		localizacao.setData(doc.getDate("data"));

		return localizacao;
	}

}
