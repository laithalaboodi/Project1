package com.revature.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.util.ConnectionFactory;
import com.revature.enums.UserRole;
import com.revature.models.User;

public class UserPostgresDAO implements UserDAO {
	
	private static ConnectionFactory cf = ConnectionFactory.getConnectionFactory(); 

	public User getUser(String username, String password) throws SQLException {
		Connection conn = cf.getConnection();
		String sql = "select ers_users_id, ers_username, ers_password, user_first_name, user_last_name, user_email, user_role "
				+ "from ers_users "
				+ "join ers_user_roles "
				+ "using (user_role_id) "
				+ "where (ers_username like ? and ers_password like ?);";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, username);
		statement.setString(2, password);
		ResultSet res = statement.executeQuery();
		if(!res.next()) {
			throw new SQLException();
		}
		User u = new User(res.getInt("ers_users_id"), res.getString("ers_username"),res.getString("ers_password"), res.getString("user_first_name"),
				res.getString("user_last_name"),res.getString("user_email"), UserRole.valueOf(res.getString("user_role")));
		
		return u;
	}

}
