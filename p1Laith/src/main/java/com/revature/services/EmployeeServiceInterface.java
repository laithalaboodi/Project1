package com.revature.services;

import java.sql.SQLException;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.models.User;

public interface EmployeeServiceInterface extends UserServiceInterface {
	
	////////////////////Employee services///////////
	List<Reimbursement> viewPastTickets(User user) throws SQLException;

	Reimbursement addReimbursementRequest(User user, Reimbursement reimbursement) throws SQLException;

}
