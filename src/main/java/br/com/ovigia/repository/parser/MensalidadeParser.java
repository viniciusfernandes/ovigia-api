package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Mensalidade;

public class MensalidadeParser {
	private MensalidadeParser() {
	}

	public static Document toDoc(Mensalidade mensalidade) {
		var doc = new Document("_id", mensalidade.id);
		doc.append("idContrato", mensalidade.idContrato);
		doc.append("dataVencimento", mensalidade.dataVencimento);
		doc.append("nomeCliente", mensalidade.nomeCliente);
		doc.append("telefoneCliente", mensalidade.telefoneCliente);
		doc.append("valor", mensalidade.valor);
		return doc;
	}

	public static Mensalidade fromDoc(Document doc) {
		var mensalidade = new Mensalidade();
		mensalidade.dataVencimento = doc.getDate("dataVencimento");
		mensalidade.id = doc.getString("id");
		mensalidade.idContrato = doc.getString("idContrato");
		mensalidade.nomeCliente = doc.getString("nomeCliente");
		mensalidade.telefoneCliente = doc.getString("telefoneCliente");
		mensalidade.valor = doc.getDouble("valor");
		return mensalidade;
	}

}
