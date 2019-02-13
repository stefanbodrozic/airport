package airport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import airport.dao.UserDAO;
import airport.model.User;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User auth = (User) session.getAttribute("auth");
			
		if(auth != null) {
			return;
		}
		
		String message = "Successfully logged in";
		String status = "success";
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			User user = UserDAO.getByUsername(username);
			
			if(user == null) throw new Exception("User not found!");
			
			if(!user.getPassword().equals(password)) throw new Exception("Username or password is incorrect!");
			
			
			session.setAttribute("auth", user);
		} catch (Exception e) {
			message = e.getMessage();
			status = "fail";
		}
		
		Map<String, Object> data = new HashMap<>();
		data.put("message", message);
		data.put("status", status);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writeValueAsString(data);
		System.out.println(jsonData);
		
		response.setContentType("application/json");
		response.getWriter().write(jsonData);
		
	}

}
