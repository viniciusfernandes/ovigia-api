package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.enumeration.TipoUsuario;

public class UsuarioParser {
	private UsuarioParser() {
	}

	public static Document toDoc(Usuario usuario) {

		var doc = new Document("_id", usuario.email);
		doc.append("nome", usuario.nome);
		doc.append("telefone", usuario.telefone);
		doc.append("tipoUsuario", usuario.tipoUsuario.toString());

		var localizacao = usuario.localizacao;
		if (localizacao != null) {
			var docLocalizacao = LocalizacaoParser.toDoc(localizacao);
			doc.append("localizacao", docLocalizacao);
		}

		return doc;
	}

	public static <T extends Usuario> T fromDoc(T t, Document doc) {
		t.nome = doc.getString("nome");
		t.email = doc.getString("_id");
		t.telefone = doc.getString("telefone");
		t.tipoUsuario = TipoUsuario.valueOf(doc.getString("tipoUsuario"));
		t.localizacao = LocalizacaoParser.fromNestedDoc(doc);
		return t;
	}

}
