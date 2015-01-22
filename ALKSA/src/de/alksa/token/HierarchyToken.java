package de.alksa.token;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class HierarchyToken extends Token {

	private Set<? extends Token> tokens;

	protected void setTokens(Set<? extends Token> tokens) {
		this.tokens = tokens == null ? new HashSet<>() : tokens;
	}

	public Set<? extends Token> getChildren() {
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HierarchyToken other = (HierarchyToken) obj;
		if (tokens == null) {
			if (other.tokens != null) {
				return false;
			}
		} else if (!tokens.equals(other.tokens)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "("
				+ tokens.stream().map(Object::toString)
						.collect(Collectors.joining(",")) + ")";
	}

}
