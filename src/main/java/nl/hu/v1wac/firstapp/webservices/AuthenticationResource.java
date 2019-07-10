package nl.hu.v1wac.firstapp.webservices;

import java.security.Key;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.v1wac.firstapp.persistence.UserDao;
import nl.hu.v1wac.firstapp.persistence.UserPostgresDaoImpl;

@Path("/authentication")
public class AuthenticationResource {
	final static public Key key = MacProvider.generateKey();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(
			@FormParam("name") String name,
			@FormParam("pass") String pass) {
		try {
			UserDao userDao = new UserPostgresDaoImpl();
			String role = userDao.findRoleForUser(name, pass);
			
			if(role == null) {throw new IllegalArgumentException("No user found");}
			
			String token = createToken(name, role);
			SimpleEntry<String, String> jwt = new SimpleEntry<String, String>("JWT", token);
			
			return Response.ok(jwt).build();
		} catch (JwtException | IllegalArgumentException e) {
			e.printStackTrace();
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	private String createToken(String user, String role) {
		Calendar exp = Calendar.getInstance();
		exp.add(Calendar.MINUTE, 30);
		
		String JWT = Jwts.builder()
				.setSubject(user)
				.setExpiration(exp.getTime())
				.claim("role", role)
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
		
		return JWT;
	}
}
