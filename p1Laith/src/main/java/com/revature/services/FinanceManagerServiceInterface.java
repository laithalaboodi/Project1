package com.revature.services;

import java.sql.SQLException;
import java.util.List;

import com.revature.enums.ReimbursementStatus;
import com.revature.models.Reimbursement;
import com.revature.models.User;

public interface FinanceManagerServiceInterface extends UserServiceInterface {

	////////////////////Finance Manager services///////////
	List<Reimbursement> viewAllReimbursements() throws SQLException;

	List<Reimbursement> viewAllReimbursementsByStatus(ReimbursementStatus status) throws SQLException;

	Reimbursement approveReimbursement(Reimbursement reimbursement, User resolver) throws SQLException;
	
	Reimbursement approveReimbursement(int reimID, User resolver) throws SQLException;

	Reimbursement denyReimbursement(Reimbursement reimbursement, User resolver) throws SQLException;
	
	Reimbursement denyReimbursement(int reimID, User resolver) throws SQLException;

}
