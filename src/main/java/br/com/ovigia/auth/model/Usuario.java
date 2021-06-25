package br.com.ovigia.auth.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.ovigia.auth.security.model.Role;

public class Usuario {

	private String email;

	private String password;

	private List<Role> roles;

	public Usuario(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Usuario(String email, String password, List<Role> roles) {
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String username) {
		this.email = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}