package project1;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

// necessary import statements
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

// File Name: RDParser.java

// Date: 2/3/2020
// Author: Cheri Longo
// Purpose: This class represents the Recursive Decent RDParser
public class RDParser {

	// The lexical analyzer associated with the parser
	private Lexer theLexer;
	// this variable is used to store the upcoming expected token
	private TokenList expectedToken;
	// this variable is used to store the current token read
	private TokenList currentToken;
	// the top level window of the output gui
	private JFrame jFrame;
	// represents a group of buttons
	private ButtonGroup buttonGroup;
	// temporary string variable to hold lexeme
	private String tempString;

	// constructs an instance of RDParser from given
	// Lexer .i.e Lexical Analyzer
	RDParser(Lexer theLexer) {
		this.theLexer = theLexer;
	}

	// this is a wrapper around "parseGUI" method
	void startParsing() throws IOException, InvalidTokenException {
		currentToken = theLexer.getNextToken();
		if (isValidGui()) {
			jFrame.setVisible(true);
			jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		} else {
			String outputMessage = String.format("Line # %d : \'%s\' was expected but \'%s\' was found.",
					theLexer.getCurrentLineNumber(), expectedToken, currentToken);
			throw new InvalidTokenException(outputMessage);
		}
	}

	// a helper method to evaluate the current token against expectedToken
	private boolean isTokenSameAs(TokenList targetToken) throws IOException, InvalidTokenException {
		boolean flag = true;
		// check if current token matches target token
		if (currentToken == targetToken) {
			if (currentToken != TokenList.PERIOD && currentToken != TokenList.EOF && currentToken != TokenList.NUMBER) {
				// proceed to next token if current token is valid
				currentToken = theLexer.getNextToken();
				// get lexeme if current token is a string
				if (currentToken == TokenList.STRING) {
					tempString = theLexer.getLexemeForCurrentToken();
				}
				// create a button group if the current token represents GROUP
				else if (currentToken == TokenList.GROUP) {
					buttonGroup = new ButtonGroup();
				}
			}
		}
		// if current token does not matches target token
		// return false
		else {
			expectedToken = targetToken;
			flag = false;
		}
		return flag;
	}

