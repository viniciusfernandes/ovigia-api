package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.Avaliacao;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.enumeration.TipoUsuario;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

public class VigiaHashRepository implements VigiaRepository {
    private final Map<String, Vigia> table = new HashMap<>();

    public Mono<Vigia> obterNomeETelefoneEAvaliacao(String idVigia) {
        return obterVigiaPorId(idVigia);
    }

    public Mono<Vigia> obterDataUltimaRonda(String idVigia) {
        return obterVigiaPorId(idVigia).map(vigia -> {
            if (vigia.dataUltimaRonda != null && vigia.dataAtualizacaoRonda != null) {
                return vigia;
            }
            return new Vigia();
        });
    }

    public Mono<Vigia> obterVigiaPorId(String idVigia) {
        if (idVigia == null) {
            return Mono.empty();
        }
        for (var e : table.values()) {
            if (idVigia.equals(e.id)) {
                return Mono.just(e);
            }
        }
        return Mono.empty();
    }

    public Flux<Vigia> obterLocalizacaoVigias() {
        var vigias = new ArrayList<Vigia>();
        for (var e : table.values()) {
            if (TipoUsuario.VIGIA == e.tipoUsuario) {
                vigias.add(e);
            }

        }
        return Flux.fromIterable(vigias);
    }

    public Flux<Vigia> obterVigias(List<String> idVigias) {
        return Flux.fromIterable(idVigias).flatMap(id -> obterVigiaPorId(id));
    }

    public Mono<Long> atualizarDataUltimaRonda(String idVigia, Date dataUltimaRonda, Date dataAtualizacaoRonda) {
        return obterVigiaPorId(idVigia).map(vigia -> {
            vigia.dataUltimaRonda = dataUltimaRonda;
            vigia.dataAtualizacaoRonda = dataAtualizacaoRonda;
            return 1L;
        }).defaultIfEmpty(0L);
    }

    public Mono<Long> atualizarAvaliacao(String idVigia, Avaliacao avaliacao) {
        return obterVigiaPorId(idVigia).map(vigia -> {
            vigia.avaliacao = avaliacao;
            return 1L;
        }).defaultIfEmpty(0L);
    }

    public Mono<Avaliacao> obterAvaliacao(String idVigia) {
        return obterVigiaPorId(idVigia).map(vigia -> vigia.avaliacao
        ).defaultIfEmpty(new Avaliacao());
    }

    public Mono<Void> atualizarCliente(String idVigia, Cliente cliente) {
        return obterVigiaPorId(idVigia).map(vigia -> {
            vigia.addCliente(cliente);
            return vigia;
        }).then();
    }

    public Mono<Void> atualizarLocalizacaoPorId(String idVigia, Localizacao localizacao) {
        return obterVigiaPorId(idVigia).map(vigia -> {
            vigia.localizacao = localizacao;
            return vigia;
        }).then();
    }
}
