package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Vigia;

public class VigiaParser {
	private VigiaParser() {
	}

	public static Document toDoc(Vigia vigia) {

		var docvigia = new Document("_id", vigia.email);
		docvigia.append("nome", vigia.nome);
		docvigia.append("telefone", vigia.telefone);
		docvigia.append("localizacao", LocalizacaoParser.toDoc(vigia.localizacao));
		return docvigia;
	}

	public static Vigia fromDoc(Document doc) {
		return UsuarioParser.fromDoc(new Vigia(), doc);
	}

}
