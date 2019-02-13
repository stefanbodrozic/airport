package airport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import airport.model.Airport;
import airport.model.Flight;

public class FlightsDAO {
	
	public static Flight getById(int id) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM flights WHERE id = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, id);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				index = 1;
				int flightId = rset.getInt(index++);
				String numberOfFlight = rset.getString(index++);
				String dateOfDeparture = rset.getString(index++);
				String arrivalDate = rset.getString(index++);
				int idDepartureAirport = rset.getInt(index++);
				int idIncomingAirport = rset.getInt(index++);
				int totalSeatNumber = rset.getInt(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Airport departureAirport = AirportDAO.getById(idDepartureAirport);
				Airport incomingAirport = AirportDAO.getById(idIncomingAirport);
				
				return new Flight(flightId, numberOfFlight, dateOfDeparture, arrivalDate, departureAirport, incomingAirport, totalSeatNumber, price, deleted);
			}			
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		}  finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return null;
	}
	
	public static Flight getByNumberOfFlight(String numberOfFlight) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM flights WHERE number_of_flight = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, numberOfFlight);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				index = 1;
				int flightId = rset.getInt(index++);
				String number = rset.getString(index++);
				String dateOfDeparture = rset.getString(index++);
				String arrivalDate = rset.getString(index++);
				int idDepartureAirport = rset.getInt(index++);
				int idIncomingAirport = rset.getInt(index++);
				int totalSeatNumber = rset.getInt(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Airport departureAirport = AirportDAO.getById(idDepartureAirport);
				Airport incomingAirport = AirportDAO.getById(idIncomingAirport);
				
				return new Flight(flightId, number, dateOfDeparture, arrivalDate, departureAirport, incomingAirport, totalSeatNumber, price, deleted);
			}			
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		}  finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return null;
	}
	
	public static List<Flight> getAll() {
		
		List<Flight> flights = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT id, number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price, deleted FROM flights "
					+ "WHERE deleted = 'false' AND date_of_departure >= CURRENT_TIMESTAMP ORDER BY date_of_departure ASC;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				String numberOfFlight = rset.getString(index++);
				String dateOfDeparture = rset.getString(index++);
				String arrivalDate = rset.getString(index++);
				int idDepartureAirport = rset.getInt(index++);
				int idIncomingAirport = rset.getInt(index++);
				int totalSeatNumber = rset.getInt(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Airport departureAirport = AirportDAO.getById(idDepartureAirport);
				Airport incomingAirport = AirportDAO.getById(idIncomingAirport);
								
				Flight flight = new Flight(id, numberOfFlight, dateOfDeparture, arrivalDate, departureAirport, incomingAirport, totalSeatNumber, price, deleted);
				
				flights.add(flight);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return flights;
		
	}
	
	public static boolean add(Flight flight) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO flights (number_of_flight, date_of_departure, arrival_date, id_departure_airport, id_incoming_airport, total_seat_number, price) VALUES (?, ?, ?, ?, ?, ?, ?);";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			
			pstmt.setString(index++, flight.getNumberOfFlight());
			pstmt.setString(index++, flight.getDateOfDeparture());
			pstmt.setString(index++, flight.getArrivalDate());
			pstmt.setInt(index++, flight.getDepartureAirport().getId());
			pstmt.setInt(index++, flight.getIncomingAirport().getId());
			pstmt.setInt(index++, flight.getTotalSeatNumber());
			pstmt.setDouble(index++, flight.getPrice());
			
			System.out.println(pstmt);
			
			return pstmt.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return false;
	}
	
	public static boolean update(Flight flight) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE flights SET arrival_date = ?, id_departure_airport = ?, id_incoming_airport = ?, total_seat_number = ?, price = ?, deleted = ? WHERE number_of_flight = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, flight.getArrivalDate());
			pstmt.setInt(index++, flight.getDepartureAirport().getId());
			pstmt.setInt(index++, flight.getIncomingAirport().getId());
			pstmt.setInt(index++, flight.getTotalSeatNumber());
			pstmt.setDouble(index++, flight.getPrice());
			pstmt.setBoolean(index++, flight.isDeleted());
			pstmt.setString(index++, flight.getNumberOfFlight());
			
			System.out.println(pstmt);
			
			return pstmt.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return false;
	}
	
	//
	public static int last() {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT id FROM flights ORDER BY id DESC LIMIT 1;";
			
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			if(rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				
				return id;
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return 0;
	}
	
	// pronadji povratne letove
	public static List<Flight> getIncomingFlights(String numberOfFlight) {
		
		List<Flight> flights = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT F1.id, F1.number_of_flight, F1.date_of_departure, F1.arrival_date, F1.id_departure_airport, F1.id_incoming_airport, F1.total_seat_number, F1.price, F1.deleted FROM "
					+ "flights F, flights F1 WHERE F.number_of_flight = ? AND F1.deleted = false AND F1.date_of_departure > DATE(NOW()) AND F.id_departure_airport = F1.id_incoming_airport AND "
					+ "F.id_incoming_airport = F1.id_departure_airport AND F1.arrival_date > F1.date_of_departure AND F1.date_of_departure > F.arrival_date AND F1.date_of_departure > F.date_of_departure;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(1, numberOfFlight);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				String numberOfFlight1 = rset.getString(index++);
				String dateOfDeparture = rset.getString(index++);
				String arrivalDate = rset.getString(index++);
				int idDepartureAirport = rset.getInt(index++);
				int idIncomingAirport = rset.getInt(index++);
				int totalSeatNumber = rset.getInt(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Airport departureAirport = AirportDAO.getById(idDepartureAirport);
				Airport incomingAirport = AirportDAO.getById(idIncomingAirport);
								
				Flight flight = new Flight(id, numberOfFlight1, dateOfDeparture, arrivalDate, departureAirport, incomingAirport, totalSeatNumber, price, deleted);
				
				flights.add(flight);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return flights;
		
	}

//	pretraga letova
	public static List<Flight> search(String search, boolean ticketNumber, String departureAirportName, String incomingAirportName, String departureDatetime, String arrivalDatetime, double minPrice, double maxPrice, String sortOption, String order) {
		List<Flight> flights = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT * FROM flights WHERE deleted = false AND date_of_departure > DATE(NOW()) ";

			List<String> conditions = new ArrayList<>();
			
			if(ticketNumber && !"".equals(search)) {
				query += "AND number_of_flight LIKE ? ";
				conditions.add("search");
				System.out.println("if - search");
			}
						
			if(!"".equals(departureAirportName)) {
				query += "AND id_departure_airport = (SELECT id FROM airports WHERE name = ?) ";
				conditions.add("departureAirportName");
				System.out.println("if - departureAirportName");
			}
			
			if(!"".equals(incomingAirportName)) {
				query += "AND id_incoming_airport = (SELECT id FROM airports WHERE name = ?) ";
				conditions.add("incomingAirportName");
				System.out.println("if - incomingAirportName");
			}
			
			if(!"".equals(departureDatetime)) {
				query += "AND date_of_departure >= ? ";
				conditions.add("departureDatetime");
				System.out.println("if - departureDatetime");
			}
			
			if(!"".equals(arrivalDatetime)) {
				query += "AND arrival_date <= ? ";
				conditions.add("arrivalDatetime");
				System.out.println("if - arrivalDatetime");
			}
			
			if(minPrice != 0.0) {
				query += "AND price >= ? ";
				conditions.add("minPrice");
				System.out.println("if - minPrice");
			}
			
			if(maxPrice != 0.0) {
				query += "AND price <= ? ";
				conditions.add("maxPrice");
				System.out.println("if - maxPrice");
			}
			
			if(!"".equals(sortOption) && !"".equals(order)) {
				if(sortOption.equals("number_of_flight")) {
					query += "ORDER BY number_of_flight ";
				} else if(sortOption.equals("id_departure_airport")) {
					query += "ORDER BY id_departure_airport ";
				} else if (sortOption.equals("id_incoming_airport")) {
					query += "ORDER BY id_incoming_airport ";
				} else if (sortOption.equals("date_of_departure")) {
					query += "ORDER BY date_of_departure ";
				} else if (sortOption.equals("arrival_date")) {
					query += "ORDER BY arrival_date ";
				} else if (sortOption.equals("price")) {
					query += "ORDER BY price ";
				}
				if(order.equals("ASC")) {
					System.out.println("Sort option: " + sortOption);
					query += "ASC";
				} else if (order.equals("DESC")) {
					query += "DESC";
				}
				conditions.add("sort");
				System.out.println("if - sort");
			}
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			for(String s: conditions) {
				
				if(s.equals("search")) {
					pstmt.setString(index++, "%" + search + "%");
				} else if (s.equals("departureAirportName")) {
					pstmt.setString(index++, departureAirportName);
				} else if (s.equals("incomingAirportName")) {
					pstmt.setString(index++, incomingAirportName);
				} else if (s.equals("departureDatetime")) {
					pstmt.setString(index++, departureDatetime);
				} else if (s.equals("arrivalDatetime")) {
					pstmt.setString(index++, arrivalDatetime);
				} else if (s.equals("minPrice")) {
					pstmt.setDouble(index++, minPrice);
				} else if (s.equals("maxPrice")) {
					pstmt.setDouble(index++, maxPrice);
				}
				
			}

			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				String numberOfFlight = rset.getString(index++);
				String dateOfDeparture = rset.getString(index++);
				String arrivalDate = rset.getString(index++);
				int idDepartureAirport = rset.getInt(index++);
				int idIncomingAirport = rset.getInt(index++);
				int totalSeatNumber = rset.getInt(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Airport departureAirport = AirportDAO.getById(idDepartureAirport);
				Airport incomingAirport = AirportDAO.getById(idIncomingAirport);
								
				Flight flight = new Flight(id, numberOfFlight, dateOfDeparture, arrivalDate, departureAirport, incomingAirport, totalSeatNumber, price, deleted);
				
				flights.add(flight);
			}			
		} catch (SQLException e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return flights;
	}
}
