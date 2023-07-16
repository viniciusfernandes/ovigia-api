package br.com.ovigia.repository.parser;

import br.com.ovigia.model.Vigia;
import org.bson.Document;

public class VigiaParser {
	private VigiaParser() {
	}

	public static Document toDoc(Vigia vigia) {
		var doc = UsuarioParser.toDoc(vigia);
		doc.append("avaliacao", AvaliacaoParser.toDoc(vigia.avaliacao));
		return doc;
	}

	public static Vigia fromDoc(Document doc) {
		var vigia = UsuarioParser.fromDoc(new Vigia(), doc);
		vigia.avaliacao = AvaliacaoParser.fromDoc(doc.get("avaliacao", Document.class));
		return vigia;
	}
}
