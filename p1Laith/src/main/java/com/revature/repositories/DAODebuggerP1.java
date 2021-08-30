package com.revature.repositories;

import java.sql.Timestamp;

import com.revature.enums.ReimbursementStatus;
import com.revature.enums.ReimbursementType;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;


public class DAODebuggerP1 {
	
	static ConnectionFactory cf = ConnectionFactory.getConnectionFactory();
	
	
	public static void main(String[] args) {
		UserDAO userDAO = new UserPostgresDAO();
		ReimbursementDAO reimDAO = new ReimbursementPostgresDAO();
//		Reimbursement r = new Reimbursement(1, 300.59, new Timestamp(System.currentTimeMillis()), null, "debug reimbursement", 1, null, 
//				ReimbursementStatus.PENDING, ReimbursementType.OTHER);
		
		try {
			//User u =userDAO.getUser("laith", "123");
//			System.out.println(u);
//			System.out.println(reimDAO.getAllReimbursements());
//			System.out.println(reimDAO.getAllReimbursementsByStatus(ReimbursementStatus.PENDING));
//			System.out.println(reimDAO.addReimbursement(r));
//			System.out.println(reimDAO.updateReimbursementStatus(r));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
