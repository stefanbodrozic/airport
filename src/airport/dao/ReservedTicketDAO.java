package airport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import airport.model.Flight;
import airport.model.ReservedTicket;
import airport.model.User;

public class ReservedTicketDAO {

	//izbroj koliko ima rezervisanih mesta za izabrani let
	public static int countReservedTickets(int flightId) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT COUNT(*) FROM reserved_ticket WHERE bought_ticket = false AND deleted = false AND (id_departure_flight = ? OR id_incoming_flight = ?);";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, flightId);
			pstmt.setInt(2, flightId);
			System.out.println(pstmt);
			rset = pstmt.executeQuery();
			if(rset.next()) {
				int index = 1;
				int count = rset.getInt(index++);
				
				return count;
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
	
	public static boolean add(ReservedTicket reservedTicket) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO reserved_ticket (id_departure_flight, departure_flight_seat_number, id_incoming_flight, incoming_flight_seat_number, user_id, firstname, lastname, bought_ticket, price, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			
			pstmt.setInt(index++, reservedTicket.getDepartureFlight().getId());
			pstmt.setInt(index++, reservedTicket.getDepartureFlightSeatNumber());
			
			if(reservedTicket.getIncomingFlight() != null) {
				pstmt.setInt(index++, reservedTicket.getIncomingFlight().getId()); //povratni let ne mora da postoji
			} else {
				pstmt.setString(index++, null);
			}	
			
			if(reservedTicket.getIncomingFlightSeatNumber() == 0) { //u prvom koraku rezervacije se ne unosi broj sedista
				pstmt.setString(index++, null);
			} else {
				pstmt.setInt(index++, reservedTicket.getIncomingFlightSeatNumber());
			}
			
			pstmt.setInt(index++, reservedTicket.getUser().getId());
			
			pstmt.setString(index++, reservedTicket.getFirstname());
			
			pstmt.setString(index++, reservedTicket.getLastname());
			
			pstmt.setBoolean(index++, reservedTicket.isBoughtTicket());
			pstmt.setDouble(index++, reservedTicket.getPrice());
			pstmt.setBoolean(index++, reservedTicket.isDeleted());
			
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
	
	// poslednja rezervisana karta, potrebno zbog ID prilikom kreiranja nove rezervacije (za objekat)
	public static int last() {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT id FROM reserved_ticket ORDER BY id DESC LIMIT 1;";
			
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
	
	
	//pronalazi sve rezervisane karte.. na servletu se proverava da li ima karata za odredjeni let i sve ostalo
	public static List<ReservedTicket> getAll() {
		
		List<ReservedTicket> reservedTickets = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT * FROM reserved_ticket;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				int idDepartureFlight = rset.getInt(index++);
				int departureFlightSeatNumber = rset.getInt(index++);
				int idIncomingFlight = rset.getInt(index++);
				int incomingFlightSeatNumber = rset.getInt(index++);
				String date = rset.getString(index++);
				int userId = rset.getInt(index++);
				String firstname = rset.getString(index++);
				String lastname = rset.getString(index++);
				boolean boughtTicket = rset.getBoolean(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Flight departureFlight = FlightsDAO.getById(idDepartureFlight);
				Flight incomingFlight = FlightsDAO.getById(idIncomingFlight);
				User user = UserDAO.getById(userId);
				
				ReservedTicket reservedTicket = new ReservedTicket(id, departureFlight, departureFlightSeatNumber, incomingFlight, incomingFlightSeatNumber, date, user, firstname, lastname, boughtTicket, price, deleted);
				
				reservedTickets.add(reservedTicket);
			}
			
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return reservedTickets;
	}
	
	public static ReservedTicket getById(int id) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT * FROM reserved_ticket WHERE id = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, id);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				index = 1;
				int reservedTicketId = rset.getInt(index++);
				int idDepartureFlight = rset.getInt(index++);
				int departureFlightSeatNumber = rset.getInt(index++);
				int idIncomingFlight = rset.getInt(index++);
				int incomingFlightSeatNumber = rset.getInt(index++);
				String reservationDate = rset.getString(index++);
				int userId = rset.getInt(index++);
				String firstname = rset.getString(index++);
				String lastname = rset.getString(index++);
				boolean boughtTicket = rset.getBoolean(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Flight departureFlight = FlightsDAO.getById(idDepartureFlight);
				Flight incomingFlight = null;
				if(idIncomingFlight != 0) {
					incomingFlight = FlightsDAO.getById(idIncomingFlight);
				} 
				
				User user = UserDAO.getById(userId);
								
				return new ReservedTicket(reservedTicketId, departureFlight, departureFlightSeatNumber, incomingFlight, incomingFlightSeatNumber, reservationDate, user, firstname, lastname, boughtTicket, price, deleted);
				
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

	public static boolean update(ReservedTicket reservedTicket) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE reserved_ticket SET departure_flight_seat_number = ?, incoming_flight_seat_number = ?, firstname = ?, lastname = ?, price = ?, bought_ticket = ?, deleted = ? WHERE id = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, reservedTicket.getDepartureFlightSeatNumber());
			pstmt.setInt(index++, reservedTicket.getIncomingFlightSeatNumber());
			pstmt.setString(index++, reservedTicket.getFirstname());
			pstmt.setString(index++, reservedTicket.getLastname());
			pstmt.setDouble(index++, reservedTicket.getPrice());
			pstmt.setBoolean(index++, reservedTicket.isBoughtTicket());
			pstmt.setBoolean(index++, reservedTicket.isDeleted());
			pstmt.setInt(index++, reservedTicket.getId());
			
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
	
	public static List<ReservedTicket> findReservedTicketByUserId(int userId) {
		
		List<ReservedTicket> reservedTickets = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT * FROM reserved_ticket WHERE user_id = ? AND bought_ticket = false ORDER BY reservation_date DESC;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, userId);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				int idDepartureFlight = rset.getInt(index++);
				int departureFlightSeatNumber = rset.getInt(index++);
				int idIncomingFlight = rset.getInt(index++);
				int incomingFlightSeatNumber = rset.getInt(index++);
				String date = rset.getString(index++);
				int userId2 = rset.getInt(index++);
				String firstname = rset.getString(index++);
				String lastname = rset.getString(index++);
				boolean boughtTicket = rset.getBoolean(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Flight departureFlight = FlightsDAO.getById(idDepartureFlight);
				Flight incomingFlight = FlightsDAO.getById(idIncomingFlight);
				User user = UserDAO.getById(userId2);
				
				ReservedTicket reservedTicket = new ReservedTicket(id, departureFlight, departureFlightSeatNumber, incomingFlight, incomingFlightSeatNumber, date, user, firstname, lastname, boughtTicket, price, deleted);
				
				reservedTickets.add(reservedTicket);
			}
		

		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
				
		return reservedTickets;
	}
	
	// flight servlet - brisanje rezervacija kad se brise let
	public static List<ReservedTicket> findReservedTicketByDepartureOrIncomingFlight(int flightId) {
		
		List<ReservedTicket> reservedTickets = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT * FROM reserved_ticket WHERE id_departure_flight = ? OR id_incoming_flight = ? AND bought_ticket = false;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, flightId);
			pstmt.setInt(index++, flightId);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				int idDepartureFlight = rset.getInt(index++);
				int departureFlightSeatNumber = rset.getInt(index++);
				int idIncomingFlight = rset.getInt(index++);
				int incomingFlightSeatNumber = rset.getInt(index++);
				String date = rset.getString(index++);
				int userId2 = rset.getInt(index++);
				String firstname = rset.getString(index++);
				String lastname = rset.getString(index++);
				boolean boughtTicket = rset.getBoolean(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Flight departureFlight = FlightsDAO.getById(idDepartureFlight);
				Flight incomingFlight = FlightsDAO.getById(idIncomingFlight);
				User user = UserDAO.getById(userId2);
				
				ReservedTicket reservedTicket = new ReservedTicket(id, departureFlight, departureFlightSeatNumber, incomingFlight, incomingFlightSeatNumber, date, user, firstname, lastname, boughtTicket, price, deleted);
				
				reservedTickets.add(reservedTicket);
			}
		

		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
				
		return reservedTickets;
	}
	
