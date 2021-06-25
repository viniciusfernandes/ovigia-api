package br.com.ovigia.auth.repository;

import org.bson.Document;
import org.springframework.stereotype.Repository;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.auth.model.Usuario;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioRepository {

	private final MongoCollection<Document> collection;

	public UsuarioRepository(MongoDatabase database) {
		collection = database.getCollection("usuario");
	}

	public Mono<String> obterPorEmail(String email) {
		return Mono.from(collection.find(new Document("_id", email))).map(docUsuario -> {
			return docUsuario.getString("password");
		});
	}

	public Mono<Void> criarUsuario(Usuario usuario) {
		var docUsuario = new Document("_id", usuario.getEmail()).append("password", usuario.getPassword());
		return Mono.from(collection.insertOne(docUsuario)).then();
	}

}
