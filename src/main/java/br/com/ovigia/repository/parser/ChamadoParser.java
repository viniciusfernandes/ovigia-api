package br.com.ovigia.repository.parser;

import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ChamadoParser {
	private ChamadoParser() {
	}

	public static Document toDoc(Chamado chamado) {
		var doc = new Document("_id", chamado.id);
		doc.append("idCliente", chamado.idCliente);
		doc.append("data", chamado.data);

		var idRonda = chamado.idRonda;
		var docId = new Document("idVigia", idRonda.idVigia).append("data", idRonda.dataRonda);
		doc.append("idRonda", docId);

		doc.append("nomeCliente", chamado.nomeCliente);
		doc.append("logradouro", chamado.logradouro);
		doc.append("situacao", chamado.situacao.toString());

		var localizacao = chamado.localizacao;
		var docLocalizacao = new Document("latitude", localizacao.latitude).append("longitude", localizacao.longitude);

		doc.append("localizacao", docLocalizacao);
		return doc;
	}

	public static Chamado fromDoc(Document doc) {
		var chamado = new Chamado();
		chamado.id = doc.getString("_id");
		chamado.idCliente = doc.getString("idCliente");
		chamado.data = doc.getDate("data");
		var docIdronda = doc.get("idRonda", Document.class);

		chamado.idRonda = docIdronda != null ? new IdRonda(docIdronda.getString("idVigia"), docIdronda.getDate("data"))
				: null;
		chamado.nomeCliente = doc.getString("nomeCliente");
		chamado.logradouro = doc.getString("logradouro");

		var docSituacao = doc.getString("situacao");
		if (docSituacao != null) {
			chamado.situacao = TipoSituacaoChamado.valueOf(docSituacao);
		}
		var docLocal = doc.get("localizacao", Document.class);
		if (docLocal != null) {
			chamado.localizacao = new Localizacao(docLocal.getDouble("latitude"), docLocal.getDouble("longitude"));
		}
		return chamado;
	}

	public static List<Chamado> fromDoc(List<Document> docs) {
		var chamados = new ArrayList<Chamado>();
		docs.forEach(doc -> chamados.add(fromDoc(doc)));
		return chamados;
	}

}
