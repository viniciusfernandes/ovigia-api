package br.com.ovigia.auth.route;

import br.com.ovigia.auth.businessrule.AuthResponse;
import br.com.ovigia.auth.businessrule.signin.SignInRequest;
import br.com.ovigia.auth.businessrule.signin.SignInRule;
import br.com.ovigia.auth.businessrule.singon.SignOnRequest;
import br.com.ovigia.auth.businessrule.singon.SignOnRule;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.model.repository.UsuarioRepository;
import br.com.ovigia.route.Route;
import br.com.ovigia.route.Router;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthRouter extends Router {

	public AuthRouter(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {

		var siginRoute = Route.<SignInRequest, AuthResponse>post().path("/ovigia/auth/signin").contemBody()
				.requestClass(SignInRequest.class).rule(new SignInRule(usuarioRepository, passwordEncoder, jwtUtil));

		var sigonRoute = Route.<SignOnRequest, AuthResponse>post().path("/ovigia/auth/signon").contemBody()
				.requestClass(SignOnRequest.class).rule(new SignOnRule(usuarioRepository, passwordEncoder, jwtUtil));

		addRoute(siginRoute);
		addRoute(sigonRoute);
	}

}
