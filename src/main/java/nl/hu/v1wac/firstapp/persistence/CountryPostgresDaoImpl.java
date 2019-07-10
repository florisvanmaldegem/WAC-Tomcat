package nl.hu.v1wac.firstapp.persistence;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import nl.hu.v1wac.firstapp.model.Country;

public class CountryPostgresDaoImpl extends PostgresBaseDao implements CountryDao{
	
	
	private List<Country> orderResult(ResultSet result) throws SQLException{
		
		ArrayList<Country> countries = new ArrayList<>();
		while(result.next()) {
			String code = result.getString("code");
			String iso3 = result.getString("iso3");
			String name = result.getString("name");
			String capital = result.getString("capital");
			String continent = result.getString("continent");
			String region = result.getString("region");
			double surface = result.getDouble("surfacearea");
			int population =result.getInt("population");
			String government = result.getString("governmentform");
			double latitude = result.getDouble("latitude");
			double longitude = result.getDouble("longitude");
			
			Country c = new Country(code, iso3, name,
					capital, continent, region,
					surface, population, government,
					latitude, longitude);
			
			countries.add(c);
		}
		return countries;
	}
	public List<Country> findAll(){
		List<Country> countries = null;
		try (Connection conn = super.getConection()){
			
			String query = "SELECT * FROM public.country ORDER BY name DESC";
			
			Statement s = conn.createStatement();
			ResultSet result = s.executeQuery(query);
			
			countries = this.orderResult(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return countries;
	}
	
	public Country findByCode(String codeq) {
		
		Country country = null;
		
		try(Connection conn = super.getConection()){
			String query = "SELECT * FROM public.country WHERE code = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, codeq);
			ResultSet result = preparedStatement.executeQuery();
			
			List<Country> countrys = this.orderResult(result);
			if(countrys.isEmpty()) {
				return null;
			}else {
				for(Country c : countrys) {
					country = c;
				}
				
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return country;
	}
	
	public List<Country> find10LargestPopulations(){
		
		List<Country> countries = null;
		
		try (Connection conn = super.getConection()){
			String query = "SELECT * FROM public.country"
					+ " ORDER BY population DESC"
					+ " LIMIT 10";
			Statement s = conn.createStatement();
			ResultSet result = s.executeQuery(query);
			
			countries = this.orderResult(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return countries;
	}
	
	public List<Country> find10LargestSurfaces(){
			
			List<Country> countries = null;
			
			try (Connection conn = super.getConection()){
				String query = "SELECT * FROM public.country"
						+ " ORDER BY surfacearea DESC"
						+ " LIMIT 10";
				Statement s = conn.createStatement();
				ResultSet result = s.executeQuery(query);
				
				countries = this.orderResult(result);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return countries;
	}
	
	public boolean update(Country country) {
		
		int success = -1;
		try (Connection conn = super.getConection()) {
			String query = "UPDATE public.country"
					+ " SET iso3 = ?,"
					+ " capital = ?,"
					+ " continent = ?,"
					+ " region = ?,"
					+ " surfacearea = ?,"
					+ " population = ?,"
					+ " governmentform = ?"
					+ " WHERE code = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			
			preparedStatement.setString(8, country.getCode());
			
			preparedStatement.setString(1, country.getIso3());
			preparedStatement.setString(2, country.getCapital());
			preparedStatement.setString(3, country.getContinent());
			preparedStatement.setString(4, country.getRegion());
			preparedStatement.setDouble(5, country.getSurface());
			preparedStatement.setInt(6, country.getPopulation());
			preparedStatement.setString(7, country.getGovernment());
		
			success = preparedStatement.executeUpdate();
			System.out.println("Succes with: " + success);
		} catch (SQLException e) {
			e.printStackTrace();
		
			return false;
		}
		return true;
	}
	
	public boolean delete(Country country) {
		int success = -1;
		
		try (Connection conn = super.getConection()) {
			String query = "DELETE FROM public.country"
					+ " WHERE code = ?";
			
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			
			preparedStatement.setString(1, country.getCode());
			
			success = preparedStatement.executeUpdate();
			System.out.println("Succes with: " + success);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean save(Country country) {
		
		try (Connection conn = super.getConection()) {
			String query = "INSERT INTO public.country (code,"
					+ " iso3,name,capital,continent,region,surfacearea,"
					+ " population,governmentform,latitude,longitude)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			
			preparedStatement.setString(1, country.getCode());
			preparedStatement.setString(2, country.getIso3());
			preparedStatement.setString(3, country.getName());
			preparedStatement.setString(4, country.getCapital());
			preparedStatement.setString(5, country.getContinent());
			preparedStatement.setString(6, country.getRegion());
			preparedStatement.setDouble(7, country.getSurface());
			preparedStatement.setInt(8, country.getPopulation());
			preparedStatement.setString(9, country.getGovernment());
			preparedStatement.setDouble(10, country.getLatitude());
			preparedStatement.setDouble(11, country.getLongitude());
			
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
