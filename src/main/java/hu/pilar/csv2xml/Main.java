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

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * This application converts CSV data to XML format.
 * The
 * @author SZIGETI János
 */
public class Main {

    final static String CSVENCODING = "UTF-8";
    final static String XMLENCODING = "UTF-8";
    final static String XMLVERSION = "1.0";
    final static String XMLROOT = "data";
    final static String XMLROW = "record";

    /**
     * @param args Arguments, currently, are not parsed.
     * @throws javax.xml.stream.XMLStreamException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws XMLStreamException, IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = new IndentingXMLStreamWriter(
                factory.createXMLStreamWriter(
                        new OutputStreamWriter(System.out)
                )
        );

        CSVParser parser = new CSVParser(
                new InputStreamReader(System.in,CSVENCODING),
                CSVFormat.
                        RFC4180.    // base settings
                        withRecordSeparator('\n').  // do not deal with \r
                        withHeader((String) null)   // the first record will be used as the header
        );
        Iterator<CSVRecord> recordIt = parser.iterator();
        CSVRecord header = recordIt.next();

        writer.writeStartDocument(XMLENCODING, XMLVERSION);
        writer.writeStartElement(XMLROOT);
        while (recordIt.hasNext()) {
            writer.writeStartElement(XMLROW);
            CSVRecord record = recordIt.next();
            for (int i = 0; i < header.size(); ++i) {
                if (record.get(i) != null) {
                    writer.writeStartElement(header.get(i));
                    writer.writeCharacters(record.get(i));
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.writeCharacters("\n");

        writer.flush();
        writer.close();
        parser.close();
    }

}
