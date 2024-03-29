package br.com.ovigia.repository.parser;

import br.com.ovigia.model.Cliente;
import org.bson.Document;

public class ClienteParser {
	private ClienteParser() {
	}

	public static Document toDoc(Cliente cliente) {
		var doc = UsuarioParser.toDoc(cliente);
		doc.append("idVigia", cliente.idVigia);
		return doc;
	}

	public static Cliente fromDoc(Document doc) {
		var cliente = UsuarioParser.fromDoc(new Cliente(), doc);
		cliente.idVigia = doc.getString("idVigia");
		return cliente;
	}

}
