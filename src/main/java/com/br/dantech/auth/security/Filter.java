package com.br.dantech.auth.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.br.dantech.auth.record.ErrorRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Filter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getHeader("Authorization") != null) {
			Authentication auth = TokenUtil.decodeToken(request);
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				ErrorRecord errorRecord = new ErrorRecord(401, "Usuario nao autorizado para este sistema");
				response.setStatus(errorRecord.status());
				response.setContentType("application/json");
				ObjectMapper mapper = new ObjectMapper();
				response.getWriter().print(mapper.writeValueAsString(errorRecord));
				response.getWriter().flush();
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
