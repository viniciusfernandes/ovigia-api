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

	public static Localizacao fromDoc(Document doc) {

		var docLoc = doc.get("localizacao", Document.class);
		Localizacao localizacao = null;
		if (docLoc != null) {
			localizacao = new Localizacao();
			localizacao.setLatitude(docLoc.getDouble("latitude"));
			localizacao.setLongitude(docLoc.getDouble("longitude"));
			localizacao.setData(docLoc.getDate("data"));
		}

		return localizacao;
	}

}
