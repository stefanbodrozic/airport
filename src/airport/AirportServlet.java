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

import airport.dao.AirportDAO;
import airport.model.Airport;
import airport.model.User;
import airport.model.User.Role;

public class AirportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//proveravam da li je prijavljeni korisnik admin i ako nije zabranjuje se pristup stranici add-flight
		
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");
		
		String status = "success";
		
		if(auth == null || auth.isDeleted() == true || auth.isBlocked() == true) {
			status = "fail";
		} else if (auth.getRole() != Role.ADMIN) {
			status = "unauthenticated";
		}
		
		List<Airport> airports = AirportDAO.getAll();
		
		Map<String, Object> data = new HashMap<>();
		data.put("airports", airports);
		data.put("status", status);
		data.put("auth", auth);
	
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writeValueAsString(data);
		System.out.println(jsonData);
		
		response.setContentType("application/json");
		response.getWriter().write(jsonData);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
