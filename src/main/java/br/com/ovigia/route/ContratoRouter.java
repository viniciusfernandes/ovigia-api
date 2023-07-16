package br.com.ovigia.route;

import br.com.ovigia.businessrule.contrato.atualizar.AtualizarValorContratoResquest;
import br.com.ovigia.businessrule.contrato.atualizar.AtualizarValorContratoRule;
import br.com.ovigia.businessrule.contrato.cancelar.CancelarContratoRequest;
import br.com.ovigia.businessrule.contrato.cancelar.CancelarContratoRule;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoRequest;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoResponse;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoRule;
import br.com.ovigia.businessrule.contrato.obter.*;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import br.com.ovigia.model.repository.VigiaRepository;

import java.util.List;

public class ContratoRouter extends Router {

    public ContratoRouter(ContratoRepository contratoRepository,
                          SolicitacaoVisitaRepository solicitacaoVisitaRepository, VigiaRepository vigiaRepository,
                          ClienteRepository clienteRepository) {
        var criarContrato = Route.<CriarContratoRequest, CriarContratoResponse>post().path("/ovigia/contratos")
                .contemBody().requestClass(CriarContratoRequest.class)
                .rule(new CriarContratoRule(contratoRepository, solicitacaoVisitaRepository, clienteRepository));

        var atualizarValorContrato = Route.<AtualizarValorContratoResquest, Void>put().path("/ovigia/contratos/{idContrato}/valor-contrato")
                .contemBody().requestClass(AtualizarValorContratoResquest.class)
                .extractFromPath((mapa, request) -> {
                    request.idContrato = mapa.get("idContrato");
                    return request;
                })
                .rule(new AtualizarValorContratoRule(contratoRepository));

        var obterContratoAtivoCliente = Route.<ObterContratoAtivoClienteRequest, ObterContratoAtivoClienteResponse>get()
                .path("/ovigia/clientes/{idCliente}/contrato-ativo").extractFromPath((mapa, request) -> {
                    request.idCliente = mapa.get("idCliente");
                    return request;
                }).requestClass(ObterContratoAtivoClienteRequest.class)
                .rule(new ObterContratoAtivoClienteRule(contratoRepository, vigiaRepository));

        var obterContratosAtivosVigia = Route
                .<ObterContratosAtivosVigiaRequest, List<ObterContratosAtivosVigiaResponse>>get()
                .path("/ovigia/vigias/{idVigia}/contratos-ativos").extractFromPath((mapa, request) -> {
                    request.idVigia = mapa.get("idVigia");
                    return request;
                }).requestClass(ObterContratosAtivosVigiaRequest.class)
                .rule(new ObterContratosAtivosVigiaRule(contratoRepository, solicitacaoVisitaRepository));

        var cancelarContrato = Route.<CancelarContratoRequest, Void>delete().path("/ovigia/contratos/{idContrato}")
                .requestClass(CancelarContratoRequest.class).extractFromPath((mapa, request) -> {
                    request.idContrato = mapa.get("idContrato");
                    return request;
                }).rule(new CancelarContratoRule(contratoRepository));

        addRoute(criarContrato);
        addRoute(obterContratoAtivoCliente);
        addRoute(obterContratosAtivosVigia);
        addRoute(cancelarContrato);
        addRoute(atualizarValorContrato);
    }

}