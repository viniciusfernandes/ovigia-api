package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import br.com.ovigia.model.repository.ChamadoRepository;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static br.com.ovigia.repository.parser.ChamadoParser.fromDoc;
import static br.com.ovigia.repository.parser.ChamadoParser.toDoc;

public class ChamadoMongoRepository implements ChamadoRepository {
	private final MongoCollection<Document> collection;

	public ChamadoMongoRepository(MongoDatabase database) {
		collection = database.getCollection("chamado");
	}

	public Mono<String> criarChamado(Chamado chamado) {
		chamado.id = UUID.randomUUID().toString();
		var doc = toDoc(chamado);
		return Mono.from(collection.insertOne(doc)).thenReturn(chamado.id);
	}

	public Mono<Void> atualizarSituacao(String idChamado, TipoSituacaoChamado situacaoChamado) {
		var update = new Document("$set", new Document("situacao", situacaoChamado.toString()));
		return Mono.from(collection.updateOne(new Document("_id", idChamado), update)).then();
	}

	public Mono<List<Chamado>> obterChamadosAbertosByIdVigia(String idVigia) {
		var inChamadosAbertos = new Document("$in", TipoSituacaoChamado.CHAMADOS_ABERTOS);
		var filter = new Document("idRonda.idVigia", idVigia).append("situacao",  inChamadosAbertos);
		return Flux.from(collection.find(filter)).collectList().map(docs -> fromDoc(docs));
	}

	public Mono<Chamado> obterChamadosAtivoByIdCliente(String idCliente) {
		var match = new Document("$match",
				new Document("idCliente", idCliente).append("situacao", TipoSituacaoChamado.ATIVO.toString()));

		var project = new Document("$project", new Document("_id", 1).append("data", 1));
		return Mono.from(collection.aggregate(Arrays.asList(match, project))).map(docs -> fromDoc(docs));
	}

	public Mono<Long> obterTotalChamadoEncerradosByIdRonda(IdRonda id) {
		var filter = new Document("idRonda.idVigia", id.idVigia).append("idRonda.data", id.dataRonda).append("situacao",
				TipoSituacaoChamado.ENCERRADO.toString());
		return Mono.from(collection.countDocuments(filter)).switchIfEmpty(Mono.just(0L));
	}

}
