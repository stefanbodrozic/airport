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
import airport.dao.FlightsDAO;
import airport.model.ConfirmTicket;
import airport.model.Flight;
import airport.model.User;

public class ConfirmTicketServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");
		
		String action = request.getParameter("action");
		switch(action) {
			case "userTickets": {
			
				String userId = request.getParameter("userId");
				
				Map<String, Object> data = new HashMap<>();
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
					
				}
				
				int id = 0;
				try {
					id = Integer.parseInt(userId);
				} catch (Exception e) {
					e.printStackTrace();
				}

				
				List<ConfirmTicket> confirmTickets = ConfirmTicketDAO.findConfirmedTicketByUserId(id);
				
				data.put("confirmTickets", confirmTickets);
				data.put("status", status);
				data.put("auth", auth);
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonData = mapper.writeValueAsString(data);
				System.out.println(jsonData);
				
				response.setContentType("application/json");
				response.getWriter().write(jsonData);
				break;
			}
			
			case "flightTickets": {
				
				String numberOfFlight = request.getParameter("numberOfFlight");

				Flight flight = FlightsDAO.getByNumberOfFlight(numberOfFlight);
				
				List<ConfirmTicket> confirmTickets = ConfirmTicketDAO.findConfirmTicketForFlight(flight.getId());
				
				Map<String, Object> data = new HashMap<>();
				data.put("auth", auth);
				data.put("confirmTicket", confirmTickets);
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonData = mapper.writeValueAsString(data);
				System.out.println(jsonData);
				
				response.setContentType("application/json");
				response.getWriter().write(jsonData);
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
