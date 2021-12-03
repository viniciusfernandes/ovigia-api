package br.com.ovigia.auth.route;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ovigia.auth.businessrule.SingInResponse;
import br.com.ovigia.auth.businessrule.singnin.SingInRequest;
import br.com.ovigia.auth.businessrule.singnin.SingInRule;
import br.com.ovigia.auth.businessrule.singon.SignOnRequest;
import br.com.ovigia.auth.businessrule.singon.SingOnRule;
import br.com.ovigia.auth.repository.UsuarioRepository;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.route.Route;
import br.com.ovigia.route.Router;

public class AuthRouter extends Router {

	public AuthRouter(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {

		var siginRoute = Route.<SingInRequest, SingInResponse>post().path("/ovigia/auth/signin").contemBody()
				.requestClass(SingInRequest.class).rule(new SingInRule(usuarioRepository, passwordEncoder, jwtUtil));

		var sigonRoute = Route.<SignOnRequest, SingInResponse>post().path("/ovigia/auth/signon").contemBody()
				.requestClass(SignOnRequest.class).rule(new SingOnRule(usuarioRepository, passwordEncoder, jwtUtil));

		addRoute(siginRoute);
		addRoute(sigonRoute);
	}

}
