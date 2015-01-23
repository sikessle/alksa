package de.alksa.token;

import java.util.Set;

public class SelectStatementToken extends Token {

	private SelectColumnListToken columnList;
	private FromListToken fromList;
	private WhereClauseToken whereClause;
	private HavingClauseToken havingClause;

	public Set<? extends Token> getColumnList() {
		return columnList.getChildren();
	}

	public void setColumnListToken(SelectColumnListToken columnList) {
		this.columnList = columnList;
	}

	public Set<? extends Token> getFromList() {
		return fromList.getChildren();
	}

	public void setFromListToken(FromListToken fromList) {
		this.fromList = fromList;
	}

	public Set<? extends Token> getWhereClause() {
		return whereClause.getChildren();
	}

	public void setWhereClauseToken(WhereClauseToken whereClause) {
		this.whereClause = whereClause;
	}

	public Set<? extends Token> getHavingClause() {
		return havingClause.getChildren();
	}

	public void setHavingClauseToken(HavingClauseToken havingClause) {
		this.havingClause = havingClause;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT [");
		if (columnList != null) {
			sb.append(columnList);
			sb.append(" ");
		}
		if (fromList != null) {
			sb.append("FROM ");
			sb.append(fromList);
			sb.append(" ");
		}
		if (whereClause != null) {
			sb.append("WHERE ");
			sb.append(whereClause);
			sb.append(" ");
		}
		if (havingClause != null) {
			sb.append("HAVING ");
			sb.append(havingClause);
		}
		sb.append("]");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnList == null) ? 0 : columnList.hashCode());
		result = prime * result
				+ ((fromList == null) ? 0 : fromList.hashCode());
		result = prime * result
				+ ((havingClause == null) ? 0 : havingClause.hashCode());
		result = prime * result
				+ ((whereClause == null) ? 0 : whereClause.hashCode());
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
		SelectStatementToken other = (SelectStatementToken) obj;
		if (columnList == null) {
			if (other.columnList != null) {
				return false;
			}
		} else if (!columnList.equals(other.columnList)) {
			return false;
		}
		if (fromList == null) {
			if (other.fromList != null) {
				return false;
			}
		} else if (!fromList.equals(other.fromList)) {
			return false;
		}
		if (havingClause == null) {
			if (other.havingClause != null) {
				return false;
			}
		} else if (!havingClause.equals(other.havingClause)) {
			return false;
		}
		if (whereClause == null) {
			if (other.whereClause != null) {
				return false;
			}
		} else if (!whereClause.equals(other.whereClause)) {
			return false;
		}
		return true;
	}

}
