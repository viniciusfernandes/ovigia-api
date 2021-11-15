package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.SolicitacaoVisita;

public class SolicitacaoVistiaParser {
	private SolicitacaoVistiaParser() {
	}

	public static Document toDoc(SolicitacaoVisita solicitacao) {
		var doc = new Document("_id", solicitacao.idCliente).append("idVigia", solicitacao.idVigia)
				.append("data", solicitacao.data).append("nomeCliente", solicitacao.nomeCliente)
				.append("telefoneCliente", solicitacao.telefoneCliente);
		if (solicitacao.localizacaoCliente != null) {
			doc.append("localizacaoCliente", LocalizacaoParser.toDoc(solicitacao.localizacaoCliente));
		}
		return doc;
	}

	public static SolicitacaoVisita fromDoc(Document doc) {
		var solicitacao = new SolicitacaoVisita();
		solicitacao.idCliente = doc.getString("_id");
		solicitacao.data = doc.getDate("data");
		solicitacao.idVigia = doc.getString("idVigia");
		solicitacao.nomeCliente = doc.getString("nomeCliente");
		solicitacao.telefoneCliente = doc.getString("telefoneCliente");
		solicitacao.localizacaoCliente = LocalizacaoParser.fromDoc(doc.get("localizacaoCliente", Document.class));
		return solicitacao;

	}

}
