package br.com.ovigia.auth.repository;

import java.util.Arrays;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Usuario;
import br.com.ovigia.repository.parser.UsuarioParser;
import reactor.core.publisher.Mono;

public class UsuarioRepository {

	private final MongoCollection<Document> collection;

	public UsuarioRepository(MongoDatabase database) {
		collection = database.getCollection("usuario");
	}

	public Mono<Usuario> obterUsuario(String email, String password) {
		var filtro = new Document("email", email).append("password", password);
		var match = new Document("$match", filtro);
		var fields = new Document("tipoUsuario", 1).append("_id", 1).append("email", 1).append("nome", 1)
				.append("telefone", 1).append("localizacao", 1);
		var project = new Document("$project", fields);

		var list = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(list)).map(docUsuario -> UsuarioParser.fromDoc(docUsuario));
	}

	public Mono<Usuario> obterNomeETelefoneUsuario(String idUsuario) {
		var match = new Document("$match", new Document("_id", idUsuario));
		var fields = new Document("nome", 1).append("telefone", 1);
		var project = new Document("$project", fields);
		var list = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(list)).map(docUsuario -> UsuarioParser.fromDoc(docUsuario));
	}

	public Mono<Void> criarUsuario(Usuario usuario) {
		usuario.id = UUID.randomUUID().toString();
		var doc = UsuarioParser.toDoc(usuario);
		return Mono.from(collection.insertOne(doc)).then();
	}

}
