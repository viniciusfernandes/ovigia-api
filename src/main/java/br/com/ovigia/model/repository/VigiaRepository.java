package br.com.ovigia.model.repository;

import br.com.ovigia.model.Avaliacao;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Vigia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface VigiaRepository {

    Mono<Vigia> obterNomeETelefoneEAvaliacao(String idVigia);

    Mono<Vigia> obterDataUltimaRonda(String idVigia);

    Mono<Vigia> obterVigiaPorId(String idVigia);

    Flux<Vigia> obterLocalizacaoVigias();

    Mono<Long> atualizarDataUltimaRonda(String idVigia, Date dataUltimaRonda, Date dataAtualizacaoRonda);

    Mono<Long> atualizarAvaliacao(String idVigia, Avaliacao avaliacao);

    Mono<Avaliacao> obterAvaliacao(String idVigia);

    Mono<Void> atualizarCliente(String idVigia, Cliente cliente);

    Mono<Void> atualizarLocalizacaoPorId(String idVigia, Localizacao localizacao);
}
