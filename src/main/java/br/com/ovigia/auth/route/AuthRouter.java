package br.com.ovigia.auth.route;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ovigia.auth.api.AuthRequest;
import br.com.ovigia.auth.businessrule.SigInRule;
import br.com.ovigia.auth.businessrule.SigOnRule;
import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.route.Route;
import br.com.ovigia.route.Router;

public class AuthRouter extends Router {

	public AuthRouter(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {

		var siginRoute = Route.<AuthRequest, String>post();
		siginRoute.url("/ovigia/auth/signin").contemBody().requestClass(AuthRequest.class)
				.rule(new SigInRule(usuarioRepository, passwordEncoder, jwtUtil));

		var sigonRoute = Route.<AuthRequest, String>post();
		sigonRoute.url("/ovigia/auth/signon").contemBody().requestClass(AuthRequest.class)
				.rule(new SigOnRule(usuarioRepository, passwordEncoder, jwtUtil));

		add(siginRoute);
		add(sigonRoute);
	}

}
