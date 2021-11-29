package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.ResumoRonda;

public class ResumoRondaParser {
	private ResumoRondaParser() {
	}

	public static Document toDoc(ResumoRonda resumoRonda) {
		var doc = IdRondaParser.toDoc(resumoRonda.id);
		appendFields(resumoRonda, doc);
		return doc;
	}

	public static void appendFields(ResumoRonda resumoRonda, Document doc) {
		doc.append("distancia", resumoRonda.distancia);
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
		resumo.id = IdRondaParser.fromDoc(doc);
		resumo.distancia = doc.getDouble("distancia");
		resumo.tempo = doc.getLong("tempo");
		resumo.totalChamados = doc.getLong("totalChamados");
		return resumo;
	}

}
