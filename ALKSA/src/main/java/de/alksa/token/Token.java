package de.alksa.token;

public abstract class Token {

	@Override
	public abstract String toString();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object other);

}
