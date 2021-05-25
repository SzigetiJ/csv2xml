/*
 * Copyright 2017 SZIGETI János.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.pilar.csv2xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author SZIGETI János
 */
public class CsvToXml {

    private static final String CSVENCODING = "UTF-8";
    private static final String XMLENCODING = "UTF-8";
    private static final String XMLVERSION = "1.0";
    private static final String XMLROOT = "data";
    private static final String XMLROW = "record";

    final XMLOutputFactory factory;

    public CsvToXml() {
        this.factory = XMLOutputFactory.newInstance();
    }

    void pipe(InputStream is, OutputStream os) throws XMLStreamException, IOException {
        XMLStreamWriter writer = factory.createXMLStreamWriter(
                new OutputStreamWriter(os)
        );

        try (CSVParser parser = new CSVParser(
                new InputStreamReader(is, CSVENCODING),
                CSVFormat.RFC4180. // base settings
                withRecordSeparator('\n'). // do not deal with \r
                withSkipHeaderRecord(true) // the first record will be used as the header
        )) {
            Iterator<CSVRecord> recordIt = parser.iterator();
            CSVRecord header = recordIt.next();

            writer.writeStartDocument(XMLENCODING, XMLVERSION);
            writer.writeStartElement(XMLROOT);
            while (recordIt.hasNext()) {
                writer.writeStartElement(XMLROW);
                writeRecord(header, recordIt.next(), writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.writeCharacters("\n");

            writer.flush();
            writer.close();
        }
    }

    private void writeRecord(CSVRecord header, CSVRecord record, XMLStreamWriter writer) throws XMLStreamException {
        for (int i = 0; i < header.size(); ++i) {
            if (!header.get(i).isEmpty() && record.get(i) != null) {
                writer.writeStartElement(header.get(i));
                writer.writeCharacters(record.get(i));
                writer.writeEndElement();
            }
        }
    }
}
