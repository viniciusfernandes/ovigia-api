package br.com.ovigia.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ovigia.model.enumeration.TipoUsuario;

public class Vigia extends Usuario {

	public Double avaliacao;
	public Date dataUltimaRonda;
	public Date dataAtualizacaoRonda;

	@JsonIgnore
	private List<Cliente> clientes;

	public Vigia() {
		tipoUsuario = TipoUsuario.VIGIA;
	}

	public Vigia(String email, String nome) {
		this.email = email;
		this.nome = nome;
		tipoUsuario = TipoUsuario.VIGIA;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

}
