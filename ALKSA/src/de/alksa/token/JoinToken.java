package de.alksa.token;

import java.util.List;

public class JoinToken extends HierarchyToken {

	private String joinType;
	private Token leftPart;
	private Token rightPart;

	public JoinToken(String joinType, Token leftPart, Token rightPart) {
		this.joinType = joinType;
		this.leftPart = leftPart;
		this.rightPart = rightPart;
	}

	public String getJoinType() {
		return joinType;
	}

	public Token getLeftPart() {
		return leftPart;
	}

	public Token getRightPart() {
		return rightPart;
	}
	
	@Override
	/**
	 * Returns an empty list.
	 */
	public List<? extends Token> getChildren() {
		return super.getChildren();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((joinType == null) ? 0 : joinType.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JoinToken other = (JoinToken) obj;
		if (joinType == null) {
			if (other.joinType != null)
				return false;
		} else if (!joinType.equals(other.joinType))
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
		return "(" + leftPart.toString() + " " + joinType + " "
				+ rightPart.toString() + ")";
	}

}
