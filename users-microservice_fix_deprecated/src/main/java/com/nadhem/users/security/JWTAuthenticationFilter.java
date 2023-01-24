package com.nadhem.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadhem.users.entities.User;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{


	private AuthenticationManager authenticationManager;

	
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
	
		User user =null;
		try {
			 user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
			
		return authenticationManager.
				authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		org.springframework.security.core.userdetails.User springUser = 
			(org.springframework.security.core.userdetails.User) authResult.getPrincipal();
		
		List<String> roles = new ArrayList<>();
		springUser.getAuthorities().forEach(au-> {
			roles.add(au.getAuthority());
		});
		
		String jwt = JWT.create().
				  withSubject(springUser.getUsername()).
		withArrayClaim("roles", roles.toArray(new String[roles.size()])).
		withExpiresAt(new Date(System.currentTimeMillis()+SecParams.EXP_TIME)). 
		sign(Algorithm.HMAC256(SecParams.SECRET));
		
		response.addHeader("Authorization", jwt);			  
		
	}
	
}
