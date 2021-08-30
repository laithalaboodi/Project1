package com.revature.repositories;

import java.sql.SQLException;

import com.revature.models.User;

public interface UserDAO {
	//user bcrypt

	User getUser(String username, String password) throws SQLException;
	
}
