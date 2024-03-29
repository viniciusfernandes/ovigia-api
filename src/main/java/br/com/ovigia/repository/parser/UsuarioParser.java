package br.com.ovigia.repository.parser;

import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.enumeration.TipoUsuario;
import org.bson.Document;

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
		doc.append("dataInicio", usuario.dataInicio);
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
		usuario.dataInicio = doc.getDate("dataInicio");
		usuario.localizacao = LocalizacaoParser.fromDoc(doc.get("localizacao", Document.class));

		return usuario;
	}

	public static Usuario fromDoc(Document doc) {
		return fromDoc(new Usuario(), doc);
	}

}
