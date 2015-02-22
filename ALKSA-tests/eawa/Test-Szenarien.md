# Szenario 1

## Gelernte Abfragen

ALKSA deaktiviert.

## Angriff

Nicht ersichtliche Spalte abgefragt: Country.GNP
Filter modifiziert: Country.Population > 1000000 AND IndepYear > 1990
UNION: 	1=0 UNION ALL SELECT Country.GNP FROM Country
Filter: Country.Name = 'Germany' OR 1=1  umgeht den Filter.
Zugriff auf andere Tabelle (Basis für weitere Angriffe) mittels Unterabfrage in Spaltenliste: (SELECT table_name FROM information_schema.tables Limit 1, 1) AS table


# Szenario 2

## Gelernte Abfragen

Normales lernen (normales Nutzungsmuster).

Alle regulären Spalten außer Country.GovernmentForm

Spalte + Filter: 

City.Name + Großstädte
City.Name + Kleinstädte UND wohlhabende Länder
City.Name + Country.Name = Germany
City.Name + Country.GovernmentForm = Republic

## Angriff

Angriffe aus Szenario 1 werden abgewehrt.

Jedoch machen einige Abfragen Probleme:

Filter: 

Es wurden nicht alle Filter-Kombinationen gelernt. Da die Filter mit AND verknüpft sind, kann ein einzelner Filter nicht einfach weggelasssen werden.

Die Filter wurden mit der Spalte City.Name gelernt. Wenn nun eine andere Spalte (z.B. Country.Name) mit dem Filter Großstädte ausgeführt wird, dann wird dies als Angriff erkannt. Dies kann in diesem Fall nicht umgangen werden, da die Spalte aus dem Filter nicht öffentlich in der Spaltenliste steht (und somit keine freien Filter erlaubt sind). 

Die Kombination City.Name + Country.GovernmentForm = Republic kann nicht variiert werden. Wird anstelle Republic z.B. "Federal Republic" benutzt, wird dies als Angriff gedeutet. Dies liegt daran, dass die Spalte nicht in der Spaltenliste gelernt wurde. Sonst könnte diese beliebig benutzt werden. 

Generell ergibt sich das Problem, sobald sehr viele Kombinationsmöglichkeiten gegeben sind. Deshalb ist die Lernphase als kritisch einzustufen. Einerseits lange genug -> andererseits keine Angriffe mit lernen!

# Szenario 3

## Gelernte Abfragen

Angriffe mit gelernt.

City.Name
City.District
Country.Name
Country.Population
Country.Continent
Country.GovernmentForm
Country.Region
Country.HeadOfState
CountryLanguage.Language
CountryLanguage.Percentage

Durch Angriff eingeschleußt: Country.IndepYear und Filter Country.GNP > 10

## Angriff

Die Angriffe auf die eingeschleußten Spalten Country.IndepYear und den Filter Country.GNP sind nun möglich.
Beide werden von ALKSA als legal eingestuft, da sie in der Lernphase gespeichert wurden.

