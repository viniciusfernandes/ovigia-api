package br.com.ovigia.model;

import java.util.Date;

public class SolicitacaoVisita {
    public String idCliente;
    public String idVigia;
    public String nomeCliente;
    public String telefoneCliente;
    public Localizacao localizacaoCliente;
    public Date data;

    public Contrato toContrato() {
        var contrato = new Contrato();
        contrato.idCliente = idCliente;
        contrato.idVigia = idVigia;
        contrato.nomeCliente = nomeCliente;
        contrato.telefoneCliente = telefoneCliente;
        contrato.valor = 0d;
        return contrato;
    }
}
