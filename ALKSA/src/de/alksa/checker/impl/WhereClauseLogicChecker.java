package de.alksa.checker.impl;

import java.util.HashSet;
import java.util.Set;

import de.alksa.log.LogEntry;
import de.alksa.token.BinaryLogicalFilterToken;
import de.alksa.token.ColumnNameToken;
import de.alksa.token.FilterToken;
import de.alksa.token.LogicalFilterToken;
import de.alksa.token.SelectStatementToken;
import de.alksa.token.Token;
import de.alksa.token.UnaryLogicalFilterToken;
import de.alksa.util.TypeUtil;

public class WhereClauseLogicChecker extends WhereClauseChecker {

	@Override
	protected LogEntry checkWhereClause(Set<FilterToken> subject,
			Set<FilterToken> learned) {

		if (subject.isEmpty()) {
			return null;
		}

		LogicalFilterToken learnedLogic = TypeUtil.getFirstTokenOfType(learned,
				LogicalFilterToken.class);
		LogicalFilterToken subjectLogic = TypeUtil.getFirstTokenOfType(subject,
				LogicalFilterToken.class);

		if (learnedLogic == null) {
			if (usesOnlyLegalColumns(subjectLogic)) {
				return null;
			} else {
				return createLogEntry("illegal columns used in logic operation");
			}
		} else {
			if (learnedLogic.equals(subjectLogic)) {
				return null;
			}
		}

		if (learnedLogic instanceof UnaryLogicalFilterToken) {
			if (!learnedLogic.equals(subjectLogic)) {
				return createLogEntry("NOT does not match");
			}
		} else if (learnedLogic instanceof BinaryLogicalFilterToken) {
			BinaryLogicalFilterToken learnedBinary = (BinaryLogicalFilterToken) learnedLogic;
			if (isIllegalBinaryLogic(subjectLogic, learnedBinary)) {
				return createLogEntry("illegal modification of binary logic operation");
			}
		}

		return null;
	}

	private boolean isIllegalBinaryLogic(LogicalFilterToken subject,
			BinaryLogicalFilterToken learned) {
		if (!(subject instanceof BinaryLogicalFilterToken)) {
			// learned: WHERE x=2 OR x=4
			// subject: WHERE x=2
			// this is allowed.
			if (learned.getType().equals(BinaryLogicalFilterToken.Type.OR)) {
				return false;
			}
			return true;
		}

		BinaryLogicalFilterToken subjectBinary = (BinaryLogicalFilterToken) subject;

		if (isIllegalANDLogic(subjectBinary, learned)
				|| isIllegalORLogic(subjectBinary, learned)) {
			return true;
		}

		return false;
	}

	private boolean isIllegalANDLogic(BinaryLogicalFilterToken subject,
			BinaryLogicalFilterToken learned) {

		if (!subject.getType().equals(learned.getType())) {
			return true;
		}
		if (!subject.getType().equals(BinaryLogicalFilterToken.Type.AND)) {
			return false;
		}

		Token partToCheck;

		if (!subject.getfirstOperand().equals(learned.getfirstOperand())) {
			partToCheck = subject.getfirstOperand();
		} else if (!subject.getSecondOperand().equals(
				learned.getSecondOperand())) {
			partToCheck = subject.getSecondOperand();
		} else {
			return false;
		}

		return !usesOnlyLegalColumns(partToCheck);
	}

	private boolean isIllegalORLogic(BinaryLogicalFilterToken subject,
			BinaryLogicalFilterToken learned) {

		if (!subject.getType().equals(learned.getType())) {
			return true;
		}
		if (!subject.getType().equals(BinaryLogicalFilterToken.Type.OR)) {
			return false;
		}

		return false;
	}

	private boolean usesOnlyLegalColumns(Token token) {
		Set<Token> tokenParts = new HashSet<>();

		tokenParts.addAll(copyRecursiveTokensOfType(token,
				ColumnNameToken.class));
		tokenParts.addAll(copyRecursiveTokensOfType(token,
				SelectStatementToken.class));

		return legalColumnsForNewFilters.containsAll(tokenParts);
	}

}
