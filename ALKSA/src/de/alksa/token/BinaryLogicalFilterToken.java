package de.alksa.token;

import java.util.Objects;

public class BinaryLogicalFilterToken extends FilterToken {

	public enum Type {
		AND, OR
	}

	private Type type;
	private Token firstOperand;
	private Token secondOperand;

	public BinaryLogicalFilterToken(Type type, Token firstOperand,
			Token secondOperand) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(firstOperand);
		Objects.requireNonNull(secondOperand);
		this.type = type;
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
	}

	public Token getfirstOperand() {
		return firstOperand;
	}

	public Type getType() {
		return type;
	}

	public Token getSecondOperand() {
		return secondOperand;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstOperand == null) ? 0 : firstOperand.hashCode());
		result = prime * result
				+ ((secondOperand == null) ? 0 : secondOperand.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * The order of first and second operand does not matter.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinaryLogicalFilterToken other = (BinaryLogicalFilterToken) obj;
		if (type != other.type)
			return false;

		return (firstOperand.equals(other.firstOperand) && secondOperand
				.equals(other.secondOperand))
				|| (firstOperand.equals(other.secondOperand) && secondOperand
						.equals(other.firstOperand));
	}

	@Override
	public String toString() {
		return firstOperand + " " + type + " " + secondOperand;
	}

}
