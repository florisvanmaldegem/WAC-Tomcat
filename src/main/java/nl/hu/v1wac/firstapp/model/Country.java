package nl.hu.v1wac.firstapp.model;

public class Country {
	private String code;
	private String iso3;
	private String name;
	private String capital;
	private String continent;
	private String region;
	private double surface;
	private int population;
	private String government;
	private double latitude;
	private double longitude;
	
	public Country(String code, String iso3, String nm, String cap, String ct, String reg, double sur, int pop, String gov, double lat, double lng) {
		this.code = code; 
		this.iso3 = iso3;
		this.name = nm;
		this.capital = cap;
		this.continent = ct;
		this.region = reg;
		this.surface = sur;
		this.population = pop;
		this.government = gov;
		this.latitude = lat;
		this.longitude = lng;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getIso3() {
		return iso3;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCapital() {
		return capital;
	}
	
	public String getContinent() {
		return continent;
	}
	
	public String getRegion() {
		return region;
	}
	
	public double getSurface() {
		return surface;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public String getGovernment() {
		return government;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Country code: ").append(this.code).append("\n");
		sb.append("Iso3: ").append(this.iso3).append("\n");
		sb.append("Name: ").append(this.name).append("\n");
		sb.append("Capital: ").append(this.capital).append("\n");
		sb.append("Continent: ").append(this.continent).append("\n");
		sb.append("Region: ").append(this.region).append("\n");
		sb.append("Surface: ").append(this.surface).append("\n");
		sb.append("Population: ").append(this.population).append("\n");
		sb.append("Government: ").append(this.government).append("\n");
		sb.append("Latitude: ").append(this.latitude).append("\n");
		sb.append("Longitude: ").append(this.longitude).append("\n");
		return sb.toString();
	}
}
