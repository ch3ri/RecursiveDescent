package project1;

// File Name: TokenList.java

// Date: 2/3/2020
// Author: Cheri Longo
// Purpose: This is an enum type that defines all the tokens to be used
// in recursive decent parsing of given grammar/example.

public enum TokenList {
	GROUP, // Represents a group of GUI elements .i.e. RadioButtonGroup
	END, // marks the end of program
	STRING, // a string
	COLON, // Symbol ':'
	RADIO, // Radio Button
	FLOW, // Flow Layout
	WINDOW, // Top level Window
	NUMBER, // Integer
	LAYOUT, // Layout
	SEMI_COLON, // Symbol ';'
	TEXTFIELD, // Represents a Text Field
	BUTTON, // Represents a Button
	PANEL, // Represents a Panel
	PERIOD, // Symbol '.'
	COMMA, // Symbol ','
	RIGHT_BRACE, // Symbol ')'
	LEFT_BRACE, // Symbol '('
	EOF, // End of file
	LABEL, // Represents a label
	GRID // Represents a Grid layout
};