package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.AllResultColumn;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.ColumnNameToken;

/**
 * Extracts only plain column names: SELECT col1, col2 as c2 FROM .. WHERE col4
 * = .., etc. But NOT SELECT ABS(col3), etc.. Collects only on the first level,
 * not nested columns (like in ABS(col))
 *
 */
class ColumnNameVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof ColumnReference) {
			ColumnReference ref = (ColumnReference) node;
			addToken(new ColumnNameToken(ref.getSQLColumnName()));
		} else if (node instanceof AllResultColumn) {
			addToken(new ColumnNameToken("*"));
		}
		return node;
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		if (node instanceof ResultColumn) {
			return false;
		}
		if (!(node instanceof ColumnReference)) {
			return true;
		}
		return false;
	}
}