	//na flight stranici - prikazuje rezervisane karte
	public static List<ReservedTicket> findReservedTicketsForFlight(int id) {
		
		List<ReservedTicket> reservedTickets = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT * FROM reserved_ticket WHERE deleted = false AND bought_ticket = false AND (id_departure_flight = ? OR id_incoming_flight = ?);";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, id);
			pstmt.setInt(index++, id);
			
			System.out.println("rezervisane karte query: " + pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				
				int id2 = rset.getInt(index++);
				int idDepartureFlight = rset.getInt(index++);
				int departureFlightSeatNumber = rset.getInt(index++);
				int idIncomingFlight = rset.getInt(index++);
				int incomingFlightSeatNumber = rset.getInt(index++);
				String date = rset.getString(index++);
				int userId2 = rset.getInt(index++);
				String firstname = rset.getString(index++);
				String lastname = rset.getString(index++);
				boolean boughtTicket = rset.getBoolean(index++);
				double price = rset.getDouble(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Flight departureFlight = FlightsDAO.getById(idDepartureFlight);
				Flight incomingFlight = FlightsDAO.getById(idIncomingFlight);
				User user = UserDAO.getById(userId2);
				
				ReservedTicket reservedTicket = new ReservedTicket(id2, departureFlight, departureFlightSeatNumber, incomingFlight, incomingFlightSeatNumber, date, user, firstname, lastname, boughtTicket, price, deleted);
				
				reservedTickets.add(reservedTicket);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}

		return reservedTickets;
	}

}
