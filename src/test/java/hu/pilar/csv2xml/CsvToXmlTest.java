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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import javax.xml.stream.XMLStreamException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author SZIGETI János
 */
public class CsvToXmlTest {

    public CsvToXmlTest() {
    }

    @Test(expected = NoSuchElementException.class)
    public void testPipeEmpty() throws XMLStreamException, IOException {
        String inputStr = "";
        InputStream is = new ByteArrayInputStream(inputStr.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CsvToXml converter = new CsvToXml();
        converter.pipe(is, os);
    }

    @Test
    public void testPipeNoColumnsNoRecords() throws XMLStreamException, IOException {
        String inputStr = "\n";
        String expectedStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<data></data>\n";

        InputStream is = new ByteArrayInputStream(inputStr.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CsvToXml converter = new CsvToXml();
        converter.pipe(is, os);
        Assert.assertEquals(expectedStr, os.toString());
    }

    @Test
    public void testPipeNoColumns() throws XMLStreamException, IOException {
        String inputStr = "\n\n";
        String expectedStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<data>"
                + "<record></record>"
                + "</data>\n";

        InputStream is = new ByteArrayInputStream(inputStr.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CsvToXml converter = new CsvToXml();
        converter.pipe(is, os);
        Assert.assertEquals(expectedStr, os.toString());
    }

    @Test
    public void testPipeNoRecord() throws XMLStreamException, IOException {
        String inputStr = "A,B\n";
        String expectedStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<data></data>\n";

        InputStream is = new ByteArrayInputStream(inputStr.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CsvToXml converter = new CsvToXml();
        converter.pipe(is, os);
        Assert.assertEquals(expectedStr, os.toString());
    }

    @Test
    public void testPipeSimple() throws XMLStreamException, IOException {
        String inputStr = "A,B\n1,2\n3,4\n";
        String expectedStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<data>"
                + "<record>"
                + "<A>1</A>"
                + "<B>2</B>"
                + "</record>"
                + "<record>"
                + "<A>3</A>"
                + "<B>4</B>"
                + "</record>"
                + "</data>\n";

        InputStream is = new ByteArrayInputStream(inputStr.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CsvToXml converter = new CsvToXml();
        converter.pipe(is, os);
        Assert.assertEquals(expectedStr, os.toString());
    }
}
