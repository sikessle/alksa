package de.alksa.token;

import java.util.ArrayList;
import java.util.List;

public abstract class HierarchyToken extends Token {

	private List<? extends Token> tokens;

	protected void setTokens(List<? extends Token> tokens) {
		this.tokens = tokens == null ? new ArrayList<>() : tokens;
	}

	public List<? extends Token> getChildren() {
		return tokens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
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
		HierarchyToken other = (HierarchyToken) obj;
		if (tokens == null) {
			if (other.tokens != null)
				return false;
		} else if (!tokens.equals(other.tokens))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("(");
		for (Token t : tokens) {
			sb.append(t.toString());
			sb.append(", ");
		}
		sb.append(")");

		return sb.toString();
	}

}
