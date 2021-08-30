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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.enums.ReimbursementStatus;
import com.revature.enums.UserRole;
import com.revature.exceptions.AbstractHttpException;
import com.revature.exceptions.InternalErrorException;
import com.revature.exceptions.UnauthenticatedException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;
import com.revature.repositories.ReimbursementPostgresDAO;
import com.revature.services.FinanceManagerServiceImplementation;
import com.revature.services.FinanceManagerServiceInterface;

import jdk.internal.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 * Servlet implementation class FinanceManagerServlet
 */
public class FinanceManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	FinanceManagerServiceInterface fmService = new FinanceManagerServiceImplementation();

	private ObjectMapper om = new ObjectMapper();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try{
		// be our front controller
		authenticate(req, res);
		
		directGetRequest(req, res);
	}
		catch (AbstractHttpException e) {
			res.setStatus(e.getStatusCode());
			res.getWriter().write(e.getMessage());
		}
	}
	
	private HttpSession authenticate(HttpServletRequest req, HttpServletResponse res) {
		HttpSession sess = req.getSession(false);
		System.out.println("Session is:" + sess);
		if(sess == null) {
			throw new UnauthenticatedException();
		}
		else if(sess.getAttribute("User-Role") == null) {
			throw new UnauthenticatedException();
		} else if(sess.getAttribute("User-Role") != UserRole.FINANCE_MANAGER) {
			throw new UnauthorizedException();
		}
		return sess;
	}
	
	private void directGetRequest(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {

		List<Reimbursement> allReimbursement = null;
		String URI = req.getRequestURI().substring(req.getContextPath().length(), req.getRequestURI().length());

		System.out.println(URI);
		try {
			switch (URI) {
			case "/manager/all": {
				allReimbursement = fmService.viewAllReimbursements();
				break;
			}
			case "/manager/pending": {
				allReimbursement = fmService.viewAllReimbursementsByStatus(ReimbursementStatus.PENDING);
				break;
			}case "/manager/approved": {
				allReimbursement = fmService.viewAllReimbursementsByStatus(ReimbursementStatus.APPROVED);
				break;
			}
			case "/manager/denied": {
				allReimbursement = fmService.viewAllReimbursementsByStatus(ReimbursementStatus.DENIED);
				break;
			}
			default:
				res.setStatus(400);
				res.getWriter().write("Method Not Supported");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new InternalErrorException();
		}
		res.setStatus(200);
//		res.addHeader("Content-Type", "application/json");
		if (allReimbursement == null) {
			throw new InternalErrorException();
		}
		res.getWriter().write(om.writeValueAsString(allReimbursement));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
		HttpSession sess = authenticate(req, res);
		
		Reimbursement r = null;
		try {
			String test = req.getReader().readLine();
			System.out.println("id is: " + test);
			test = test.split("=")[1];
			int reimID = Integer.parseInt(test.replaceAll("'", ""));
			String URI = req.getRequestURI().substring(req.getContextPath().length(), req.getRequestURI().length());
			switch (URI) {
			case "/manager/approvereq": {
				r = fmService.approveReimbursement(reimID, (User)sess.getAttribute("user"));
				break;
			}
			case "/manager/denyreq": {
				r = fmService.denyReimbursement(reimID, (User)sess.getAttribute("user"));
				break;
			}
			default:{
				res.setStatus(400);
				res.getWriter().write("Method Not Supported");
			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		res.setStatus(200);
		res.getWriter().write(om.writeValueAsString(r));
		}
		catch (AbstractHttpException e) {
			res.setStatus(e.getStatusCode());
			res.getWriter().write(e.getMessage());
		}
	}

}
