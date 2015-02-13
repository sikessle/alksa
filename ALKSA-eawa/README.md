# Easily Attackable Web Application EAWA

Web application which offers some security issues to allow SQL injections. Some additional information is provided in the frontend to allow easy testing of different scenarios with ALKSA. 

The database is the in-memory H2 database. As data set the MySQL world database is used, which was altered at some points to enable compatibility with the H2 database. The database is cleaned at each startup by using the Play evolution scripts.

## Build

* Build with sbt
* Play Framework 2.3.7

## Run

* activator run

## Database Scheme (Tables)

### City

* ID
* Name
* CountryCode
* District
* Population

### Country

* Code
* Name
* Continent
* Region
* SurfaceArea
* IndepYear
* Population
* LifeExpectancy
* GNP
* GNPOld
* LocalName
* GovernmentForm
* HeadOfState
* Capital
* Code2

### CountryLanguage

* CountryCode
* Language
* IsOfficial
* Percentage



