package com.br.dantech.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
	private String login;
	private String password;
	private String name;
}
