package airport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import airport.model.Airport;

public class AirportDAO {
	
	public static Airport getById(int airportId) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT name FROM airports WHERE id = ?;";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, airportId);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				int index = 1;
				String name = rset.getString(index++);
				
				return new Airport(airportId, name);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return null;
	}
	
	public static Airport getByName(String airportName) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT id FROM airports WHERE name = ?;";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, airportName);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				
				return new Airport(id, airportName);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return null;
	}
	
	public static List<Airport> getAll() {
		
		List<Airport> airports = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT id, name FROM airports;";
			pstmt = conn.prepareStatement(query);
			int index = 1;
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				String name = rset.getString(index++);
				
				Airport airport = new Airport(id, name);
				
				airports.add(airport);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return airports;
	}

}
