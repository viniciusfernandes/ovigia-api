package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Vigia;

public class VigiaParser {
	private VigiaParser() {
	}

	public static Document toDoc(Vigia vigia) {

		var docvigia = new Document("_id", vigia.getId());
		docvigia.append("nome", vigia.getNome());
		docvigia.append("email", vigia.getEmail());
		docvigia.append("telefone", vigia.getTelefone());
		docvigia.append("localizacao", LocalizacaoParser.toDoc(vigia.getLocalizacao()));
		return docvigia;
	}

	public static Vigia fromDoc(Document doc) {
		var vigia = new Vigia();
		vigia.setId(doc.getString("_id"));
		vigia.setNome(doc.getString("nome"));
		vigia.setEmail(doc.getString("email"));
		vigia.setTelefone(doc.getString("telefone"));
		vigia.setLocalizacao(LocalizacaoParser.fromDoc(doc));
		return vigia;
	}

}
