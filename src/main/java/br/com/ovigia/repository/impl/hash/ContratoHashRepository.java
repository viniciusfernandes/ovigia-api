package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.enumeration.TipoSituacaoContrato;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContratoHashRepository implements ContratoRepository {
    private final Map<String, Contrato> table = new HashMap<>();

    public Mono<String> criarContrato(Contrato contrato) {
        contrato.id = UUID.randomUUID().toString();
        table.put(contrato.id, contrato);
        return Mono.just(contrato.id);
    }

    public Flux<Contrato> obterIdContratosVencidos() {
        return Flux.fromIterable(table.values())
                .filter(cont -> cont.isContratoVencido() && cont.isAtivo());
    }

    public Flux<Contrato> obterContratosAtivosByIdVigia(String idVigia) {
        return Flux.fromIterable(table.values())
                .filter(cont -> cont.idVigia.equals(idVigia) && cont.isAtivo());
    }

    public Mono<Contrato> obterContratoAtivoByIdCliente(String idCliente) {
        for (var cont : table.values()) {
            if (cont.idCliente.equals(idCliente) && cont.isAtivo()) {
                return Mono.just(cont);
            }
        }
        return Mono.empty();
    }

    public Mono<Long> atualizarDataFimContrato(String idContrato, Date dataFim) {
        var cont = table.get(idContrato);
        if (cont == null) {
            return Mono.just(0l);
        }
        cont.dataFim = dataFim;
        cont.situacao = TipoSituacaoContrato.CANCELADO;
        return Mono.just(1l);
    }

    public Mono<Long> atualizarValorContrato(String idContrato, Double valor) {
        var cont = table.get(idContrato);
        if (cont == null) {
            return Mono.just(0l);
        }
        cont.valor = valor;
        return Mono.just(1l);
    }

    public Mono<Long> atualizarDataVencimentoContrato(String idContrato, Date dataVencimento) {
        var cont = table.get(idContrato);
        if (cont == null) {
            return Mono.just(0l);
        }
        cont.dataVencimento = dataVencimento;
        return Mono.just(1l);
    }

}
