package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.ContratoParser.fromDoc;
import static br.com.ovigia.repository.parser.ContratoParser.toDoc;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Contrato;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ContratoRepository {
	private final MongoCollection<Document> collection;

	public ContratoRepository(MongoDatabase database) {
		collection = database.getCollection("contrato");
	}

	public Mono<Void> criarContrato(Contrato contrato) {
		var doc = toDoc(contrato);
		return Mono.from(collection.insertOne(doc)).then();
	}

	public Flux<Contrato> obterContratosDiaVencimentoInferiorByIdVigia(String idVigia, Integer diaReferencia) {
		var filter = new Document("idVigia", idVigia).append("diaVencimento", new Document("$lte", diaReferencia));
		var match = new Document("$match", filter);
		var fields = new Document("valor", 1).append("idCliente", 1).append("diaVencimento", 1);
		var project = new Document("$project", fields);
		var sort = new Document("$sort", new Document("diaVencimento", 1));
		var pipeline = Arrays.asList(match, project, sort);
		return Flux.from(collection.aggregate(pipeline)).map(doc -> fromDoc(doc));
	}

	public Flux<Contrato> obterContratosByIdVigia(String idVigia) {
		return Flux.from(collection.find(new Document("idVigia", idVigia))).map(doc -> fromDoc(doc));
	}

	public Mono<Long> removerContrato(String idCliente) {
		return Mono.from(collection.deleteOne(new Document("_id", idCliente))).map(result -> result.getDeletedCount());
	}

	public Mono<String> obterIdVigiaByIdCliente(String idCliente) {
		var match = new Document("$match", new Document("_id", idCliente));
		var project = new Document("$project", new Document("idVigia", 1));
		var pipeline = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(pipeline)).map(doc -> doc.getString("idVigia"));

	}
}
