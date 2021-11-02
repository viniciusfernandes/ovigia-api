package br.com.ovigia.repository.parser;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import br.com.ovigia.model.Id;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.enumeration.TipoSituacaoRonda;

public class RondaParser {
	private RondaParser() {
	}

	public static Document toDoc(Ronda ronda) {
		var doc = toIdDoc(ronda.id);
		doc.append("inicio", ronda.inicio);
		doc.append("fim", ronda.fim);
		doc.append("situacao", ronda.situacao.toString());
		doc.append("distancia", ronda.distancia);
		doc.append("localizacoes", LocalizacaoParser.toDoc(ronda.localizacoes));
		return doc;
	}

	public static Ronda fromDoc(Document doc) {
		var docId = doc.get("_id", Document.class);

		var id = new Id();
		id.idVigia = docId.getString("idVigia");
		id.data = docId.getDate("data");

		var ronda = new Ronda(id);
		var situacao = doc.getString("situacao");
		if (situacao != null) {
			ronda.situacao = TipoSituacaoRonda.valueOf(situacao);
		}
		ronda.inicio = doc.getDate("inicio");
		ronda.fim = doc.getDate("fim");

		@SuppressWarnings("unchecked")
		var localizacoes = (List<Document>) doc.get("localizacoes");
		if (localizacoes != null) {
			for (Document docLoc : localizacoes) {
				ronda.add(LocalizacaoParser.fromNestedDoc(docLoc));
			}
		}
		return ronda;
	}

	public static Document toIdDoc(Id id) {
		return toIdDoc(id.idVigia, id.data);
	}

	public static Document toIdDoc(String idVigia, Date data) {
		var value = new Document().append("idVigia", idVigia).append("data", data);
		return new Document("_id", value);
	}
}
