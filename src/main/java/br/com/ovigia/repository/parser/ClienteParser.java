package br.com.ovigia.repository.parser;

import java.util.ArrayList;

import org.bson.Document;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;

public class ClienteParser {
	private ClienteParser() {
	}

	public static Document toDoc(Cliente cliente) {

		var doc = new Document("_id", cliente.getId());
		doc.append("nome", cliente.getNome());
		doc.append("email", cliente.getEmail());
		doc.append("telefone", cliente.getTelefone());

		var localizacao = cliente.getLocalizacao();
		if (localizacao != null) {
			var docLocalizacao = toDoc(localizacao);
			doc.append("localizacao", docLocalizacao);
		}

		if (cliente.hasVigias()) {
			var idVigias = new ArrayList<String>();
			cliente.getVigias().forEach(id -> idVigias.add(id));

			doc.append("vigias", idVigias);
		}

		return doc;
	}

	public static Document toDoc(Localizacao localizacao) {

		var docLocalizacao = new Document("latitude", localizacao.getLatitude());
		docLocalizacao.append("longitude", localizacao.getLongitude());
		docLocalizacao.append("data", localizacao.getData());

		return docLocalizacao;
	}

	public static Cliente fromDoc(Document doc) {

		var docLoc = doc.get("localizacao", Document.class);
		Localizacao localizacao = null;
		if (docLoc != null) {
			localizacao = new Localizacao();
			localizacao.setLatitude(docLoc.getDouble("latitude"));
			localizacao.setLongitude(docLoc.getDouble("longitude"));
			localizacao.setData(docLoc.getDate("data"));
		}

		var cliente = new Cliente();
		cliente.setId(doc.getString("_id"));
		cliente.setNome(doc.getString("nome"));
		cliente.setEmail(doc.getString("email"));
		cliente.setTelefone(doc.getString("telefone"));
		cliente.setLocalizacao(localizacao);

		cliente.setVigias(doc.getList("vigias", String.class));

		return cliente;
	}

}
