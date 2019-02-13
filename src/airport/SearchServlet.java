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

import airport.dao.FlightsDAO;
import airport.dao.UserDAO;

import airport.model.Flight;
import airport.model.User;
import airport.model.User.Role;


public class SearchServlet extends HttpServlet {
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
		
		case "index": {
			
			String search = request.getParameter("search");
			boolean ticketNumberCheckBox = Boolean.parseBoolean(request.getParameter("ticketNumberCheckBox"));
			String departureAirportName = request.getParameter("departureAirport");
			String incomingAirportName = request.getParameter("incomingAirport");
			String departureDatetime = request.getParameter("departureDatetime");
			String arrivalDatetime = request.getParameter("arrivalDatetime");
			String minPriceS = request.getParameter("minPrice");
			String maxPriceS = request.getParameter("maxPrice");
			String sort = request.getParameter("sort");
			
			System.out.println("Servlet - search: " +search);
			System.out.println("Servlet - ticketNumberCheckBox: " + ticketNumberCheckBox);
			System.out.println("Servlet - departureAirportName: " + departureAirportName);
			System.out.println("Servlet - incomingAirportName: " + incomingAirportName);
			System.out.println("Servlet - departureDatetime: " + departureDatetime);
			System.out.println("Servlet - arrivalDatetime: " + arrivalDatetime);
			System.out.println("Servlet - minPriceS: " + minPriceS);
			System.out.println("Servlet - maxPriceS: " + maxPriceS);
			System.out.println("Servlet - sort: " + sort);
			
			String sortOption = "";
			String order = "";
			String sortOptionFinal = "";
			
			if(!"".equals(sort)) {
				
				String [] tokens = sort.split("-");
				
				try {
					sortOption = tokens[0];
					order = tokens[1];
					
					System.out.println("Sort option: " + sortOption);
					
					if(sortOption.equals("Number of flight")) sortOptionFinal = "number_of_flight";
					
					if(sortOption.equals("Departure airport")) sortOptionFinal = "id_departure_airport";
					
					if(sortOption.equals("Incoming airport")) sortOptionFinal = "id_incoming_airport";
					
					if(sortOption.equals("Departure date")) sortOptionFinal = "date_of_departure";
					
					if(sortOption.equals("Arrival date")) sortOptionFinal = "arrival_date";
					
					if(sortOption.equals("Price")) sortOptionFinal = "price";

					System.out.println("Sort option FINAL: " + sortOptionFinal);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

			double minPrice = 0;
			double maxPrice = 0;
			
			
			if(!"".equals(minPriceS) || !"".equals(maxPriceS)) {
				try {
					minPrice = Double.parseDouble(minPriceS);
					
					maxPrice = Double.parseDouble(maxPriceS);
				} catch (Exception e) {
					e.printStackTrace();
				}
					
			}
		
			List<Flight> flights = FlightsDAO.search(search, ticketNumberCheckBox, departureAirportName, incomingAirportName, departureDatetime, arrivalDatetime, minPrice, maxPrice, sortOptionFinal, order);
			
			Map<String, Object> data = new HashMap<>();
			data.put("flights", flights);
			data.put("status", status);
			data.put("auth", auth);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			System.out.println(jsonData);
			
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			break;
		}
		
		case "adminPanel": {
			
			String search = request.getParameter("search");
			boolean usernameSearch = Boolean.parseBoolean(request.getParameter("usernameSearch"));
			String searchRole = request.getParameter("searchRole");
			String sortOption = request.getParameter("sortOption");

			System.out.println("Servlet - search: " +search);
			System.out.println("Servlet - usernameSearch: " + usernameSearch);
			System.out.println("Servlet - searchRole: " + searchRole);
			System.out.println("Servlet - sortOption: " + sortOption);
			
			String order = "";
			String sortOptionFinal = "";
			
			if(!"".equals(sortOption)) {
				
				String [] tokens = sortOption.split("-");
				
				try {
					sortOption = tokens[0];
					order = tokens[1];
					
					System.out.println("Sort option: " + sortOption);
					
					if(sortOption.equals("Username")) sortOptionFinal = "username";
					
					if(sortOption.equals("ADMIN")) sortOptionFinal = "role = 'ADMIN'";
					
					if(sortOption.equals("USER")) sortOptionFinal = "role = 'USER'";					

					System.out.println("Servlet - sort option: " + sortOption);
					System.out.println("Servlet - sortOptionFinal: " + sortOptionFinal);
					System.out.println("Servlet - order: " + order);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			List<User> users = UserDAO.search(search, usernameSearch, searchRole, sortOptionFinal, order);
			
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
		
		case "": {
			
		}
		}
				
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
