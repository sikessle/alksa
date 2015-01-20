# Parser

## SELECT COLUMN_LIST

* Column names
* Unary functions (AVG, ABS, etc.)
* Asterisk *
* Simple Calculations as Strings: "column \* 12", "ABS(col1) \* 8"

## FROM TABLE_LIST

* Table names
* Multiple Joins: Inner, Full Outer, Left Outer, Right Outer, Natural

## WHERE Clause

* ON clause from Join-Statement 
* Logic AND/OR/NOT
* Comparison < > = <= >=

## HAVING Clause

* Same as WHERE Clause

## Set Operations

* UNION: Each SELECT statement is separately parsed

## Subqueries

* Each SELECT is parsed separately   >>>> TODO >>>>>