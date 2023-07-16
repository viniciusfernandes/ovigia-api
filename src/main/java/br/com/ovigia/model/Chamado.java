package br.com.ovigia.model;

import br.com.ovigia.model.enumeration.TipoSituacaoChamado;

import java.util.Date;

public class Chamado {
    public String id;
    public Date data;
    public String idCliente;
    public String nomeCliente;
    public IdRonda idRonda;
    public String logradouro;
    public Localizacao localizacao;
    public TipoSituacaoChamado situacao;

    public boolean isAberto() {
        return situacao != null && situacao.isAberto();
    }

    public boolean isVigia(String idVigia) {
        return idRonda != null && idRonda.idVigia.equals(idVigia);
    }
}
