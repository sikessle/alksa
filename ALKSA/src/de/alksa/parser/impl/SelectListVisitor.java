package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.ColumnToken;

public class SelectListVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof ColumnReference) {
			ColumnReference col = (ColumnReference) node;
			addToken(new ColumnToken(col.getColumnName()));
		} 
		// TODO add other types
		
		return node;
	}

}
