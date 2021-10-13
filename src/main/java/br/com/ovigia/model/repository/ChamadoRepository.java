package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.ChamadoParser.fromDoc;
import static br.com.ovigia.repository.parser.ChamadoParser.toDoc;

import java.util.List;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Chamado;
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

	public Mono<List<Chamado>> obterChamadosAbertosByVigia(String idVigia) {
		//var filter = new Document("idVigia", idVigia).append("situacao", TipoSituacaoChamado.ABERTO.toString());
		var filter = new Document("idVigia", idVigia) ;
		return Flux.from(collection.find(filter)).collectList().map(docs -> fromDoc(docs));
	}

}
