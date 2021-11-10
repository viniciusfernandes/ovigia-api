package br.com.ovigia.model.repository;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.enumeration.TipoUsuario;
import br.com.ovigia.repository.parser.ClienteParser;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import br.com.ovigia.repository.parser.VigiaParser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VigiaRepository {
	private final MongoCollection<Document> collection;

	public VigiaRepository(MongoDatabase database) {
		collection = database.getCollection("usuario");
	}

	@Deprecated
	public Mono<Void> criar(Vigia vigia) {
		var doc = VigiaParser.toDoc(vigia);
		return Mono.from(collection.insertOne(doc)).then();
	}

	public Mono<Vigia> obterPorId(String idVigia) {
		var mvigia = collection.find(new Document("_id", idVigia));
		return Mono.from(mvigia).map(doc -> VigiaParser.fromDoc(doc));
	}

	public Flux<Vigia> obterLocalizacaoVigias() {
		var match = new Document("$match", new Document("tipoUsuario", TipoUsuario.VIGIA.toString()));
		var fields = new Document("_id", 1).append("localizacao", 1).append("nome", 1).append("dataInicio", 1)
				.append("telefone", 1	);
		var project = new Document("$project", fields);
		var pipeline = Arrays.asList(match, project);
		return Flux.from(collection.aggregate(pipeline)).map(doc -> VigiaParser.fromDoc(doc));
	}

	public Mono<Void> atualizarCliente(String idVigia, Cliente cliente) {
		var docId = new Document("_id", idVigia);
		var docCliente = ClienteParser.toDoc(cliente);

		var adicionarCliente = new Document("$push", new Document("clientes", docCliente));
		return Mono.from(collection.updateOne(docId, adicionarCliente)).then();
	}

	public Mono<Void> atualizarLocalizacaoPorId(String idVigia, Localizacao localizacao) {
		var docLocalizacao = new Document("localizacao", LocalizacaoParser.toDoc(localizacao));
		var update = new Document("$set", docLocalizacao);
		return Mono.from(collection.updateOne(new Document("_id", idVigia), update)).then();
	}
}
