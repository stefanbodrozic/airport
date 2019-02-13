package airport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import airport.dao.ConfirmTicketDAO;
import airport.dao.ReservedTicketDAO;
import airport.dao.UserDAO;
import airport.model.ConfirmTicket;
import airport.model.ReservedTicket;
import airport.model.User;
import airport.model.User.Role;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");
		
		String status = "success";
		
		if(auth == null || auth.isDeleted() == true || auth.isBlocked() == true) {
			status = "fail";
		} else if (auth.getRole() != Role.ADMIN) {
			status = "unauthenticated";
		}
		
		String action = request.getParameter("action");
		
		switch(action) {
			case "all": {
				
				List<User> users = UserDAO.getAll();
				
				Map<String, Object> data = new HashMap<>();
				data.put("users", users);
				data.put("status", status);
				data.put("auth", auth);
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonData = mapper.writeValueAsString(data);
				System.out.println(jsonData);
				
				response.setContentType("application/json");
				response.getWriter().write(jsonData);
				break;
			}
			
			case "single": {
				
				String userId = request.getParameter("userId");
				
				User user = null;
				int id = 0;
				try {
					System.out.println("userId try: " + userId);
					id = Integer.parseInt(userId);
					System.out.println("id try: " + id);
					user = UserDAO.getById(id);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Map<String, Object> data = new HashMap<>();
				data.put("user", user);
				data.put("status", status);
				data.put("auth", auth);
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonData = mapper.writeValueAsString(data);
				System.out.println(jsonData);
				
				response.setContentType("application/json");
				response.getWriter().write(jsonData);
				break;
				
			}
		}
		

	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");
		
		Map<String, Object> data = new HashMap<String, Object>();
		String status = "success";
		
		if(auth == null) {
			status = "fail";
			
			data.put("status", status);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			System.out.println(jsonData);
			
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			return;
		}  else if(auth.getRole() != Role.ADMIN) {
			status = "unauthenticated"; 
			
			data.put("status", status);
			data.put("auth", auth);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			System.out.println(jsonData);
			
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			return;
		}
		
		String action = request.getParameter("action");
		
		switch(action) {
			case "update": {
				
				String userId = request.getParameter("userId");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String role = request.getParameter("role");
				String blocked = request.getParameter("blocked");
				
				int id = 0;
				User user = null;
				boolean block;
				try {
					
					id = Integer.parseInt(userId);
					user = UserDAO.getById(id);
					
					block = Boolean.parseBoolean(blocked);
					
					user.setUsername(username);
					user.setPassword(password);
					user.setRole(Role.valueOf(role));
					user.setBlocked(block);
					
					UserDAO.update(user);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
			}
			
			case "delete": {
				
				String userId = request.getParameter("userId");
				
				try {
					int id = Integer.parseInt(userId);
					
					User user = UserDAO.getById(id);
					
					user.setDeleted(true);
					
					UserDAO.update(user);
					
					List<ReservedTicket> reservedTicketsByUser = ReservedTicketDAO.findReservedTicketByUserId(id);
					List<ConfirmTicket> confirmTicketsByUser = ConfirmTicketDAO.findConfirmedTicketByUserId(id);
					if(reservedTicketsByUser != null) {
						for(ReservedTicket r: reservedTicketsByUser) {
							if(r.getUser().getId() == id) {
								r.setDeleted(true);
								ReservedTicketDAO.update(r);
							}

						}
					}
					if(confirmTicketsByUser != null) {
						for(ConfirmTicket c: confirmTicketsByUser) {
							for(ReservedTicket r: reservedTicketsByUser) {
								if(c.getReservedTicket().getId() == r.getId()) {
									c.setDeleted(true);
									ConfirmTicketDAO.update(c);
								}
							}
						}
					}
					


				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//izbrisati i rezervacije
				
				
				
				break;
			}
		
		}
		
		data.put("status", status);
		data.put("auth", auth);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writeValueAsString(data);
		System.out.println(jsonData);
		
		response.setContentType("application/json");
		response.getWriter().write(jsonData);
		
	}

}
