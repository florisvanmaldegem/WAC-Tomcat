package nl.hu.v1wac.firstapp.webservices;

import nl.hu.v1wac.firstapp.model.Country;
import nl.hu.v1wac.firstapp.model.ServiceProvider;
import nl.hu.v1wac.firstapp.model.WorldService;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("/countries")
public class WorldResource {
	
	WorldService worldService = ServiceProvider.getWorldService();
	
	@GET
	@Produces("application/json")
	public Response getCountries() {
		List<Country> countries = worldService.getAllCountries();
		if(countries.isEmpty()) {
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "No countries found!");
			return Response.status(404).entity(messages).build();
		}
		
		return Response.ok(countries).build();
	}
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getCountry(@PathParam("id") String id) {
		WorldService worldService = ServiceProvider.getWorldService();
		
		Country c = worldService.getCountryByCode(id);
		
		if(c == null) {
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "No countries found with id: "+id);
			return Response.status(404).entity(messages).build();
		}
		
		return Response.ok(c).build();		
	}
	
	@POST
	@RolesAllowed("user")
	@Produces("application/json")
	public Response postCountry(
			@FormParam("name") String name,
			@FormParam("capital") String capital,
			@FormParam("region") String region,
			@FormParam("iso3") String iso3,
			@FormParam("continent") String continent,
			@FormParam("surface") double surface,
			@FormParam("population") int population,
			@FormParam("countryCode") String countrycode,
			@FormParam("government") String government,
			@FormParam("lat") double latitude,
			@FormParam("lon") double longitude) {
		Country c = new Country(countrycode, iso3,
				name, capital,
				continent, region,
				surface, population,
				government, latitude, longitude);
			
		WorldService worldService = ServiceProvider.getWorldService();
		
		if(worldService.saveCountry(c)) {
			return Response.ok(c).build();
		}else {
			System.out.println("Something went wrong");
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "Couldnt save country:  "+c.getName());
			return Response.status(404).entity(messages).build();
		}
	}
	
	@PUT
	@RolesAllowed("user")
	@Path("{id}")
	@Produces("application/json")
	public Response putCountry(@PathParam("id") String id,
			@FormParam("name") String name,
			@FormParam("capital") String capital,
			@FormParam("region") String region,
			@FormParam("surface") double surface,
			@FormParam("population") int population) {
		WorldService worldService = ServiceProvider.getWorldService();
		
		Country c = worldService.getCountryByCode(id);
		
		if(c == null) {
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "No countries found with id: "+id);
			return Response.status(404).entity(messages).build();
		}
		Country nc = new Country(c.getCode(),
				c.getIso3(),
				name,
				capital,
				c.getContinent(),
				region,
				surface,
				population,
				c.getGovernment(),
				c.getLatitude(),
				c.getLongitude());
		
		if(worldService.updateCountry(nc)) {
			return Response.ok(c).build();
		}else {
			System.out.println("Something went wrong");
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "Couldnt update country:  "+c.getName());
			return Response.status(404).entity(messages).build();
		}
	}
	
	@DELETE
	@Path("{id}")
	@RolesAllowed("user")
	@Produces("application/json")
	public Response deleteCountry(@PathParam("id") String id) {
		WorldService worldService = ServiceProvider.getWorldService();
		
		Country c = worldService.getCountryByCode(id);
		
		if(c == null) {
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "No countries found with id: "+id);
			return Response.status(404).entity(messages).build();
		}
		
		if(worldService.deleteCountry(c)) {
			System.out.println("Deleted: " + c.getCode());
			return Response.ok(c).build();
		}else {
			System.out.println("Something went wrong");
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "Couldnt delete country:  "+c.getName());
			return Response.status(404).entity(messages).build();
		}
			
	}
	
	@GET
	@Path("largestsurfaces")
	@Produces("application/json")
	public Response largestSurfaces() {
		WorldService worldService = ServiceProvider.getWorldService();
		
		List<Country> countries = worldService.get10LargestSurfaces();
		
		if(countries.isEmpty()) {
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "No countries found!");
			return Response.status(404).entity(messages).build();
		}
		
		return Response.ok(countries).build();
	}
	
	@GET
	@Path("largestpopulations")
	@Produces("application/json")
	public Response largestPopulations() {
		WorldService worldService = ServiceProvider.getWorldService();
		
		List<Country> countries = worldService.get10LargestPopulations();
		
		if(countries.isEmpty()) {
			Map<String, String> messages = new HashMap<>();
			messages.put("error", "No countries found!");
			return Response.status(404).entity(messages).build();
		}
		
		return Response.ok(countries).build();
	}
}
