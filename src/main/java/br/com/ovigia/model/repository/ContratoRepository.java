package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.ContratoParser.fromDoc;
import static br.com.ovigia.repository.parser.ContratoParser.toDoc;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.enumeration.TipoSituacaoContrato;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ContratoRepository {
	private final MongoCollection<Document> collection;

	public ContratoRepository(MongoDatabase database) {
		collection = database.getCollection("contrato");
	}

	public Mono<String> criarContrato(Contrato contrato) {
		contrato.id = UUID.randomUUID().toString();
		var doc = toDoc(contrato);
		return Mono.from(collection.insertOne(doc)).thenReturn(contrato.id);
	}

	public Flux<Contrato> obterContratosDiaVencimentoInferiorByIdVigia(String idVigia, Integer dia, Integer mes) {
		var match = new Document("$match", new Document("idVigia", idVigia)
				.append("diaVencimento", new Document("$lte", dia)).append("mesVencimento", new Document("$lt", mes)));

		var fields = new Document("valor", 1).append("_id", 1).append("idCliente", 1).append("nomeCliente", 1)
				.append("telefoneCliente", 1).append("diaVencimento", 1).append("mesVencimento", 1);
		var project = new Document("$project", fields);

		var sort = new Document("$sort", new Document("diaVencimento", 1));

		var pipeline = Arrays.asList(match, project, sort);
		return Flux.from(collection.aggregate(pipeline)).map(doc -> fromDoc(doc));
	}

	public Flux<Contrato> obterContratosByIdVigia(String idVigia) {
		return Flux.from(collection.find(new Document("idVigia", idVigia))).map(doc -> fromDoc(doc));
	}

	public Mono<Long> atualizarDataFimContrato(String idContrato, Date dataFim) {
		var filter = new Document("_id", idContrato);
		var fields = new Document("dataFim", dataFim).append("situacao",
				TipoSituacaoContrato.ENCERRADO_VIGIA.toString());
		var update = new Document("$set", fields);
		return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
	}

	public Mono<String> obterIdVigiaByIdContrato(String idContrato) {
		var match = new Document("$match", new Document("_id", idContrato));
		var project = new Document("$project", new Document("idVigia", 1));
		var pipeline = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(pipeline)).map(doc -> doc.getString("idVigia"));

	}
}
