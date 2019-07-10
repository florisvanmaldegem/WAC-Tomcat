package nl.hu.v1wac.firstapp.persistence;

import java.sql.*;

public class UserPostgresDaoImpl extends PostgresBaseDao implements UserDao {

	@Override
	public String findRoleForUser(String name, String pass) {
		try (Connection conn = super.getConection()){
			String sql = "SELECT password, role FROM public.useraccount WHERE username = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, name);
			
			ResultSet result = preparedStatement.executeQuery();
			
			while(result.next()) {
				String tPass = result.getString("password");
				String role = result.getString("role");
				if(pass.equals(tPass)) {
					return role;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
