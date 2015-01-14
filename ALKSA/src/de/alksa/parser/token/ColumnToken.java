package de.alksa.parser.token;

public class ColumnToken extends ElementalToken<String> {

	public ColumnToken(String columnName) {
		this.value = columnName;
	}

}
