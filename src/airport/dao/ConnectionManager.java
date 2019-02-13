package airport.dao;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;


public class ConnectionManager {

	private static final String DATABASE = "localhost:3306/airport";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	
	private static DataSource dataSource;
	
	public static void open() {
		try {
			Properties dataSourceProperties = new Properties();
			dataSourceProperties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
			dataSourceProperties.setProperty("url", "jdbc:mysql://" + DATABASE + "?useSSL=false");
			dataSourceProperties.setProperty("username", USER_NAME);
			dataSourceProperties.setProperty("password", PASSWORD);
		
			dataSource = BasicDataSourceFactory.createDataSource(dataSourceProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
