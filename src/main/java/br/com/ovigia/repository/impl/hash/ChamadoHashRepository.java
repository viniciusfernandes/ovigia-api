package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import br.com.ovigia.model.repository.ChamadoRepository;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

public class ChamadoHashRepository implements ChamadoRepository {
    private final Map<String, Chamado> table = new HashMap<>();

    public Mono<String> criarChamado(Chamado chamado) {
        chamado.id = UUID.randomUUID().toString();
        table.put(chamado.id, chamado);
        return Mono.just(chamado.id);
    }

    public Mono<Void> atualizarSituacao(String idChamado, TipoSituacaoChamado situacaoChamado) {
        if (idChamado == null) {
            return Mono.empty();
        }
        for (var e : table.values()) {
            if (idChamado.equals(e.id)) {
                e.situacao = situacaoChamado;
            }
        }
        return Mono.empty();
    }

    public Mono<List<Chamado>> obterChamadosAbertosByIdVigia(String idVigia) {
        if (idVigia == null) {
            return Mono.empty();
        }
        return Flux.fromIterable(table.values())
                .filter(chamado -> chamado.isAberto() && chamado.isVigia(idVigia))
                .collectList();
    }

    public Mono<Chamado> obterChamadosAtivoByIdCliente(String idCliente) {
        if (idCliente == null) {
            return Mono.empty();
        }
        for (var e : table.values()) {
            if (idCliente.equals(e.idCliente) && e.situacao.isAtivo()) {
                return Mono.just(e);
            }
        }
        return Mono.empty();
    }

    public Mono<Long> obterTotalChamadoEncerradosByIdRonda(IdRonda id) {
        if (id == null) {
            return Mono.empty();
        }
        for (var e : table.values()) {
            if (e.idRonda.idVigia.equals(id.idVigia) && e.situacao == TipoSituacaoChamado.ENCERRADO) {
                return Mono.just(1L);
            }
        }
        return Mono.just(0L);
    }

}
