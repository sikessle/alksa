package de.alksa.token;

public class BinaryLogicalFilterToken extends FilterToken {

	public enum Type {
		AND, OR
	}

	private Token leftPart;
	private Type type;
	private Token rightPart;

	public BinaryLogicalFilterToken(Token leftPart, Type type, Token rightPart) {
		this.leftPart = leftPart;
		this.type = type;
		this.rightPart = rightPart;
	}

	public Token getLeftPart() {
		return leftPart;
	}

	public Type getType() {
		return type;
	}

	public Token getRightPart() {
		return rightPart;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((leftPart == null) ? 0 : leftPart.hashCode());
		result = prime * result
				+ ((rightPart == null) ? 0 : rightPart.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinaryLogicalFilterToken other = (BinaryLogicalFilterToken) obj;
		if (leftPart == null) {
			if (other.leftPart != null)
				return false;
		} else if (!leftPart.equals(other.leftPart))
			return false;
		if (rightPart == null) {
			if (other.rightPart != null)
				return false;
		} else if (!rightPart.equals(other.rightPart))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return leftPart + " " + type + " " + rightPart;
	}

}
