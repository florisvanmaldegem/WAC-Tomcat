package nl.hu.v1wac.firstapp.webservices;

import nl.hu.v1wac.firstapp.model.Country;
import nl.hu.v1wac.firstapp.model.ServiceProvider;
import nl.hu.v1wac.firstapp.model.WorldService;

import javax.json.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import java.util.List;

@Path("/countries")
public class WorldResource {
	
	@GET
	@Produces("application/json")
	public String getCountries() {
		WorldService worldService = ServiceProvider.getWorldService();
		
		List<Country> countries = worldService.getAllCountries();
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(Country c : countries) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("code", c.getCode());
			job.add("iso3", c.getIso3());
			job.add("name", c.getName());
			job.add("continent", c.getContinent());
			job.add("capital", c.getCapital());
			job.add("region", c.getRegion());
			job.add("surface", c.getSurface());
			job.add("population", c.getPopulation());
			job.add("government", c.getGovernment());
			job.add("lat", c.getLatitude());
			job.add("lon", c.getLongitude());
			jab.add(job);
		}
		return jab.build().toString();
	}
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public String getCountry(@PathParam("id") String id) {
		WorldService worldService = ServiceProvider.getWorldService();
		
		Country c = worldService.getCountryByCode(id);
		JsonObjectBuilder job = Json.createObjectBuilder();
		if(c != null) {
			job.add("code", c.getCode());
			job.add("iso3", c.getIso3());
			job.add("name", c.getName());
			job.add("continent", c.getContinent());
			job.add("capital", c.getCapital());
			job.add("region", c.getRegion());
			job.add("surface", c.getSurface());
			job.add("population", c.getPopulation());
			job.add("government", c.getGovernment());
			job.add("lat", c.getLatitude());
			job.add("lon", c.getLongitude());
		}else {
			job.add("error", "country not found");
		}
		
		return job.build().toString();
		
	}
	
	@GET
	@Path("largestsurfaces")
	@Produces("application/json")
	public String largestSurfaces() {
		WorldService worldService = ServiceProvider.getWorldService();
		
		List<Country> countries = worldService.get10LargestSurfaces();
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(Country c : countries) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("code", c.getCode());
			job.add("iso3", c.getIso3());
			job.add("name", c.getName());
			job.add("continent", c.getContinent());
			job.add("capital", c.getCapital());
			job.add("region", c.getRegion());
			job.add("surface", c.getSurface());
			job.add("population", c.getPopulation());
			job.add("government", c.getGovernment());
			job.add("lat", c.getLatitude());
			job.add("lon", c.getLongitude());
			jab.add(job);
		}
		return jab.build().toString();
	}
	
	@GET
	@Path("largestpopulations")
	@Produces("application/json")
	public String largestPopulations() {
		WorldService worldService = ServiceProvider.getWorldService();
		
		List<Country> countries = worldService.get10LargestPopulations();
		
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(Country c : countries) {
			JsonObjectBuilder job = Json.createObjectBuilder();
			job.add("code", c.getCode());
			job.add("iso3", c.getIso3());
			job.add("name", c.getName());
			job.add("continent", c.getContinent());
			job.add("capital", c.getCapital());
			job.add("region", c.getRegion());
			job.add("surface", c.getSurface());
			job.add("population", c.getPopulation());
			job.add("government", c.getGovernment());
			job.add("lat", c.getLatitude());
			job.add("lon", c.getLongitude());
			jab.add(job);
		}
		return jab.build().toString();
	}
}
