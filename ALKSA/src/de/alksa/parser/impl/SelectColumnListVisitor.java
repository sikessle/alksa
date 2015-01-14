package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.Token;

/**
 * Processes everything in the SelectColumnList: SELECT [FUNC(col1), col2,
 * col3*25, etc.]
 */
public class SelectColumnListVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {

		processColumnList(node);

		// TODO add other types like Function, CAlculations, ..

		return node;
	}

	private void processColumnList(Visitable node) throws StandardException {
		if (!(node instanceof ResultColumn)) {
			return;
		}

		ResultColumn resultColumn = (ResultColumn) node;
		AbstractVisitor plainColumnVisitor = new ColumnNameFirstLevelVisitor();
		resultColumn.accept(plainColumnVisitor);
		
		for (Token t : plainColumnVisitor.getTokens()) {
			addToken(t);
		}
	}

}
