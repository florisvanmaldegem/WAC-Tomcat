package nl.hu.v1wac.firstapp.persistence;

import java.sql.Connection;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public abstract class PostgresBaseDao {
	
	protected final Connection getConection() {
		
		Connection result = null;
		
		try {
			InitialContext ic = new InitialContext();
			
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/PostgresDS");
			result = ds.getConnection();
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
}
