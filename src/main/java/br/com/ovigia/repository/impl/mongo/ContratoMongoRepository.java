package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.enumeration.TipoSituacaoContrato;
import br.com.ovigia.model.repository.ContratoRepository;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static br.com.ovigia.repository.parser.ContratoParser.fromDoc;
import static br.com.ovigia.repository.parser.ContratoParser.toDoc;

public class ContratoMongoRepository implements ContratoRepository {
	private final MongoCollection<Document> collection;

	public ContratoMongoRepository(MongoDatabase database) {
		collection = database.getCollection("contrato");
	}

	public Mono<String> criarContrato(Contrato contrato) {
		contrato.id = UUID.randomUUID().toString();
		var doc = toDoc(contrato);
		return Mono.from(collection.insertOne(doc)).thenReturn(contrato.id);
	}

	public Flux<Contrato> obterIdContratosVencidos() {
		var filter = new Document("dataVencimento", new Document("$lte", new Date())).append("situacao",
				TipoSituacaoContrato.ATIVO.toString());
		var match = new Document("$match", filter);
		var fields = new Document("_id", 1).append("dataVencimento", 1).append("nomeCliente", 1)
				.append("telefoneCliente", 1).append("idVigia", 1).append("idCliente", 1).append("valor", 1);
		var project = new Document("$project", fields);

		var pipeline = Arrays.asList(match, project);
		return Flux.from(collection.aggregate(pipeline)).map(doc -> fromDoc(doc));
	}

	public Flux<Contrato> obterContratosAtivosByIdVigia(String idVigia) {
		var filter = new Document("idVigia", idVigia).append("situacao", TipoSituacaoContrato.ATIVO.toString());
		return Flux.from(collection.find(filter)).map(doc -> fromDoc(doc));
	}

	public Mono<Contrato> obterContratoAtivoByIdCliente(String idCliente) {
		var match = new Document("$match",
				new Document("idCliente", idCliente).append("situacao", TipoSituacaoContrato.ATIVO.toString()));
		var fields = new Document("idVigia", 1).append("valor", 1).append("dataVencimento", 1).append("dataInicio", 1)
				.append("_id", 1);
		var project = new Document("$project", fields);
		var pipeline = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(pipeline)).map(doc -> fromDoc(doc));
	}

	public Mono<Long> atualizarDataFimContrato(String idContrato, Date dataFim) {
		var filter = new Document("_id", idContrato);
		var fields = new Document("dataFim", dataFim).append("situacao", TipoSituacaoContrato.CANCELADO.toString());
		var update = new Document("$set", fields);
		return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
	}

	public Mono<Long> atualizarValorContrato(String idContrato, Double valor) {
		var filter = new Document("_id", idContrato);
		var fields = new Document("valor", valor);
		var update = new Document("$set", fields);
		return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
	}

	public Mono<Long> atualizarDataVencimentoContrato(String idContrato, Date dataVencimento) {
		var filter = new Document("_id", idContrato);
		var update = new Document("$set", new Document("dataVencimento", dataVencimento));
		return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
	}

}
