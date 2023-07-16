package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.SolicitacaoVisita;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static br.com.ovigia.repository.parser.SolicitacaoVistiaParser.toDoc;

public class SolicitacaoVisitaHashRepository implements SolicitacaoVisitaRepository {
    private final List<SolicitacaoVisita> table = new ArrayList<>();


    public Mono<SolicitacaoVisita> criarSolicitacao(SolicitacaoVisita solicitacao) {
        var doc = toDoc(solicitacao);
        table.add(solicitacao);
        return Mono.just(solicitacao);
    }

    public Flux<SolicitacaoVisita> obterSolicitacaoByIdVigia(String idVigia) {
        return Flux.fromIterable(table).filter(sol -> sol.idVigia.equals(idVigia));
    }

    public Mono<String> obterIdVigiaSolicitado(String idCliente) {
        for (var sol : table) {
            if (sol.idCliente.equals(idCliente)) {
                return Mono.just(sol.idVigia);
            }
        }
        return Mono.empty();
    }

    public Mono<Long> removerSolicitacao(String idCliente) {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).idCliente.equals(idCliente)) {

            }
        }
        int count = -1;
        for (var sol : table) {
            count++;
            if (sol.idCliente.equals(idCliente)) {
                break;
            }
        }
        if (count >= 0) {
            table.remove(count);
            return Mono.just(1L);
        }
        return Mono.just(0L);
    }

}
