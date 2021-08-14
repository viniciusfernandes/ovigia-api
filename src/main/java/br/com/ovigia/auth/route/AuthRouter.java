package br.com.ovigia.auth.route;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ovigia.auth.api.AuthRequest;
import br.com.ovigia.auth.api.AuthResponse;
import br.com.ovigia.auth.api.SignOnRequest;
import br.com.ovigia.auth.businessrule.SingInRule;
import br.com.ovigia.auth.businessrule.SingOnRule;
import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.route.Route;
import br.com.ovigia.route.Router;

public class AuthRouter extends Router {

	public AuthRouter(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {

		var siginRoute = Route.<AuthRequest, AuthResponse>post();
		siginRoute.url("/ovigia/auth/signin").contemBody().requestClass(AuthRequest.class)
				.rule(new SingInRule(usuarioRepository, passwordEncoder, jwtUtil));

		var sigonRoute = Route.<SignOnRequest, AuthResponse>post();
		sigonRoute.url("/ovigia/auth/signon").contemBody().requestClass(SignOnRequest.class)
				.rule(new SingOnRule(usuarioRepository, passwordEncoder, jwtUtil));

		addRoute(siginRoute);
		addRoute(sigonRoute);
	}

}
