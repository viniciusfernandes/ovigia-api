package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.SolicitacaoVistiaParser.fromDoc;
import static br.com.ovigia.repository.parser.SolicitacaoVistiaParser.toDoc;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.SolicitacaoVisita;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SolicitacaoVisitaRepository {
	private final MongoCollection<Document> collection;

	public SolicitacaoVisitaRepository(MongoDatabase database) {
		collection = database.getCollection("solicitacaoVisita");
	}

	public Mono<Void> criarSolicitacao(SolicitacaoVisita solicitacao) {
		var doc = toDoc(solicitacao);
		return Mono.from(collection.insertOne(doc)).then();
	}

	public Flux<SolicitacaoVisita> obterSolicitacaoByIdVigia(String idvigia) {
		var match = new Document("$match", new Document("idVigia", idvigia));
		var project = new Document("idVigia", 0);
		var pipeline = Arrays.asList(match, project);
		return Flux.from(collection.aggregate(pipeline)).map(doc -> fromDoc(doc));
	}

	public Mono<Void> removerSolicitacao(String idCliente) {
		return Mono.from(collection.deleteOne(new Document("_id", idCliente))).then();
	}

}