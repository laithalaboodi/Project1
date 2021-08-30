package com.revature.services;

import java.sql.SQLException;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;
import com.revature.repositories.ReimbursementPostgresDAO;
import com.revature.repositories.UserDAO;
import com.revature.repositories.UserPostgresDAO;

public class EmployeeServiceImplementation implements EmployeeServiceInterface{

	static private UserDAO uDAO = new UserPostgresDAO();
	static private ReimbursementDAO reimDAO = new ReimbursementPostgresDAO();
	
	@Override
	public User login(String username, String password) throws SQLException {
		return uDAO.getUser(username, password);		
	}

	@Override
	public List<Reimbursement> viewPastTickets(User user) throws SQLException {
		return reimDAO.getAllReimbursementsByUser(user);
	}

	@Override
	public Reimbursement addReimbursementRequest(User user, Reimbursement reimbursement) throws SQLException {
		if(user.getUserId() != reimbursement.getAuthorID()) {throw new SQLException();}
		return reimDAO.addReimbursement(reimbursement);
	}

}
