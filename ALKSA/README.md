# Prototype

This is a prototype implementation of the ALKSA security concept. It is mainly test-driven and uses a component-based approach with Google Guice.

## Build

The project is a maven project. For details of the dependencies look at the pom.xml file.

## Features

### General

#### Depth of Queries

* The depth is mainly limited by performance and the persistence layer (see activation depth of Db4o)

#### Learning/Productive Phase

* The learning or productive phase must be set manually.

### Parser - Implemented Features

#### Alias

* No support 

#### SELECT column list

* Column names
* Unary functions (AVG, ABS, etc.)
* Asterisk *
* Simple Calculations as Strings: "column \* 12", "ABS(col1) \* 8"

#### FROM table list

* Table names
* Multiple Joins: Inner, Full Outer, Left Outer, Right Outer, Natural
* CROSS Joins are not detected
* Implicit Inner Joins are not detected but are implicit detectable if multiple column names occur (i.e. FROM tab1, tab2)
* Subqueries

#### WHERE clause

* ON clause from Join-Statement 
* Logic AND/OR/NOT (not in CCNF)
* Comparison < > = <= >=

#### HAVING clause

* Same as WHERE Clause

#### Set operations

* UNION: Each SELECT statement is separately parsed

#### Subqueries

* In column list, table list and where clause: each SELECT is parsed separately



### Classifier - Implemented Features

#### SELECT column list

* Check of simple column names
* Support for Asterisk * 
* Check of functions (without function groups). Additional functions on unbound columns are allowed.
* Check of calculations as strings, calculations with unbound columns are not allowed. (i.e. SELECT unbound .. cannot be altered to SELECT unbound*2 .. )

#### FROM table list

* table list must be exactly the same (same table name, join or subquery).

#### WHERE clause

* Check for existence
* Subqueries must match   
* Logic AND/OR/NOT: Removal of disjunctions, adding of conjunctions with unbound columns. No CCNF. 
* Comparisons with explicitly stated columns (displayed in select column list) are allowed. No checking for subsets comparisons (i.e. col > 1500 instead of col > 1000).
* Functions: allowed on explicitly stated columns (displayed in select column list).
* General: A query may use a new statement in the where clause only if the column is visible in the select column list

#### Set operations

* UNION: Checks each SELECT statement. Caveat: The query string with UNION is saved for each separate SELECT statement. The query string of each separate SELECT should be saved for each SELECT.

#### Subqueries

* Classified as part of each statement, i.e. in SELECT <subquery> FROM .. the subquery is treated as a column. Therefore the subquery itself must be exactly the same, no variations allowed. Applies to column list, table list and where clause.


