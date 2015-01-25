package de.alksa.token;

import java.util.Arrays;
import java.util.HashSet;

public class UnaryLogicalFilterToken extends LogicalToken {

	public enum Type {
		NOT
	}

	private Type type;
	private Token parameter;

	public UnaryLogicalFilterToken(Type type, Token parameter) {
		this.type = type;
		this.parameter = parameter;
		setTokens(new HashSet<>(Arrays.asList(parameter)));
	}

	public Type getType() {
		return type;
	}

	public Token getParameter() {
		return parameter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((parameter == null) ? 0 : parameter.hashCode());
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
		UnaryLogicalFilterToken other = (UnaryLogicalFilterToken) obj;
		if (parameter == null) {
			if (other.parameter != null) {
				return false;
			}
		} else if (!parameter.equals(other.parameter)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return type + "(" + parameter + ")";
	}

}
