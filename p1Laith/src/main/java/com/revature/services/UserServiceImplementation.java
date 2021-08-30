package com.revature.services;

import java.sql.SQLException;

import com.revature.models.User;
import com.revature.repositories.UserDAO;
import com.revature.repositories.UserPostgresDAO;

public class UserServiceImplementation implements UserServiceInterface {
	
	private static UserDAO uDAO = new UserPostgresDAO();

	@Override
	public User login(String username, String password) throws SQLException {
		return uDAO.getUser(username, password);		
	}

}
