package br.com.ovigia.repository.parser;

import java.util.ArrayList;

import org.bson.Document;

import br.com.ovigia.model.Cliente;

public class ClienteParser {
	private ClienteParser() {
	}

	public static Document toDoc(Cliente cliente) {

		var doc = UsuarioParser.toDoc(cliente);

		if (cliente.hasVigias()) {
			var idVigias = new ArrayList<String>();
			cliente.vigias.forEach(id -> idVigias.add(id));

			doc.append("vigias", idVigias);
		}

		return doc;
	}

	public static Cliente fromDoc(Document doc) {
		var cliente = UsuarioParser.fromDoc(new Cliente(), doc);
		cliente.vigias = doc.getList("vigias", String.class);

		return cliente;
	}

}
