package airport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import airport.dao.ReservedTicketDAO;
import airport.model.ConfirmTicket;
import airport.model.Flight;
import airport.model.ReservedTicket;
import airport.model.User;

public class ReservedTicketServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");
		
		String action = request.getParameter("action");
		
		switch(action) {
			case "ticketFinal": {
				String idReservedTicket = request.getParameter("ticketId");
				int id = 0;;
				try {
					id = Integer.parseInt(idReservedTicket);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("id reserved ticket: " + idReservedTicket);
				
				Map<String, Object> data = new HashMap<>();
				String status = "success";
				
				ReservedTicket reservedTicket = ReservedTicketDAO.getById(id);
				
				Flight departureFlight = null;
				try {
					departureFlight = FlightsDAO.getById(reservedTicket.getDepartureFlight().getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Flight incomingFlight = null;
				if(reservedTicket.getIncomingFlight() != null) {
					incomingFlight = FlightsDAO.getById(reservedTicket.getIncomingFlight().getId());	
				}
				
				double totalPrice = departureFlight.getPrice();
				
				List<String> departureSeats = new ArrayList<>(); //u ovu listu se dodaju vec rezervisana sedista za polazni let
				
				List<ReservedTicket> reservedTickets = ReservedTicketDAO.getAll(); // sve (rezervisane) karte
				for(ReservedTicket rt: reservedTickets) {
					if(rt.isDeleted() == false && rt.getDepartureFlight().getId() == departureFlight.getId()) { //ako je let na izabranoj rezervaciji isti kao polazni let sa ove rezervacije
						
						System.out.println("dodaje departure broj sedista: " + rt.getDepartureFlightSeatNumber());
						
						departureSeats.add(String.valueOf(rt.getDepartureFlightSeatNumber())); //dodaje broj sedista tog leta (polaznog) u listu sa rezervisanim sedistima
						
					} else if(rt.isDeleted() == false && rt.getIncomingFlight() != null && rt.getIncomingFlight().getId() == departureFlight.getId()) {
						
						System.out.println("dodaje incoming broj sedista: " + rt.getIncomingFlightSeatNumber());
						
						departureSeats.add(String.valueOf(rt.getIncomingFlightSeatNumber())); //dodaje broj sedista tog leta (povratni) u listu sa rezervisanim sedistima
					}
				}
				
				List<String> departureAvailableSeats = new ArrayList<>(); 
				for(int i=0; i<=departureFlight.getTotalSeatNumber(); i++) { //dodaju se svi brojevi sedista
					departureAvailableSeats.add(String.valueOf(i));
				}
				
				departureAvailableSeats.removeAll(departureSeats); //uklanjanje rezervisanih sedista iz liste sa svim brojevima sedista
				departureAvailableSeats.remove("0");
				
				List<String> incomingSeats = new ArrayList<>(); //u ovu listu se dodaju vec rezervisana sedista za povratni let
				List<String> incomingAvailableSeats = new ArrayList<>();
						
				if (incomingFlight != null) {
				
					for(ReservedTicket rt: reservedTickets) {
						if(rt.isDeleted() == false && rt.getDepartureFlight().getId() == incomingFlight.getId()) {
							
							System.out.println("(povratni let) dodaje departure broj sedista: " + rt.getDepartureFlightSeatNumber());
							
							incomingSeats.add(String.valueOf(rt.getDepartureFlightSeatNumber())); 
							
						} else if (rt.isDeleted() == false && rt.getIncomingFlight() != null && rt.getIncomingFlight().getId() == incomingFlight.getId()) {
							
							System.out.println("(povratni let) dodaje incoming broj sedista: " + rt.getIncomingFlightSeatNumber());
							
							incomingSeats.add(String.valueOf(rt.getIncomingFlightSeatNumber()));
						}
					}
					
					for(int i=0; i<= incomingFlight.getTotalSeatNumber(); i++) { //dodaju se svi brojevi sedista na povratnom letu
						incomingAvailableSeats.add(String.valueOf(i));
					}
					
					incomingAvailableSeats.removeAll(incomingSeats);
					incomingAvailableSeats.remove("0");
					
					totalPrice += incomingFlight.getPrice();
				}
				
				data.put("reservedTicket", reservedTicket);
				data.put("departureFlight", departureFlight);
				data.put("incomingFlight", incomingFlight);
				data.put("departureAvailableSeats", departureAvailableSeats);
				data.put("incomingAvailableSeats", incomingAvailableSeats);
				data.put("totalPrice", totalPrice);
				data.put("status", status);
				data.put("auth", auth);
				
				ObjectMapper mapper = new ObjectMapper();
				String jsonData = mapper.writeValueAsString(data);
				System.out.println(jsonData);
				
				response.setContentType("application/json");
				response.getWriter().write(jsonData);
				break;
			}
		
			case "userTickets": {
				
				String userId = request.getParameter("userId");	
				
				try {
					
					int id = Integer.parseInt(userId);
					
					List<ReservedTicket> reservedTickets = ReservedTicketDAO.findReservedTicketByUserId(id);
					Map<String, Object> data = new HashMap<>();
					data.put("user", auth);
					data.put("reservedTickets", reservedTickets);
					
					ObjectMapper mapper = new ObjectMapper();
					String jsonData = mapper.writeValueAsString(data);
					System.out.println(jsonData);
					
					response.setContentType("application/json");
					response.getWriter().write(jsonData);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
			}
			
			//za flight stranicu - prikaz rezervisanih karata
			case "flightTickets": {
				
				String numberOfFlight = request.getParameter("numberOfFlight");
				System.out.println("nf servlet: " + numberOfFlight);
				
				Flight flight = FlightsDAO.getByNumberOfFlight(numberOfFlight);
				System.out.println("flight id servlet: " + flight.getId());
				
				List<ReservedTicket> reservedTickets = ReservedTicketDAO.findReservedTicketsForFlight(flight.getId());
				
				Map<String, Object> data = new HashMap<>();
				data.put("auth", auth);
				data.put("reservedTickets", reservedTickets);
				
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
			
		} else if (auth.isBlocked() == true) {
			
			status = "blocked";
			
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
			
		}
		
		String action = request.getParameter("action");
		switch(action) {
			case "add": {
				try {
					
					String departureFlightNumber = request.getParameter("departureFlightNumber");
					String incomingFlightNumber = request.getParameter("incomingFlightNumber");
					
					System.out.println("departure flight number REZ: " + departureFlightNumber);
					System.out.println("incoming flight number REZ: " + incomingFlightNumber);
					
					Flight departureFlight = null;
					if(departureFlightNumber != null) {
						try {
							departureFlight = FlightsDAO.getByNumberOfFlight(departureFlightNumber);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					Flight incomingFlight = null;
					if(incomingFlightNumber != null) {
						try {
							incomingFlight = FlightsDAO.getByNumberOfFlight(incomingFlightNumber);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					int lastId = ReservedTicketDAO.last();
					int id = lastId + 1;
					
					Date date = new Date();
					String reservationDate = date.toString();
					
					ReservedTicket reservedTicket = new ReservedTicket();
					reservedTicket.setId(id);
					reservedTicket.setDepartureFlight(departureFlight);
					reservedTicket.setIncomingFlight(incomingFlight);
					reservedTicket.setUser(auth);
					reservedTicket.setReservationDate(reservationDate);
					
					reservedTicket.setDeleted(true); // kada se potvrdi rezervacija ili kupi karta menja se na false
								
					ReservedTicketDAO.add(reservedTicket);
					
					data.put("newReservation", reservedTicket.getId());
					
					break;
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			case "update": {
				
				String ticketId = request.getParameter("ticketId");
				String firstname = request.getParameter("firstname");
				String lastname = request.getParameter("lastname");
				String departureFlightSeatNumber = request.getParameter("departureFlightSeatNumber");
				String incomingFlightSeatNumber = request.getParameter("incomingFlightSeatNumber");
				String price = request.getParameter("price");
				
				int id = 0;
				try {
					id = Integer.parseInt(ticketId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int departureFlightSeatNumber2 = 0;
				try {
					departureFlightSeatNumber2 = Integer.parseInt(departureFlightSeatNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int incomingFlightSeatNumber2 = 0;
				try {
					incomingFlightSeatNumber2 = Integer.parseInt(incomingFlightSeatNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				double price2 = 0;
				try {
					price2 = Double.parseDouble(price);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ReservedTicket reservedTicket = ReservedTicketDAO.getById(id);
				reservedTicket.setFirstname(firstname);
				reservedTicket.setLastname(lastname);
				reservedTicket.setDepartureFlightSeatNumber(departureFlightSeatNumber2);
				reservedTicket.setIncomingFlightSeatNumber(incomingFlightSeatNumber2);
				reservedTicket.setPrice(price2);
				reservedTicket.setDeleted(false);
				
				ReservedTicketDAO.update(reservedTicket);
				break;
			}
			case "buy": {
				
				//prvo se pravi rezervacija (zapravo se update-uje)
				String ticketId = request.getParameter("ticketId");
				String firstname = request.getParameter("firstname");
				String lastname = request.getParameter("lastname");
				String departureFlightSeatNumber = request.getParameter("departureFlightSeatNumber");
				String incomingFlightSeatNumber = request.getParameter("incomingFlightSeatNumber");
				String price = request.getParameter("price");
				
				int id = 0;
				try {
					id = Integer.parseInt(ticketId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int departureFlightSeatNumber2 = 0;
				try {
					departureFlightSeatNumber2 = Integer.parseInt(departureFlightSeatNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int incomingFlightSeatNumber2 = 0;
				try {
					incomingFlightSeatNumber2 = Integer.parseInt(incomingFlightSeatNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				double price2 = 0;
				try {
					price2 = Double.parseDouble(price);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ReservedTicket reservedTicket = ReservedTicketDAO.getById(id);
				reservedTicket.setFirstname(firstname);
				reservedTicket.setLastname(lastname);
				reservedTicket.setDepartureFlightSeatNumber(departureFlightSeatNumber2);
				reservedTicket.setIncomingFlightSeatNumber(incomingFlightSeatNumber2);
				reservedTicket.setPrice(price2);
				reservedTicket.setDeleted(false);
				
				//a onda i potvrda da je kupljena karta

				String boughtTicket = request.getParameter("boughtTicket");
								
				boolean bought = Boolean.parseBoolean(boughtTicket);
				
				reservedTicket.setBoughtTicket(bought);
				
				ReservedTicketDAO.update(reservedTicket);
				
				//confirm ticket 
				ConfirmTicket confirmTicket = new ConfirmTicket();
				int lastId = ConfirmTicketDAO.last();
				int confirmID = lastId + 1;
				
				confirmTicket.setId(confirmID);
				confirmTicket.setReservedTicket(reservedTicket);
				
				ConfirmTicketDAO.add(confirmTicket);
				
				break;			
				
			}
			case "delete": {
				String ticketId = request.getParameter("ticketId");

				
				int id = 0;
				try {
					id = Integer.parseInt(ticketId);
				} catch (Exception e) {
					e.printStackTrace();
				}
								
				ReservedTicket reservedTicket = ReservedTicketDAO.getById(id);
				reservedTicket.setDeleted(true);
				
				ReservedTicketDAO.update(reservedTicket);
				
				ConfirmTicket confirmTicket = ConfirmTicketDAO.getByIdReservedTicket(reservedTicket.getId());
				confirmTicket.setDeleted(true);
				ConfirmTicketDAO.update(confirmTicket);
				
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
