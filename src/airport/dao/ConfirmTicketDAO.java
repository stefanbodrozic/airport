package airport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import airport.model.ConfirmTicket;
import airport.model.ReservedTicket;

public class ConfirmTicketDAO {

	//izbroj koliko ima kupljenih mesta za izabrani let
	public static int countBoughtTickets(int flightId) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT COUNT(*) FROM confirm_ticket C WHERE C.deleted = false AND C.id_reserved_ticket IN (SELECT R.id FROM reserved_ticket R WHERE R.deleted = false AND r.bought_ticket = true AND (R.id_departure_flight = ? OR R.id_incoming_flight = ?))";
			
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
	
	public static boolean add(ConfirmTicket confirmTicket) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			
			String query = "INSERT INTO confirm_ticket (id_reserved_ticket) VALUES (?);";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			
			pstmt.setInt(index++, confirmTicket.getReservedTicket().getId());
			
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
	
	public static boolean update (ConfirmTicket confirmTicket) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE confirm_ticket SET deleted = ? WHERE id = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setBoolean(index++, confirmTicket.isDeleted());
			pstmt.setInt(index++, confirmTicket.getId());
			
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
	
	//poslednja prodata karta, potrebno zbog ID prilikom kreiranja nove prodate karte
	public static int last() {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT id FROM confirm_ticket ORDER BY id DESC LIMIT 1;";
			
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
	
	public static ConfirmTicket getByIdReservedTicket(int idReservedTicket) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT * FROM confirm_ticket WHERE id_reserved_ticket = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, idReservedTicket);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				int idReservedTicket2 = rset.getInt(index++);
				String confirmDate = rset.getString(index++);
				boolean deleted = rset.getBoolean(index++);
				
				ReservedTicket reservedTicket = ReservedTicketDAO.getById(idReservedTicket2);
				
				return new ConfirmTicket(id, reservedTicket, confirmDate, deleted);
				
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
	
	public static List<ConfirmTicket> findConfirmedTicketByUserId(int userId) {
		List<ConfirmTicket> confirmTickets = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT A.id, A.id_reserved_ticket, A.confirm_date, A.deleted FROM confirm_ticket A, reserved_ticket B WHERE B.user_id = ? AND B.deleted = false AND B.bought_ticket = true AND B.id = A.id_reserved_ticket AND A.deleted = false ORDER BY confirm_date DESC;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, userId);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				int idReservedTicket2 = rset.getInt(index++);
				String confirmDate = rset.getString(index++);
				boolean deleted = rset.getBoolean(index++);
				
				ReservedTicket reservedTicket = ReservedTicketDAO.getById(idReservedTicket2);
				
				ConfirmTicket confirmTicket = new ConfirmTicket(id, reservedTicket, confirmDate, deleted);
				
				confirmTickets.add(confirmTicket);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return confirmTickets;
	}
	
	public static List<ConfirmTicket> findConfirmTicketForFlight(int id) {

		List<ConfirmTicket> confirmTickets = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			
			String query = "SELECT A.* FROM confirm_ticket A, reserved_ticket B WHERE A.deleted = false AND A.id_reserved_ticket = B.id AND (B.deleted = false AND B.bought_ticket = true) AND (B.id_departure_flight = ? OR B.id_incoming_flight = ?)";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, id);
			pstmt.setInt(index++, id);
			
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id2 = rset.getInt(index++);
				int idReservedTicket2 = rset.getInt(index++);
				String confirmDate = rset.getString(index++);
				boolean deleted = rset.getBoolean(index++);
				
				ReservedTicket reservedTicket = ReservedTicketDAO.getById(idReservedTicket2);
				
				ConfirmTicket confirmTicket = new ConfirmTicket(id2, reservedTicket, confirmDate, deleted);
				
				confirmTickets.add(confirmTicket);
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return confirmTickets;
	}
}
