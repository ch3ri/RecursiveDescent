package project1;

import java.io.FileNotFoundException;
import java.io.IOException;

// File Name: Main.java

// Date: 2/3/2020
// Author: Cheri Longo
// Purpose: This class represents the driver class of the application
public class Main {

	// main method
	public static void main(String[] args) {

		// currently, the test file is named as "test_file.txt"
		String pathToFile = "test_file.txt";

		try {
			new RDParser(new Lexer(pathToFile)).startParsing();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InvalidTokenException ex) {
			ex.printStackTrace();
		}

	}
}
