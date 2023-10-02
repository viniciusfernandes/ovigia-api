package br.com.ovigia.auth.businessrule.signin;

/**
 *
 * @author ard333
 */
public class SignInRequest {
	private String email;
	private String password;

	public SignInRequest() {

	}

	public SignInRequest(String email, String password) {
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
