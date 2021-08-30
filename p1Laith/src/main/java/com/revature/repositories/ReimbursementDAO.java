package com.revature.repositories;

import java.sql.SQLException;
import java.util.List;

import com.revature.enums.ReimbursementStatus;
import com.revature.models.Reimbursement;
import com.revature.models.User;

public interface ReimbursementDAO {
	
	List<Reimbursement> getAllReimbursements() throws SQLException;

	List<Reimbursement> getAllReimbursementsByStatus(ReimbursementStatus status) throws SQLException;
	
	List<Reimbursement> getAllReimbursementsByUser(User user) throws SQLException;

	Reimbursement updateReimbursementStatus(Reimbursement reimbursement) throws SQLException;
	
	Reimbursement addReimbursement(Reimbursement reimbursement) throws SQLException;

	Reimbursement getAllReimbursementsByID(int reimID) throws SQLException;;
	
	
}
