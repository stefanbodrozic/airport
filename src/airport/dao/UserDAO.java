package airport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import airport.model.User;
import airport.model.User.Role;

public class UserDAO {
	
	public static User getByUsername(String username) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM users WHERE username = ? AND deleted = false;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, username);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				String userName = rset.getString(index++);
				String password = rset.getString(index++);
				String registeredAt = rset.getString(index++);
				Role role = Role.valueOf(rset.getString(index++));
				boolean blocked = rset.getBoolean(index++);
				boolean deleted = rset.getBoolean(index++);
				
				return new User(id, userName, password, registeredAt, role, blocked, deleted);
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

	public static boolean create(User user) {
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		
		try {
			String query = "INSERT INTO users (username, password) VALUES (?, ?);";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, user.getUsername());
			pstmt.setString(index++, user.getPassword());
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
	
	public static int last() {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT id FROM users ORDER BY id DESC LIMIT 1;";
			
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
	
	public static User getById(int id) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM users WHERE id = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, id);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				index = 1;
				int id2 = rset.getInt(index++);
				String userName = rset.getString(index++);
				String password = rset.getString(index++);
				String registeredAt = rset.getString(index++);
				Role role = Role.valueOf(rset.getString(index++));
				boolean blocked = rset.getBoolean(index++);
				boolean deleted = rset.getBoolean(index++);
				
				return new User(id2, userName, password, registeredAt, role, blocked, deleted);
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
	
	public static List<User> getAll() {
		
		List<User> users = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT * FROM users WHERE deleted = false;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				String username = rset.getString(index++);
				String password = rset.getString(index++);
				String date = rset.getString(index++);
				String role = rset.getString(index++);
				boolean blocked = rset.getBoolean(index++);
				boolean deleted = rset.getBoolean(index++);	
							
				User user = new User();
				user.setId(id);
				user.setUsername(username);
				user.setPassword(password);
				user.setRegisteredAt(date);
				user.setRole(User.Role.valueOf(role));
				user.setBlocked(blocked);
				user.setDeleted(deleted);
				
				users.add(user);				
			}
		} catch (Exception e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return users;
	}
	
	//admin panel
	public static List<User> search(String search, boolean username, String searchRole, String sortOption, String order) {
		List<User> users = new ArrayList<>();
		
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT * FROM users WHERE deleted = false ";
			
			List<String> conditions = new ArrayList<>();
						
			if(username && !"".equals(search)) {
				query += "AND username LIKE ? ";
				conditions.add("search");
				System.out.println("if - search");
			}
			
			if(!"".equals(searchRole)) {
				query += "AND role LIKE ? ";
				conditions.add("roleOptions");
				System.out.println("if - role");
			} 
			

			if(!"".equals(sortOption) && !"".equals(order)) {
				if(sortOption.equals("username")) {
					query += "ORDER BY username ";
				} else if(sortOption.equals("role = 'ADMIN'")) {
					query += "ORDER BY role ";
				} else if(sortOption.equals("role = 'USER'")) {
					query += "ORDER BY role ";
				}
				
					
				if(order.equals("ASC")) {
					query += "ASC";
				} else if(order.equals("DESC")) {
					query += "DESC";
				}
					
				conditions.add("sort");
				System.out.println("if - sort");
			}
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			for (String s: conditions) {
				if(s.equals("search")) {
					pstmt.setString(index++, "%" + search + "%");
				} else if (s.equals("roleOptions")) {
					pstmt.setString(index++, searchRole);
				}
			}
			
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			while(rset.next()) {
				index = 1;
				int id = rset.getInt(index++);
				String username2 = rset.getString(index++);
				String password = rset.getString(index++);
				String date = rset.getString(index++);
				String role = rset.getString(index++);
				boolean blocked = rset.getBoolean(index++);
				boolean deleted = rset.getBoolean(index++);
				
				Role role2 = User.Role.valueOf(role);
				
				User user = new User(id, username2, password, date, role2, blocked, deleted);
				
				users.add(user);
			}
		}  catch (SQLException e) {
			System.out.println("SQL query error!");
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			try {rset.close();} catch (SQLException e) {e.printStackTrace();}	
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		
		return users;
	}
	
	public static boolean update(User user) {
		
		Connection conn = ConnectionManager.getConnection();
	
		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE users SET username = ?, password = ?, role = ?, blocked = ?, deleted = ? WHERE id = ?;";
			
			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, user.getUsername());
			pstmt.setString(index++, user.getPassword());
			pstmt.setString(index++, String.valueOf(user.getRole()));
			pstmt.setBoolean(index++, user.isBlocked());
			pstmt.setBoolean(index++, user.isDeleted());
			pstmt.setInt(index++, user.getId());
			
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
	
	
}
