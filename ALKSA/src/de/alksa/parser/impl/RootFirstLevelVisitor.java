package de.alksa.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.SelectColumnListToken;
import de.alksa.token.Token;

/**
 * Processes everything. Can be applied multiple times to different "levels"
 */
public class RootFirstLevelVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		
		visitSelectList(node);
		// TODO process FROM, JOIN, WHERE, ORDER BY statements

		return node;
	}
	
	private void visitSelectList(Visitable node) throws StandardException {
		if (!(node instanceof ResultColumn)) {
			return;
		}
		
		ResultColumn resultColumn = (ResultColumn) node;
		List<Token> columnTokens = new ArrayList<>();
		
		columnTokens.addAll(visitColumnNames(resultColumn));
		columnTokens.addAll(visitFunctions(resultColumn));
		// TODO add other types like CAlculations, Mixed (nested), ..
		
		addToken(new SelectColumnListToken(columnTokens));
	}

	private List<? extends Token> visitColumnNames(Visitable node)
			throws StandardException {
		AbstractVisitor plainColumnVisitor = new ColumnNameFirstLevelVisitor();
		node.accept(plainColumnVisitor);

		return plainColumnVisitor.getTokens();
	}

	private List<? extends Token> visitFunctions(Visitable node)
			throws StandardException {
		AbstractVisitor functionColumnVisitor = new FunctionFirstLevelVisitor();
		node.accept(functionColumnVisitor);

		return functionColumnVisitor.getTokens();
	}

}
