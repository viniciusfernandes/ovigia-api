package br.com.ovigia.repository.parser;

import br.com.ovigia.model.Avaliacao;
import org.bson.Document;

public class AvaliacaoParser {
	private AvaliacaoParser() {
	}

	public static Document toDoc(Avaliacao avaliacao) {
		if (avaliacao == null) {
			return null;
		}
		var doc = new Document();
		doc.append("valor", avaliacao.valor);
		doc.append("quantidade", avaliacao.quantidade);
		return doc;
	}

	public static Avaliacao fromDoc(Document doc) {
		var avaliacao = new Avaliacao();
		if (doc == null) {
			return avaliacao;
		}
		avaliacao.quantidade = doc.getInteger("quantidade");
		avaliacao.valor = doc.getDouble("valor");
		return avaliacao;
	}
}
