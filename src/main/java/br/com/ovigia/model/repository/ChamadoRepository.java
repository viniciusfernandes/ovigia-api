package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.ChamadoParser.fromDoc;
import static br.com.ovigia.repository.parser.ChamadoParser.toDoc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ChamadoRepository {
	private final MongoCollection<Document> collection;

	public ChamadoRepository(MongoDatabase database) {
		collection = database.getCollection("chamado");
	}

	public Mono<String> criar(Chamado chamado) {
		chamado.id = UUID.randomUUID().toString();
		var doc = toDoc(chamado);
		return Mono.from(collection.insertOne(doc)).thenReturn(chamado.id);
	}

	public Mono<Void> atualizarSituacao(String idChamado, TipoSituacaoChamado situacaoChamado) {
		var update = new Document("$set", new Document("situacao", situacaoChamado.toString()));
		return Mono.from(collection.updateOne(new Document("_id", idChamado), update)).then();
	}

	public Mono<List<Chamado>> obterChamadosAtivoyVigia(String idVigia) {
		var filter = new Document("idVigia", idVigia).append("situacao", TipoSituacaoChamado.ATIVO.toString());
		return Flux.from(collection.find(filter)).collectList().map(docs -> fromDoc(docs));
	}

	public Mono<Chamado> obterChamadosAtivoByIdCliente(String idCliente) {
		var match = new Document("$match",
				new Document("idCliente", idCliente).append("situacao", TipoSituacaoChamado.ATIVO.toString()));

		var project = new Document("$project", new Document("_id", 1).append("data", 1));
		return Mono.from(collection.aggregate(Arrays.asList(match, project))).map(docs -> fromDoc(docs));
	}

}
