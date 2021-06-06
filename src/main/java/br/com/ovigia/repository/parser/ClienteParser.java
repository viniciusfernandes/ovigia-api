package br.com.ovigia.repository.parser;

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
