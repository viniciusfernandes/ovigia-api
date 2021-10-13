package br.com.ovigia.repository.parser;

import java.util.Date;

import org.bson.Document;

import br.com.ovigia.model.Id;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.Vigia;

public class RondaParser {
	private RondaParser() {
	}

	public static Document toDoc(Ronda ronda) {
		var docvigia = toIdDoc(ronda.id);
		docvigia.append("inicio", ronda.inicio);
		docvigia.append("fim", ronda.fim);
		docvigia.append("localizacoes", LocalizacaoParser.toDoc(ronda.localizacoes));
		return docvigia;
	}

	public static Vigia fromDoc(Document doc) {
		return UsuarioParser.fromDoc(new Vigia(), doc);
	}

	public static Document toIdDoc(Id id) {
		return toIdDoc(id.idVigia, id.data);
	}

	public static Document toIdDoc(String idVigia, Date data) {
		var value = new Document().append("idVigia", idVigia).append("data", data);
		return new Document("_id", value);
	}
}
