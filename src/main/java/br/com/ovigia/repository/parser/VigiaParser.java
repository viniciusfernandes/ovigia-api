package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Vigia;

public class VigiaParser {
	private VigiaParser() {
	}

	public static Document toDoc(Vigia vigia) {
		var doc = UsuarioParser.toDoc(vigia);
		doc.append("avaliacao", vigia.avaliacao);
		return doc;
	}

	public static Vigia fromDoc(Document doc) {
		var vigia = UsuarioParser.fromDoc(new Vigia(), doc);
		vigia.avaliacao = doc.getDouble("avaliacao");
		return vigia;
	}
}
