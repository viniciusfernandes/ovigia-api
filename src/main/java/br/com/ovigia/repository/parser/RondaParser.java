package br.com.ovigia.repository.parser;

import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.enumeration.TipoSituacaoRonda;
import org.bson.Document;

public class RondaParser {
	private RondaParser() {
	}

	public static Document toDoc(Ronda ronda) {
		var doc = IdRondaParser.toDoc(ronda.id);
		doc.append("inicio", ronda.inicio);
		doc.append("fim", ronda.fim);
		doc.append("situacao", ronda.situacao.toString());
		doc.append("dataAtualizacao", ronda.dataAtualizacao);
		doc.append("localizacoes", LocalizacaoParser.toDoc(ronda.localizacoes));
		return doc;
	}

	public static Ronda fromDoc(Document doc) {
		var id = IdRondaParser.fromDoc(doc);

		var ronda = new Ronda(id);
		var situacao = doc.getString("situacao");
		if (situacao != null) {
			ronda.situacao = TipoSituacaoRonda.valueOf(situacao);
		}
		ronda.inicio = doc.getDate("inicio");
		ronda.fim = doc.getDate("fim");
		ronda.dataAtualizacao = doc.getDate("dataAtualizacao");

		var docsLocal = doc.getList("localizacoes", Document.class);
		ronda.add(LocalizacaoParser.fromDoc(docsLocal));
		return ronda;
	}

}
