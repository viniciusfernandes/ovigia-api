package br.com.ovigia.repository.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import br.com.ovigia.model.Localizacao;

public class LocalizacaoParser {
	private LocalizacaoParser() {
	}

	public static Document toDoc(Localizacao localizacao) {
		var doc = new Document("latitude", localizacao.latitude);
		doc.append("longitude", localizacao.longitude);
		doc.append("timestamp", localizacao.timestamp);
		doc.append("velocidade", localizacao.velocidade);
		return doc;
	}

	public static List<Document> toDoc(List<Localizacao> localizacoes) {
		var docLocalizacoes = new ArrayList<Document>(localizacoes.size());
		localizacoes.forEach(locl -> docLocalizacoes.add(toDoc(locl)));
		return docLocalizacoes;
	}

	public static Localizacao fromDoc(Document doc) {
		if (doc == null) {
			return null;
		}
		var localizacao = new Localizacao();
		localizacao.latitude = doc.getDouble("latitude");
		localizacao.longitude = doc.getDouble("longitude");
		localizacao.data = doc.getDate("data");
		localizacao.timestamp = doc.getLong("timestamp");
		localizacao.velocidade = doc.getDouble("velocidade");
		return localizacao;
	}

	public static List<Localizacao> fromDoc(List<Document> docs) {
		return docs.stream().map(doc -> fromDoc(doc)).collect(Collectors.toList());
	}

}
