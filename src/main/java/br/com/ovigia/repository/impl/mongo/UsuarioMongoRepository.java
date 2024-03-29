package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.Usuario;
import br.com.ovigia.model.repository.UsuarioRepository;
import br.com.ovigia.repository.parser.UsuarioParser;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.UUID;

public class UsuarioMongoRepository implements UsuarioRepository {

	private final MongoCollection<Document> collection;

	public UsuarioMongoRepository(MongoDatabase database) {
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

	public Mono<Void> criarUsuario(Usuario usuario) {
		usuario.id = UUID.randomUUID().toString();
		var doc =   UsuarioParser.toDoc(usuario);
		return Mono.from(collection.insertOne(doc)).then();
	}

}
