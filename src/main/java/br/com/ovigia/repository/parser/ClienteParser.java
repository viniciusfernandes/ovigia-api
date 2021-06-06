package br.com.ovigia.repository.parser;

import java.util.ArrayList;

import org.bson.Document;

import br.com.ovigia.model.Cliente;

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
			var docLocalizacao = new Document("latitude", localizacao.getLatitude());
			docLocalizacao.append("longitude", localizacao.getLongitude());
			doc.append("localicazao", docLocalizacao);
		}
		
		if (cliente.hasVigias()) {
			var idVigias = new ArrayList<String>();
			cliente.getVigias().forEach(id -> idVigias.add(id));

			doc.append("vigias", idVigias);
		}

		return doc;
	}

	public static Cliente fromDoc(Document doc) {
		var cliente = new Cliente();
		cliente.setId(doc.getString("_id"));
		cliente.setNome(doc.getString("nome"));
		cliente.setEmail(doc.getString("email"));
		cliente.setTelefone(doc.getString("telefone"));
		return cliente;
	}

}
