package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.ResumoRonda;

public class ResumoRondaParser {
	private ResumoRondaParser() {
	}

	public static Document toDoc(ResumoRonda resumoRonda) {
		var doc = new Document("_id", resumoRonda.idVigia);
		appendFields(resumoRonda, doc);
		return doc;
	}

	public static void appendFields(ResumoRonda resumoRonda, Document doc) {
		doc.append("data", resumoRonda.data);
		doc.append("distancia", resumoRonda.distancia);
		doc.append("escalaTempo", resumoRonda.escalaTempo);
		doc.append("tempo", resumoRonda.tempo);
		doc.append("totalChamados", resumoRonda.totalChamados);
	}

	public static Document toDocFields(ResumoRonda resumoRonda) {
		var doc = new Document();
		appendFields(resumoRonda, doc);
		return doc;
	}

	public static ResumoRonda fromDoc(Document doc) {
		var resumo = new ResumoRonda();
		resumo.idVigia = doc.getString("_id");
		resumo.distancia = doc.getDouble("distancia");
		resumo.escalaTempo = doc.getString("escalaTempo").charAt(0);
		resumo.data = doc.getDate("data");
		resumo.tempo = doc.getDouble("tempo");
		resumo.totalChamados = doc.getLong("totalChamados");
		return resumo;
	}
}
