package br.com.ovigia.auth.api;

/**
 *
 * @author ard333
 */
public class AuthRequest {
	private String email;
	private String password;

	public AuthRequest() {

	}

	public AuthRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "AuthRequest [email=" + email + ", password=" + password + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String username) {
		this.email = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
