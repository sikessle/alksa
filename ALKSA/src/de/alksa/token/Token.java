package de.alksa.token;

public abstract class Token {

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract boolean equals(Object other);
	
}
