package de.alksa.parser.impl;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.FromTable;
import com.foundationdb.sql.parser.Visitable;

import de.alksa.token.TableNameToken;

public class FromTableVisitor extends AbstractVisitor {

	@Override
	public Visitable visit(Visitable node) throws StandardException {
		if (node instanceof FromTable) {
			FromTable table = (FromTable) node;
			String tableName = table.getOrigTableName().getFullTableName();
			addToken(new TableNameToken(tableName));
		}
		return node;
	}

}
