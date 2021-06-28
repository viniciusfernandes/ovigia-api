package br.com.ovigia.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ovigia.auth.model.Usuario;
import br.com.ovigia.auth.security.JwtUtil;
import br.com.ovigia.auth.service.UserService;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationApi {
	private JwtUtil jwtUtil;
	private UserService userService;

	public AuthenticationApi(JwtUtil jwtUtil, UserService userService) {
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@PostMapping("/ovigia/auth/signin")
	public Mono<ResponseEntity<AuthResponse>> signIn(@RequestBody AuthRequest request) {
		return userService.isUsuarioExistente(request.getEmail(), request.getPassword()).map(usurioExiste -> {
			if (usurioExiste) {
				return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(request.getEmail())));
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		});
	}

	@PostMapping("/ovigia/auth/signon")
	public Mono<ResponseEntity<AuthResponse>> signOn(@RequestBody AuthRequest request) {
		var usuario = new Usuario(request.getEmail(), request.getPassword());
		return userService.criarUsuario(usuario)
				.thenReturn(ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(request.getEmail()))));
	}
}
