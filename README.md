# csv2xml

Converts csv files to xml format.
`csv2xml` requires csv input file in utf8 encoding at standard input and writes xml in utf8 encoding to standard output.
It does not take any command line argument or system property.
It does not catch any java exeption.

## Usage

Process `input.csv` and write unformatted xml to standard output:
```
cat input.csv | bin/csv2xml.sh
```

Write formatted xml to standard output:
```
cat input.csv | bin/csv2xml.sh | xmllint --format -
```

Store output in file:
```
cat input.csv | bin/csv2xml.sh | xmllint --format - > output.xml
```

Process input given in ISO-8859-1 encoding:
```
cat input.latin1.csv | iconv -f latin1 -t utf8 | bin/csv2xml.sh
```

Transform output into ISO-8859-1 encoding:
```
cat input.utf8.csv | bin/csv2xml.sh | \
sed 's/^<?xml version="1.0" encoding="UTF-8"?>/<?xml version="1.0" encoding="ISO-8859-1"?>"/' | \
iconv -f utf8 -t latin1
```
