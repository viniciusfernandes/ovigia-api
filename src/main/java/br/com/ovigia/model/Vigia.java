package br.com.ovigia.model;

import br.com.ovigia.model.enumeration.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

public class Vigia extends Usuario {

    public Avaliacao avaliacao;
    public Date dataUltimaRonda;
    public Date dataAtualizacaoRonda;

    @JsonIgnore
    public List<Cliente> clientes;

    public Vigia() {
        tipoUsuario = TipoUsuario.VIGIA;
    }

    public Vigia(String email, String nome) {
        this.email = email;
        this.nome = nome;
        tipoUsuario = TipoUsuario.VIGIA;
    }

    public void addCliente(Cliente cliente) {
        if (clientes != null) {
            clientes.add(cliente);
        }
    }

}
