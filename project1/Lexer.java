package project1;

// necessary import statements
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

// File Name: Lexer.java

// Date: 2/3/2020
// Author: Cheri Longo
// Purpose: This class represents the lexical analyzer to parse the given example
public class Lexer {

	private StreamTokenizer streamTokenizer;
	private final String PUNCTUATION_TEXT = ",:;.()";
	private TokenList[] punctuationMarks = { TokenList.COMMA, TokenList.COLON, TokenList.SEMI_COLON, TokenList.PERIOD,
			TokenList.LEFT_BRACE, TokenList.RIGHT_BRACE };

	// A constructor to create the Lexical Analyzer From the given file path
	public Lexer(String filePath) throws FileNotFoundException {
		streamTokenizer = new StreamTokenizer(new FileReader(filePath));
		// set the ordinary Char for the stream tokenizer
		streamTokenizer.ordinaryChar('.');
		// set the quote char for the stream tokenizer
		streamTokenizer.quoteChar('"');
	}

	// This method returns the line number of file which is currently being
	// read
	public int getCurrentLineNumber() {
		return streamTokenizer.lineno();
	}

	// For current token, this method will return the lexeme associated with it
	public String getLexemeForCurrentToken() {
		return streamTokenizer.sval;
	}

	// This method gets and return the next token in the input stream
	public TokenList getNextToken() throws InvalidTokenException, IOException {
		// save next token
		int currentToken = streamTokenizer.nextToken();

		// check if the current token
		// is a number or not, if not then keep
		// checking for next category
		if (currentToken != StreamTokenizer.TT_NUMBER) {
			// check if the current token
			// is a word or not, if not then keep
			// checking for next category
			if (currentToken != StreamTokenizer.TT_WORD) {
				// check if the current token
				// is an END_OF_FILE character or not,
				// if not then keep
				// checking for next category
				if (currentToken != StreamTokenizer.TT_EOF) {
					// check if the current token
					// is a Quote Char or not, if not then keep
					// checking for next category
					if (currentToken != '"') {
						// The current token is not a word, number,
						// EOF or a quote Char. It can be a punctuation mark
						boolean punctuationFound = false;
						int index = 0;
						// checking if the current token is a punctuation mark
						while (!punctuationFound && index < PUNCTUATION_TEXT.length()) {
							if (currentToken == PUNCTUATION_TEXT.charAt(index)) {
								punctuationFound = true;
							}
							if (!punctuationFound) {
								index++;
							}
						}
						// return punctuation mark if the current token is
						if (punctuationFound) {
							return punctuationMarks[index];
						}

					} else {
						// the token was a quote char
						return TokenList.STRING;

					}
				} else {
					// the token was an END_OF_FILE Character
					return TokenList.EOF;
				}
			} else {

				TokenList tokens[] = TokenList.values();
				boolean keywordFound = false;
				int index = 0;

				// the current token must be a keyword
				while (!keywordFound && index < tokens.length) {
					if (tokens[index].name().equalsIgnoreCase(streamTokenizer.sval)) {
						keywordFound = true;
					}
					if (!keywordFound) {
						index++;
					}
				}

				// if keyword is found return it
				if (keywordFound) {
					return tokens[index];
				}

				// if invalid token is found throw an InvalidTokenException
				String outputMessage = String.format("Invalid Token: \"%s\" Found at Line # %d",
						getLexemeForCurrentToken(), getCurrentLineNumber());
				throw new InvalidTokenException(outputMessage);
			}

		} else {
			// the current token was a number
			return TokenList.NUMBER;
		}
		// if nothing was found, then it is assumed that the end of file
		// has been reached
		return TokenList.EOF;
	}

	// For current token if it is a numeric token,
	// this method will return the numeric value associated with it
	public int getValueOfNumericToken() {
		return (int) streamTokenizer.nval;
	}
} // end of class Lexer
