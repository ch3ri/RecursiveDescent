package project1;

// File Name: nvalidTokenException.java

// Date: 2/3/2020
// Author: Cheri Longo
// Purpose: User Defined InvalidTokenException Class which is thrown whenever 
// an invalid or unexpected token is encountered.
// The message displayed to the user is brief description about 
// error along with the line number
public class InvalidTokenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// This constructor Creates an InvalidTokenException Instance
	// and initializes it with the given message.
	public InvalidTokenException(String message) {
		super(message);
	}
}