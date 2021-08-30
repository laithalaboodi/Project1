package com.revature.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.enums.UserRole;
import com.revature.exceptions.AbstractHttpException;
import com.revature.exceptions.InternalErrorException;
import com.revature.exceptions.UnauthenticatedException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.Credentials;
import com.revature.models.User;
import com.revature.services.UserServiceImplementation;
import com.revature.services.UserServiceInterface;

/**
 * Servlet implementation class FrontController
 */
public class LoginServlet extends HttpServlet {

	private UserServiceInterface userService = new UserServiceImplementation();

	private ObjectMapper om = new ObjectMapper();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			authenticateLogin(req, res);
		} catch (AbstractHttpException e) {
			res.setStatus(e.getStatusCode());
			res.getWriter().write(e.getMessage());
		}
	}

	protected void authenticateLogin(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// be our front controller
		String URI = req.getRequestURI().substring(req.getContextPath().length(), req.getRequestURI().length());
		System.out.println(URI);
		switch (URI) {
		case "/login": {
			switch (req.getMethod()) {
			case "POST": {
				Credentials cred = om.readValue(req.getInputStream(), Credentials.class);
				System.out.println(cred);
				User u = null;
				try {
					u = userService.login(cred.getUsername(), cred.getPassword());
					System.out.println(u);
				} catch (SQLException e) {
					e.printStackTrace();
					throw new UserNotFoundException();
				}
				if (u == null) {
					throw new UserNotFoundException();
				}
				// use your session to keep track of your user permission level
				HttpSession sess = req.getSession();

				if (u.getRole() == UserRole.EMPLOYEE) {
					sess.setAttribute("User-Role", UserRole.EMPLOYEE);
				} else if (u.getRole() == UserRole.FINANCE_MANAGER) {
					sess.setAttribute("User-Role", UserRole.FINANCE_MANAGER);
				}
				sess.setAttribute("user", u);
				System.out.println(sess.getId());

				res.setStatus(200);
				res.getWriter().write(om.writeValueAsString(u));
				break;
			}
			default: {
				res.setStatus(400);
				res.getWriter().write("Method Not Supported");
				break;
			}

			}
			break;
		}
		default: {
			res.setStatus(404);
			res.getWriter().write("No Such Resource");
			break;
		}

		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			authenticate(req, res);
			res.setStatus(200);
			res.getWriter().write("athenticated");
		} catch (AbstractHttpException e) {
			res.setStatus(e.getStatusCode());
			res.getWriter().write(e.getMessage());
		}
	}

	private HttpSession authenticate(HttpServletRequest req, HttpServletResponse res) {
		String URI = req.getRequestURI().substring(req.getContextPath().length(), req.getRequestURI().length());
		System.out.println(URI);
		UserRole expectedRole = null;
		switch (URI) {
		case "/login/employee": {
			expectedRole = UserRole.EMPLOYEE;
			break;
		}
		case "/login/financemanager": {
			expectedRole = UserRole.FINANCE_MANAGER;
			break;
		}
		default: {
			throw new InternalErrorException();
		}
		}

		HttpSession sess = req.getSession(false);
		System.out.println("Session is:" + sess);
		if (sess == null) {
			throw new UnauthenticatedException();
		} else if (sess.getAttribute("User-Role") == null) {
			throw new UnauthenticatedException();
		} else if (sess.getAttribute("User-Role") != expectedRole) {
			throw new UnauthorizedException();
		}

		return sess;
	}

}
