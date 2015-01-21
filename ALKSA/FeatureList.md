# Parser - Implemented Features

## SELECT column list

* Column names
* Unary functions (AVG, ABS, etc.)
* Asterisk *
* Simple Calculations as Strings: "column \* 12", "ABS(col1) \* 8"

## FROM table list

* Table names
* Multiple Joins: Inner, Full Outer, Left Outer, Right Outer, Natural

## WHERE clause

* ON clause from Join-Statement 
* Logic AND/OR/NOT
* Comparison < > = <= >=

## HAVING clause

* Same as WHERE Clause

## Set operations

* UNION: Each SELECT statement is separately parsed

## Subqueries

* In column list, table list and where clause: each SELECT is parsed separately

# Classifier - Implemented Features

## Set operations

* UNION: Checks each SELECT statement

## Subqueries

* Classified as part of each statement, i.e. in SELECT <subquery> FROM .. the subquery is treated as a column. 

