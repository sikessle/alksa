package de.alksa.parser.token;


public interface ElementalToken {
	
	<T extends Object> T getValue();
}
