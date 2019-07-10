package nl.hu.v1wac.firstapp.authentication;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import nl.hu.v1wac.firstapp.webservices.AuthenticationResource;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		boolean isSecure = requestContext.getSecurityContext().isSecure();
		MySecurityContext msc = new MySecurityContext("Unknown", "guest", isSecure);
		
		String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		
		if(authHeader != null && authHeader.startsWith("Bearer")) {
			String token = authHeader.substring("Bearer".length()).trim();
			
			try {
				JwtParser parser = Jwts.parser().setSigningKey(AuthenticationResource.key);
				Claims claims = parser.parseClaimsJws(token).getBody();
				
				String user = claims.getSubject();
				String role = claims.get("role").toString();
		
				msc = new MySecurityContext(user, role, isSecure);
			} catch(JwtException | IllegalArgumentException e) {
				System.out.println("Invalid JWT, Processing as guest!");
			}
			
			requestContext.setSecurityContext(msc);
		}
		
	}
}
