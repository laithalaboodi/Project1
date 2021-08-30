package com.revature.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import com.revature.models.Credentials;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.services.EmployeeServiceImplementation;
import com.revature.services.EmployeeServiceInterface;

/**
 * Servlet implementation class EmployeeServlet
 */
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EmployeeServiceInterface empService = new EmployeeServiceImplementation();

	private ObjectMapper om = new ObjectMapper();

	private HttpSession authenticate(HttpServletRequest req, HttpServletResponse res) {
		HttpSession sess = req.getSession(false);
		System.out.println("Session is:" + sess);
		if (sess == null) {
			throw new UnauthenticatedException();
		} else if (sess.getAttribute("User-Role") == null) {
			throw new UnauthenticatedException();
		} else if (sess.getAttribute("User-Role") != UserRole.EMPLOYEE) {
			throw new UnauthorizedException();
		}
		return sess;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			HttpSession sess = authenticate(req, res);
			String URI = req.getRequestURI().substring(req.getContextPath().length(), req.getRequestURI().length());
			System.out.println(URI);
			if (!"/employee/reimbursements".equals(URI)) {
				res.setStatus(400);
				res.getWriter().write("Method Not Supported");
			}
			List<Reimbursement> allReimbursement = null;
			System.out.println("service controller");
			try {
				allReimbursement = empService.viewPastTickets((User) sess.getAttribute("user"));
			} catch (SQLException e) {
				e.printStackTrace();
				throw new InternalErrorException();
			}
			res.setStatus(200);
//		res.addHeader("Content-Type", "application/json");
			if (allReimbursement == null) {
				throw new InternalErrorException();
			}
			res.getWriter().write(om.writeValueAsString(allReimbursement));
		} catch (AbstractHttpException e) {
			res.setStatus(e.getStatusCode());
			res.getWriter().write(e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			HttpSession sess = authenticate(req, res);
			String URI = req.getRequestURI().substring(req.getContextPath().length(), req.getRequestURI().length());
			System.out.println(URI);
			if (!"/employee/submit".equals(URI)) {
				res.setStatus(400);
				res.getWriter().write("Method Not Supported");
			}
			User u = (User) sess.getAttribute("user");
			Reimbursement r = null;
			System.out.println("service controller");
			try {
				r = om.readValue(req.getInputStream(), Reimbursement.class);
				r.setAuthorID(u.getUserId());
				System.out.println(r);
				r = empService.addReimbursementRequest(u, r);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new InternalErrorException();
			}
			res.setStatus(200);
			res.getWriter().write(om.writeValueAsString(r));
		} catch (AbstractHttpException e) {
			res.setStatus(e.getStatusCode());
			res.getWriter().write(e.getMessage());
		}
	}

}
