package airport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import airport.model.User.Role;

public class RegisterServlet extends HttpServlet {
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
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String repeatedPassword = request.getParameter("repeatedPassword");
		
		String message = "Successfully registered";
		String status = "success";
		try {
			if("".equals(username)) throw new Exception("Username error!");
			User checkUser = UserDAO.getByUsername(username);
			if(checkUser != null) throw new Exception("Username already exist!");
			
			if("".equals(password) || "".equals(repeatedPassword) || !password.equals(repeatedPassword)) throw new Exception("Password error!");
						
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			User user = new User();
			
			user.setId(UserDAO.last() + 1);
			user.setUsername(username);
			user.setPassword(password);
			user.setRole(Role.USER);
			user.setRegisteredAt(currentTime);
			user.setBlocked(false);
			user.setDeleted(false);
			
			UserDAO.create(user);
			System.out.println(user.toString());
			
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
