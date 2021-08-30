package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private static ConnectionFactory cf = new ConnectionFactory(1);
	
	public static ConnectionFactory getConnectionFactory() {
		return cf;
	}
	
	private Connection[] conns;
	
	private ConnectionFactory(int numberOfConnections) {
		try {
			DriverManager.registerDriver(new org.postgresql.Driver());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		try {
			this.conns = new Connection[numberOfConnections];
			for(int i =0; i< this.conns.length; i++) {
				Connection c = DriverManager.getConnection("jdbc:postgresql://java-uta-2021.cgwkgluikw7p.us-west-1.rds.amazonaws.com/mydb", "socialuser", "password");
				this.conns[i]  = c;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return this.conns[0];
	}
	
	public void closeConnection() {}
}
