package de.alksa.token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class BinaryLogicalFilterToken extends LogicalToken {

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

		setTokens(new HashSet<>(Arrays.asList(firstOperand, secondOperand)));
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
		int result = super.hashCode();
		result = prime * result
				+ ((firstOperand == null) ? 0 : firstOperand.hashCode());
		result = prime * result
				+ ((secondOperand == null) ? 0 : secondOperand.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BinaryLogicalFilterToken other = (BinaryLogicalFilterToken) obj;
		if (firstOperand == null) {
			if (other.firstOperand != null) {
				return false;
			}
		} else if (!firstOperand.equals(other.firstOperand)) {
			return false;
		}
		if (secondOperand == null) {
			if (other.secondOperand != null) {
				return false;
			}
		} else if (!secondOperand.equals(other.secondOperand)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return firstOperand + " " + type + " " + secondOperand;
	}

}
