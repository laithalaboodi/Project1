package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.enums.ReimbursementStatus;
import com.revature.enums.ReimbursementType;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

public class ReimbursementPostgresDAO implements ReimbursementDAO {

	private static ConnectionFactory cf = ConnectionFactory.getConnectionFactory();
	
	public List<Reimbursement> getAllReimbursements() throws SQLException {
		System.out.println("getting all reimburser");
		Connection conn = cf.getConnection();
		System.out.println("got conn");
		List<Reimbursement> reimbursementList = new ArrayList<>();
		String sql = "select reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, "
				+ "reimb_author, reimb_resolver, reimb_status, reimb_type, user_first_name, user_last_name "
				+ "from ers_reimbursement "
				+ "join ers_reimbursement_status using (reimb_status_id) "
				+ "join ers_reimbursement_type using (reimb_type_id) "
				+ "join ers_users on ers_users.ers_users_id = ers_reimbursement.reimb_author "
				+ "order by reimb_submitted "
				+ ";";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet res = statement.executeQuery();

		
		while (res.next()) {
			reimbursementList.add(new Reimbursement(res.getInt("reimb_id"), res.getDouble("reimb_amount"), 
					res.getTimestamp("reimb_submitted"), res.getTimestamp("reimb_resolved"), res.getString("reimb_description"), 
					res.getInt("reimb_author"), res.getInt("reimb_resolver"), ReimbursementStatus.valueOf(res.getString("reimb_status")), 
					ReimbursementType.valueOf(res.getString("reimb_type")), res.getString("user_first_name"), res.getString("user_last_name")));
		}
		if(reimbursementList.size() == 0) {
			throw new SQLException();
		}
		return reimbursementList;
	}

