package de.alksa.token;

public class ColumnNameToken extends ElementalToken<String> {

	public ColumnNameToken(String columnName) {
		setValue(columnName.toLowerCase());
	}

}
