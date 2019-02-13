package airport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import airport.dao.AirportDAO;
import airport.dao.ConfirmTicketDAO;
import airport.dao.FlightsDAO;
import airport.dao.ReservedTicketDAO;
import airport.model.Airport;
import airport.model.Flight;
import airport.model.ReservedTicket;
import airport.model.User;
import airport.model.User.Role;

public class FlightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");
		
		String numberOfFlight = request.getParameter("numberOfFlight");
		System.out.println("nf: " + numberOfFlight);
		
		Map<String, Object> data = new HashMap<>();
		String status = "success";

		Flight flight = FlightsDAO.getByNumberOfFlight(numberOfFlight);
		if(flight == null) {
			status = "fail";
		}
		
		List<Flight> incomingFlights = FlightsDAO.getIncomingFlights(numberOfFlight); //pronalazi povratne letove
		
		int countReservedTickets = ReservedTicketDAO.countReservedTickets(flight.getId());
		int countBoughtTickets = ConfirmTicketDAO.countBoughtTickets(flight.getId());
					
		System.out.println("rezervisano: " + countReservedTickets);
		System.out.println("kupljeno: " + countBoughtTickets);
		int reservedAndBoughtTicketNumber = countReservedTickets + countBoughtTickets; //ukupno rezervisano i kupljeno karata (za prikaz ispod leta)
		

		
		List<String> seats = new ArrayList<>(); //nova lista u koju se dodaju brojevi sedista
		
		List<ReservedTicket> reservedTickets = ReservedTicketDAO.getAll(); //sve (rezervisane) karte
		for(ReservedTicket rt: reservedTickets) {
			if(rt.isDeleted() == false && rt.getDepartureFlight().getId() == flight.getId()) {  //ako je let na rezervisanoj karti isti kao i izabrani let na stranici
				
				System.out.println("dodaje departure broj sedista: " + rt.getDepartureFlightSeatNumber()); 
				
				seats.add(String.valueOf(rt.getDepartureFlightSeatNumber())); //dodaje broj sedista tog leta (polaznog) u listu sa svim brojevima sedista
				
			} else if(rt.isDeleted() == false && rt.getIncomingFlight() != null && rt.getIncomingFlight().getId() == flight.getId()) {
				
				System.out.println("dodaje incoming broj sedista: " + rt.getIncomingFlightSeatNumber());
				
				seats.add(String.valueOf(rt.getIncomingFlightSeatNumber()));  //dodaje broj sedista tog leta (povratni) u listu sa svim brojevima sedista
			}
		}
		
		System.out.println("rezervisana sedista: " + seats);
		

		List<String> availableSeats = new ArrayList<>();
		for(int i=1; i <= flight.getTotalSeatNumber(); i++) {
			availableSeats.add(String.valueOf(i));			
		}
		
		availableSeats.removeAll(seats); //iz liste gde se nalaze sva sedista sklanja ona koja su rezervisana

		System.out.println("dostupna sedista: " + availableSeats);
				
		data.put("flight", flight);
		data.put("incomingFlights", incomingFlights);
		data.put("reservedAndBoughtTicketNumber", reservedAndBoughtTicketNumber);
		data.put("availableSeats", availableSeats);
		data.put("status", status);
		data.put("auth", auth);
	
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writeValueAsString(data);
		System.out.println(jsonData);
		
		response.setContentType("application/json");
		response.getWriter().write(jsonData);
			
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
		} else if (auth.isDeleted() == true) {
			status = "deleted";
			
			data.put("status", status);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			System.out.println(jsonData);
			
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			return;
		} else if(auth.getRole() != Role.ADMIN) {
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
		switch (action) {
			case "add": {
				String dateOfDeparture = request.getParameter("departureDatetime");
				String arrivalDate = request.getParameter("arrivalDatetime");
				String departureAirportName = request.getParameter("departureAirport");
				String incomingAirportName = request.getParameter("incomingAirport");
				int totalSeatNumber = Integer.parseInt(request.getParameter("totalSeatNumber"));
				double price = Double.parseDouble(request.getParameter("price"));
				
				int lastId = FlightsDAO.last();
				int id = lastId + 1;
				String numberOfFlight = "F" + id;
				
				Airport departureAirport = AirportDAO.getByName(departureAirportName);
				Airport incomingAirport = AirportDAO.getByName(incomingAirportName);
			
				//ispravnost datuma se proverava na frontendu!
				Flight flight = new Flight(id, numberOfFlight, dateOfDeparture, arrivalDate, departureAirport, incomingAirport, totalSeatNumber, price, false);
				
				FlightsDAO.add(flight);
				
				break;
			}
			
			case "update": {
				String numberOfFlight = request.getParameter("numberOfFlight");
				
				String arrivalDate = request.getParameter("arrivalDatetime");
				String departureAirportName = request.getParameter("departureAirport");
				String incomingAirportName = request.getParameter("incomingAirport");
				int totalSeatNumber = Integer.parseInt(request.getParameter("totalSeatNumber"));
				double price = Double.parseDouble(request.getParameter("price"));
							
				Airport departureAirport = AirportDAO.getByName(departureAirportName);
				Airport incomingAirport = AirportDAO.getByName(incomingAirportName);
				
				Flight flight = FlightsDAO.getByNumberOfFlight(numberOfFlight);
				flight.setArrivalDate(arrivalDate);
				flight.setDepartureAirport(departureAirport);
				flight.setIncomingAirport(incomingAirport);
				flight.setTotalSeatNumber(totalSeatNumber);
				flight.setPrice(price);
				
				FlightsDAO.update(flight);
				
				break;
			}
			
			case "delete": {
				String numberOfFlight = request.getParameter("numberOfFlight");
				
				try {
					Flight flight = FlightsDAO.getByNumberOfFlight(numberOfFlight);
					
					flight.setDeleted(true);
					
					//izbrisati i rezervacije za izbrisani let!!!
					
					List<ReservedTicket> reservedTickets = ReservedTicketDAO.findReservedTicketByDepartureOrIncomingFlight(flight.getId());
					
					for(ReservedTicket rt: reservedTickets) {
						rt.setDeleted(true);
						ReservedTicketDAO.update(rt);
					}
					
					
					FlightsDAO.update(flight);				
				} catch (Exception e) {
					e.printStackTrace();
				}			
				
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
	