	public List<Reimbursement> getAllReimbursementsByStatus(ReimbursementStatus status) throws SQLException {
		Connection conn = cf.getConnection();
		List<Reimbursement> reimbursementList = new ArrayList<>();
		String sql = "select reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, "
				+ "reimb_author, reimb_resolver, reimb_status, reimb_type, user_first_name, user_last_name "
				+ "from ers_reimbursement "
				+ "join ers_reimbursement_status using (reimb_status_id) "
				+ "join ers_reimbursement_type using (reimb_type_id) "
				+ "join ers_users on ers_users.ers_users_id = ers_reimbursement.reimb_author "
				+ "where reimb_status like ? "
				+ "order by reimb_submitted "
				+ ";";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, status.toString());
		ResultSet res = statement.executeQuery();

		
		while (res.next()) {
			reimbursementList.add(new Reimbursement(res.getInt("reimb_id"), res.getDouble("reimb_amount"), 
					res.getTimestamp("reimb_submitted"), res.getTimestamp("reimb_resolved"), res.getString("reimb_description"), 
					res.getInt("reimb_author"), res.getInt("reimb_resolver"), ReimbursementStatus.valueOf(res.getString("reimb_status")), 
					ReimbursementType.valueOf(res.getString("reimb_type")), res.getString("user_first_name"), res.getString("user_last_name")));
		}
		return reimbursementList;
	}

	public List<Reimbursement> getAllReimbursementsByUser(User user) throws SQLException {
		Connection conn = cf.getConnection();
		List<Reimbursement> reimbursementList = new ArrayList<>();
		String sql = "select reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, "
				+ "reimb_author, reimb_resolver, reimb_status, reimb_type, user_first_name, user_last_name "
				+ "from ers_reimbursement "
				+ "join ers_reimbursement_status using (reimb_status_id) "
				+ "join ers_reimbursement_type using (reimb_type_id) "
				+ "join ers_users on ers_users.ers_users_id = ers_reimbursement.reimb_author "
				+ "where reimb_author= ? "
				+ "order by reimb_submitted "
				+ ";";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, user.getUserId());
		ResultSet res = statement.executeQuery();

		
		while (res.next()) {
			reimbursementList.add(new Reimbursement(res.getInt("reimb_id"), res.getDouble("reimb_amount"), 
					res.getTimestamp("reimb_submitted"), res.getTimestamp("reimb_resolved"), res.getString("reimb_description"), 
					res.getInt("reimb_author"), res.getInt("reimb_resolver"), ReimbursementStatus.valueOf(res.getString("reimb_status")), 
					ReimbursementType.valueOf(res.getString("reimb_type")), res.getString("user_first_name"), res.getString("user_last_name")));
		}
		return reimbursementList;
	}

	public Reimbursement updateReimbursementStatus(Reimbursement reimbursement) throws SQLException {
		Connection conn = cf.getConnection();

		String sql = "update ers_reimbursement "
				+ "set	"
				+ "	reimb_amount = ?, "
				+ "	reimb_submitted = ?, "
				+ "	reimb_resolved = ?, "
				+ "	reimb_description = ?, "
				+ "	reimb_author = ?, "
				+ "	reimb_resolver = ?, "
				+ "	reimb_status_id = (select reimb_status_id from ers_reimbursement_status where reimb_status like ?), "
				+ "	reimb_type_id =(select reimb_type_id from ers_reimbursement_type where reimb_type like ?) "
				+ "where reimb_id=?;";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setDouble(1, reimbursement.getAmount());
		statement.setTimestamp(2, reimbursement.getSubmitted());
		statement.setTimestamp(3, reimbursement.getResolved());
		statement.setString(4, reimbursement.getDescription());
		statement.setInt(5, reimbursement.getAuthorID());
		if(reimbursement.getResolverID() == null) {
			statement.setNull(6, Types.BIGINT);
		}else {
			statement.setInt(6, reimbursement.getResolverID());
		}
		statement.setString(7, reimbursement.getStatus().toString());
		statement.setString(8, reimbursement.getType().toString());
		statement.setInt(9, reimbursement.getId());
		
		statement.executeUpdate();
		
		return reimbursement;
	}

	public Reimbursement addReimbursement(Reimbursement reimbursement) throws SQLException {
		Connection conn = cf.getConnection();

		String sql = "insert into ers_reimbursement "
				+ "	values(default, ?, ?, ?, ?, ?, ?, "
				+ "(select reimb_status_id from ers_reimbursement_status where reimb_status like ?), "
				+ "(select reimb_type_id from ers_reimbursement_type where reimb_type like ?))"
				+ "returning reimb_id;";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setDouble(1, reimbursement.getAmount());
		statement.setTimestamp(2, reimbursement.getSubmitted());
		statement.setTimestamp(3, reimbursement.getResolved());
		statement.setString(4, reimbursement.getDescription());
		statement.setInt(5, reimbursement.getAuthorID());
		if(reimbursement.getResolverID() == null) {
			statement.setNull(6, Types.BIGINT);
		}else {
			statement.setInt(6, reimbursement.getResolverID());
		}
		statement.setString(7, reimbursement.getStatus().toString());
		statement.setString(8, reimbursement.getType().toString());
		
		ResultSet rs = statement.executeQuery();
		rs.next();
		reimbursement.setId(rs.getInt("reimb_id"));

		return reimbursement;
	}

	@Override
	public Reimbursement getAllReimbursementsByID(int reimID) throws SQLException {
		Connection conn = cf.getConnection();
		String sql = "select reimb_id, reimb_amount, reimb_submitted, reimb_resolved, reimb_description, "
				+ "reimb_author, reimb_resolver, reimb_status, reimb_type, user_first_name, user_last_name "
				+ "from ers_reimbursement "
				+ "join ers_reimbursement_status using (reimb_status_id) "
				+ "join ers_reimbursement_type using (reimb_type_id) "
				+ "join ers_users on ers_users.ers_users_id = ers_reimbursement.reimb_author "
				+ "where reimb_id=? "
				+ "order by reimb_submitted "
				+ ";";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, reimID);
		ResultSet res = statement.executeQuery();

		if(!res.next()) {
			throw new SQLException();	
		}
		
		Reimbursement reimbursement = new Reimbursement(res.getInt("reimb_id"), res.getDouble("reimb_amount"), 
					res.getTimestamp("reimb_submitted"), res.getTimestamp("reimb_resolved"), res.getString("reimb_description"), 
					res.getInt("reimb_author"), res.getInt("reimb_resolver"), ReimbursementStatus.valueOf(res.getString("reimb_status")), 
					ReimbursementType.valueOf(res.getString("reimb_type")), res.getString("user_first_name"), res.getString("user_last_name"));
		
		return reimbursement;
	}

}
