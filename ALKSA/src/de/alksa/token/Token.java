package de.alksa.token;

public abstract class Token {

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