	// this method parses the definition language token by token
	// and constructs the gui associated with it
	// Returns True if Gui was defined correctly otherwise returns false
	private boolean isValidGui() throws IOException, InvalidTokenException {
		boolean flag = false;
		// the first token should be WINDOW
		if (isTokenSameAs(TokenList.WINDOW)) {
			// after WINDOW, comes the name of the window
			if (isTokenSameAs(TokenList.STRING)) {
				// create jFrame
				jFrame = new JFrame();
				jFrame.setLocationRelativeTo(null);
				// create jpanel
				JPanel jPanel = new JPanel();
				jFrame.add(jPanel);
				jFrame.setTitle(tempString);
				// next token should be Left Bracket
				if (isTokenSameAs(TokenList.LEFT_BRACE)) {
					// next token is the width of the window
					if (isTokenSameAs(TokenList.NUMBER)) {
						// parse the width of the window
						int w = theLexer.getValueOfNumericToken();
						currentToken = theLexer.getNextToken();
						// make sure after width a comma should be present
						if (isTokenSameAs(TokenList.COMMA)) {
							// now comes the height of the window
							if (isTokenSameAs(TokenList.NUMBER)) {
								// parse height of the window
								int h = theLexer.getValueOfNumericToken();
								currentToken = theLexer.getNextToken();
								// close the bracket which was opened earlier
								if (isTokenSameAs(TokenList.RIGHT_BRACE)) {
									// set the size of jframe and jpanel
									jFrame.setSize(w, h);
									jPanel.setSize(w, h);
									// make sure that panel was defined correctly
									if (isValidLayout(jPanel)) {
										if (isValidWidget(jPanel)) {
											if (isTokenSameAs(TokenList.END)) {
												flag = isTokenSameAs(TokenList.PERIOD);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}

	// validate the definition of the given layout
	private boolean isValidLayout(Container container) throws IOException, InvalidTokenException {
		boolean flag = isTokenSameAs(TokenList.LAYOUT) && isLayoutOfValidType(container)
				&& isTokenSameAs(TokenList.COLON);
		return flag;
	}

	// make sure that the layout has only valid types .i.e. FLOW or GRID
	private boolean isLayoutOfValidType(Container container) throws IOException, InvalidTokenException {
		boolean flag = true;

		// set layout to FLOW if layout type is so
		if (isTokenSameAs(TokenList.FLOW)) {
			container.setLayout(new FlowLayout());
		}
		// if layout type is GRID then validate the
		// definition of GRID Layout
		else if (isTokenSameAs(TokenList.GRID)) {
			// opening bracket
			if (isTokenSameAs(TokenList.LEFT_BRACE)) {
				// number of rows to create in the GRID
				if (isTokenSameAs(TokenList.NUMBER)) {
					// parse the number of rows
					int rowCount = theLexer.getValueOfNumericToken();
					// move to next token
					currentToken = theLexer.getNextToken();
					// comma after row count
					if (isTokenSameAs(TokenList.COMMA)) {
						// number of columns to create in GRID
						if (isTokenSameAs(TokenList.NUMBER)) {
							// parse number of columns
							int colCount = theLexer.getValueOfNumericToken();
							// move to next token
							currentToken = theLexer.getNextToken();
							// check if bracket is closed or not
							if (isTokenSameAs(TokenList.RIGHT_BRACE)) {
								// set GridLayout with specified rows and Columns
								container.setLayout(new GridLayout(rowCount, colCount));
							}
							// if bracket was not closed and there was a comma then
							else if (isTokenSameAs(TokenList.COMMA)) {
								// now we have horizontal gap to specify in GRID
								if (isTokenSameAs(TokenList.NUMBER)) {
									// parse hGap
									int hGap = theLexer.getValueOfNumericToken();
									// move to next token
									currentToken = theLexer.getNextToken();
									// comma after hgap
									if (isTokenSameAs(TokenList.COMMA)) {
										// this number is vertical gap to keep in between GRID elements
										if (isTokenSameAs(TokenList.NUMBER)) {
											// parse vGap
											int vGap = theLexer.getValueOfNumericToken();
											// move to next token
											currentToken = theLexer.getNextToken();
											// close brackets
											if (isTokenSameAs(TokenList.RIGHT_BRACE)) {
												// set grid layout with specified rows, columns, horizontal and vertical
												// gap
												container.setLayout(new GridLayout(rowCount, colCount, hGap, vGap));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}

	// validate the widget definition
	private boolean isValidWidget(Container container) throws IOException, InvalidTokenException {
		boolean flag = true;
		if (isWidgetOfValidType(container)) {
			isValidWidget(container);
		} else {
			flag = false;
		}
		return flag;
	}

	// validate the type of widget definition
	private boolean isWidgetOfValidType(Container container) throws IOException, InvalidTokenException {
		boolean flag = false;
		// if widget is a button
		if (isTokenSameAs(TokenList.BUTTON)) {
			// parse the text to display in button
			if (isTokenSameAs(TokenList.STRING) && isTokenSameAs(TokenList.SEMI_COLON)) {
				// set the text of the button
				container.add(new JButton(tempString));
				flag = true;
			}
		}
		// if the widget is a group of Radio Buttons
		else if (isTokenSameAs(TokenList.GROUP)) {
			// parse the Radio buttons
			if (isValidRadioButtonGroup(container)) {
				flag = isTokenSameAs(TokenList.END) && isTokenSameAs(TokenList.SEMI_COLON);
			}
		}
		// if the widget is a Label
		else if (isTokenSameAs(TokenList.LABEL)) {
			// parse and set the text of the label
			if (isTokenSameAs(TokenList.STRING) && isTokenSameAs(TokenList.SEMI_COLON)) {
				container.add(new JLabel(tempString));
				flag = true;
			}
		}
		// if the widget is a Panel
		else if (isTokenSameAs(TokenList.PANEL)) {
			// add the panel to the gui and validate it
			container.add(container = new JPanel());
			if (isValidLayout(container)) {
				if (isValidWidget(container)) {
					flag = isTokenSameAs(TokenList.END) && isTokenSameAs(TokenList.SEMI_COLON);
				}
			}
		}
		// if the widget is a text field
		else if (isTokenSameAs(TokenList.TEXTFIELD)) {
			// parse and set the column width of the TextField
			if (isTokenSameAs(TokenList.NUMBER)) {
				int width = theLexer.getValueOfNumericToken();
				currentToken = theLexer.getNextToken();
				if (isTokenSameAs(TokenList.SEMI_COLON)) {
					container.add(new JTextField(width));
					flag = true;
				}
			}
		}
		return flag;
	}

	// validate the definition of Group of RadioButtons
	private boolean isValidRadioButtonGroup(Container container) throws IOException, InvalidTokenException {
		boolean flag = true;
		// check if definition of Radio Button is valid
		if (isValidRadioButton(container)) {
			isValidRadioButtonGroup(container);
		} else {
			flag = false;
		}
		return flag;
	}

	// validate the definition of the RadioButton
	private boolean isValidRadioButton(Container container) throws IOException, InvalidTokenException {
		boolean flag = false;
		// Parse definition; if the token represents a RadioButton otherwise return
		// false
		if (isTokenSameAs(TokenList.RADIO)) {
			// parse the String to display in RadioButton
			if (isTokenSameAs(TokenList.STRING)) {
				// end of RadioButton's definition
				if (isTokenSameAs(TokenList.SEMI_COLON)) {
					// create the RadioButton and add it to the gui
					JRadioButton tempRB = new JRadioButton(tempString);
					container.add(tempRB);
					buttonGroup.add(tempRB);
					flag = true;
				}
			}
		}
		return flag;
	}
}
