package de.alksa.token;

public class JoinToken extends Token {

	public enum Type {
		NATURAL, FULL_OUTER, LEFT_OUTER, RIGHT_OUTER, INNER;

		@Override
		public String toString() {
			// "FULL_OUTER" should be output as "FULL OUTER", to allow easy
			// usage in query strings.
			return name().replace('_', ' ');
		}
	}

	private Type joinType;
	private Token leftPart;
	private Token rightPart;
	private FilterToken onClause;

	public JoinToken(Token leftPart, Type joinType, Token rightPart) {
		this.joinType = joinType;
		this.leftPart = leftPart;
		this.rightPart = rightPart;
	}

	public FilterToken getOnClause() {
		return onClause;
	}

	public void setOnClause(FilterToken onClause) {
		this.onClause = onClause;
	}

	public boolean hasOnClause() {
		return onClause != null;
	}

	public Type getJoinType() {
		return joinType;
	}

	public Token getLeftPart() {
		return leftPart;
	}

	public Token getRightPart() {
		return rightPart;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((joinType == null) ? 0 : joinType.hashCode());
		result = prime * result
				+ ((leftPart == null) ? 0 : leftPart.hashCode());
		result = prime * result
				+ ((onClause == null) ? 0 : onClause.hashCode());
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
		JoinToken other = (JoinToken) obj;
		if (joinType != other.joinType)
			return false;
		if (leftPart == null) {
			if (other.leftPart != null)
				return false;
		} else if (!leftPart.equals(other.leftPart))
			return false;
		if (onClause == null) {
			if (other.onClause != null)
				return false;
		} else if (!onClause.equals(other.onClause))
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
		return "(" + leftPart + " " + joinType + " " + rightPart + " [ON : "
				+ onClause + "])";
	}

}
