package br.com.ovigia.model;

import br.com.ovigia.model.enumeration.TipoSituacaoContrato;

import java.util.Date;

public class Contrato {
    public String id;
    public Date dataInicio;
    public Date dataFim;
    public Date dataVencimento;
    public Double valor;
    public String idVigia;
    public String idCliente;
    public String nomeCliente;
    public String telefoneCliente;
    public TipoSituacaoContrato situacao;

    public boolean isContratoVencido() {
        return dataVencimento != null && dataVencimento.before(new Date());
    }

    public boolean isAtivo() {
        return situacao != null && situacao == TipoSituacaoContrato.ATIVO;
    }

}
