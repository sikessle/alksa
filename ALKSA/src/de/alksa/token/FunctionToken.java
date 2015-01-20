package de.alksa.token;

import java.util.List;

public class FunctionToken extends HierarchyToken {

	private String operator;

	public FunctionToken(String operator, List<? extends Token> parameters) {
		setTokens(parameters);
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
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
		FunctionToken other = (FunctionToken) obj;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return operator + super.toString();
	}

}
