package com.revature.exceptions;

public class UserNotFoundException extends AbstractHttpException {
	public UserNotFoundException() {
		super("User Not Found", 404);
	}
}
