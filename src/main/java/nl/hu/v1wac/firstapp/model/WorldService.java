package nl.hu.v1wac.firstapp.model;

import java.util.List;

import nl.hu.v1wac.firstapp.persistence.CountryDao;
import nl.hu.v1wac.firstapp.persistence.CountryPostgresDaoImpl;

public class WorldService {
	
	private CountryDao countryDaoImpl;
	public WorldService() {
		this.countryDaoImpl = new CountryPostgresDaoImpl();
	}
	public List<Country> getAllCountries() {
		return this.countryDaoImpl.findAll();
	}
	
	public List<Country> get10LargestPopulations() {
		return countryDaoImpl.find10LargestPopulations();
	}

	public List<Country> get10LargestSurfaces() {
		return countryDaoImpl.find10LargestSurfaces();
	}
	
	public Country getCountryByCode(String code) {
		return countryDaoImpl.findByCode(code);
	}
}
