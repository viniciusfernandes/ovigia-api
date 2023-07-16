package br.com.ovigia.model.repository;

import br.com.ovigia.model.Chamado;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.enumeration.TipoSituacaoChamado;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChamadoRepository {

    Mono<String> criarChamado(Chamado chamado);

    Mono<Void> atualizarSituacao(String idChamado, TipoSituacaoChamado situacaoChamado);

    Mono<List<Chamado>> obterChamadosAbertosByIdVigia(String idVigia);

    Mono<Chamado> obterChamadosAtivoByIdCliente(String idCliente);

    Mono<Long> obterTotalChamadoEncerradosByIdRonda(IdRonda id);

}
