package de.alksa.token;

public class ComparisonFilterToken extends FilterToken {

	public enum Type {
		EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL
	}

	private Token leftPart;
	private Type comparisonType;
	private Token rightPart;

	public ComparisonFilterToken(Token leftPart, Type comparisonType,
			Token rightPart) {

		this.leftPart = leftPart;
		this.comparisonType = comparisonType;
		this.rightPart = rightPart;
	}

	public Token getLeftPart() {
		return leftPart;
	}

	public Type getComparisonType() {
		return comparisonType;
	}

	public Token getRightPart() {
		return rightPart;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comparisonType == null) ? 0 : comparisonType.hashCode());
		result = prime * result
				+ ((leftPart == null) ? 0 : leftPart.hashCode());
		result = prime * result
				+ ((rightPart == null) ? 0 : rightPart.hashCode());
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
		ComparisonFilterToken other = (ComparisonFilterToken) obj;
		if (comparisonType != other.comparisonType)
			return false;
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
		return true;
	}

	@Override
	public String toString() {
		return "(" + leftPart + " " + comparisonType + " " + rightPart + ")";
	}

}
