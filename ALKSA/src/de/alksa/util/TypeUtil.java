package de.alksa.util;

import java.util.HashSet;
import java.util.Set;

import de.alksa.token.Token;

public class TypeUtil {

	private TypeUtil() {
	}

	@SuppressWarnings("unchecked")
	public static <T extends Token> T getFirstTokenOfType(
			Set<? extends Token> tokens, Class<T> clazz) {

		for (Token token : tokens) {
			if (clazz.isInstance(token)) {
				return (T) token;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Token> Set<T> getAllTokensOfType(
			Set<? extends Token> tokens, Class<T> clazz) {

		Set<T> matchingTokens = new HashSet<>();

		for (Token token : tokens) {
			if (clazz.isInstance(token)) {
				matchingTokens.add((T) token);
			}
		}

		return matchingTokens;
	}
}
