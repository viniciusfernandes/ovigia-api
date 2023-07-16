
package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.Faturamento;
import br.com.ovigia.model.IdFaturamento;
import br.com.ovigia.model.repository.FaturamentoRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static br.com.ovigia.repository.parser.FaturamentoParser.toDoc;

public class FaturamentoHashRepository implements FaturamentoRepository {
    private final Map<IdFaturamento, Faturamento> table = new HashMap<>();

    public Mono<Faturamento> criarFaturamento(Faturamento faturamento) {
        table.put(faturamento.id, faturamento);
        return Mono.just(faturamento);
    }

    public Mono<Long> atualizarFaturamento(Faturamento faturamento) {
        var filter = toDoc(faturamento.id);
        var fat = table.get(faturamento.id);
        if (fat == null) {
            return Mono.empty();
        }
        fat.quantidadePagamentos = faturamento.quantidadePagamentos;
        fat.valor = faturamento.valor;
        return Mono.just(1L);
    }

    public Mono<Faturamento> obterValoresFaturamento(IdFaturamento id) {
        var fat = table.get(id);
        if (fat == null) {
            return Mono.empty();
        }
        return Mono.just(fat);
    }

}
