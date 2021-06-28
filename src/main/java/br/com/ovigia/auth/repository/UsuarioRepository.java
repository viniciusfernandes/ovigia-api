package br.com.ovigia.auth.repository;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.auth.model.Usuario;
import reactor.core.publisher.Mono;

public class UsuarioRepository {

	private final MongoCollection<Document> collection;

	public UsuarioRepository(MongoDatabase database) {
		collection = database.getCollection("usuario");
	}

	public Mono<Boolean> isUsuarioExistente(String email, String password) {
		var filtro = new Document("_id", email).append("password", password);
		var match = new Document("$match", filtro);
		var fields = new Document("_id", 1);
		var project = new Document("$project", fields);

		var list = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(list)).map(docUsuario -> {
			return true;
		}).switchIfEmpty(Mono.just(false));
	}

	public Mono<Void> criarUsuario(Usuario usuario) {
		var docUsuario = new Document("_id", usuario.getEmail()).append("password", usuario.getPassword());
		return Mono.from(collection.insertOne(docUsuario)).then();
	}

	public Mono<Void> criarUsuario(String email, String password) {
		var docUsuario = new Document("_id", email).append("password", password);
		return Mono.from(collection.insertOne(docUsuario)).then();
	}

}
