package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.enumeration.TipoUsuario;

public class UsuarioParser {
	private UsuarioParser() {
	}

	public static Document toDoc(Usuario usuario) {

		var doc = new Document("_id", usuario.id);
		doc.append("nome", usuario.nome);
		doc.append("email", usuario.email);
		doc.append("telefone", usuario.telefone);
		if (usuario.tipoUsuario != null) {
			doc.append("tipoUsuario", usuario.tipoUsuario.toString());
		}

		if (usuario.password != null) {
			doc.append("password", usuario.password);
		}
		var localizacao = usuario.localizacao;
		if (localizacao != null) {
			var docLocalizacao = LocalizacaoParser.toDoc(localizacao);
			doc.append("localizacao", docLocalizacao);
		}

		return doc;
	}

	public static <T extends Usuario> T fromDoc(T usuario, Document doc) {
		usuario.id = doc.getString("_id");
		usuario.nome = doc.getString("nome");
		usuario.email = doc.getString("email");

		usuario.telefone = doc.getString("telefone");
		var tipo = doc.getString("tipoUsuario");
		if (tipo != null) {
			usuario.tipoUsuario = TipoUsuario.valueOf(tipo);
		}
		usuario.localizacao = LocalizacaoParser.fromNestedDoc(doc);
		return usuario;
	}

	public static Usuario fromDoc(Document doc) {
		return fromDoc(new Usuario(), doc);
	}

}
