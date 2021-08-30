package com.revature.services;

import java.sql.SQLException;

import com.revature.models.User;


public interface UserServiceInterface {
	
	User login(String username, String password) throws SQLException;
	
}

