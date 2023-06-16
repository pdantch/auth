package com.br.dantech.auth.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.dantech.auth.model.User;
import com.br.dantech.auth.security.AuthToken;
import com.br.dantech.auth.security.TokenUtil;

@RestController
public class AuthController {

	@GetMapping("/free")
	public String sayHallo() {
		return "Este é um endpoint que liberado pela API";
	}

	@GetMapping("/authenticated")
	public String syAuthHello() {
		return "Este é um endpoint que precisa de autenticação";
	}

	@PostMapping("/authentication")
	public ResponseEntity<AuthToken> login(@RequestBody User user) {
		return "dantch".equals(user.getLogin()) && "12345".equals(user.getPassword())
				? ResponseEntity.ok(TokenUtil.encodeToken(user))
				: ResponseEntity.status(403).build();
	}
}
