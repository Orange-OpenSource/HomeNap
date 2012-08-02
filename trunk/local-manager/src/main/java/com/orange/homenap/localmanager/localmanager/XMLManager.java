/*
 * --------------------------------------------------------
 *  Module Name : local-manager
 *  Version : 0.1-SNAPSHOT
 *
 *  Software Name : HomeNap
 *  Version : 0.1-SNAPSHOT
 *
 *  Copyright © 28/06/2012 – 28/06/2012 France Télécom
 *  This software is distributed under the Apache 2.0 license,
 *  the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 *  or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 *  File Name   : XMLManager.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.localmanager;

import com.orange.homenap.utils.Properties;
import com.orange.homenap.localmanager.xml.MigrationReader;
import com.orange.homenap.localmanager.xml.MigrationSource;
import com.orange.homenap.localmanager.xml.PropertiesHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Inspired from: http://java.developpez.com/faq/xml/?page=xslt#creerXmlSax
 */

public class XMLManager implements XMLManagerItf
{
    // iPOJO properties
    private String directory;
    private boolean stateful;

    public void write(String filename, List<Properties> propertiesList)
    {
        XMLReader migrationReader = new MigrationReader();
        InputSource migrationSource = new MigrationSource(propertiesList);
        Source source = new SAXSource(migrationReader, migrationSource);

        File file = new File(directory + filename);

        System.out.println("Writing " + propertiesList.size() + " properties");

        if(file.exists())
            file.delete();

        Result result = new StreamResult(file);

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(source, result);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public List<Properties> read(String filename)
    {
        List<Properties> properties = new ArrayList<Properties>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            File file = new File(directory + filename);
            DefaultHandler manager = new PropertiesHandler();
            parser.parse(file, manager);

            // Wait for the XML document to be completely parsed
            while(((PropertiesHandler) manager).isReading());

            properties = ((PropertiesHandler) manager).getProperties();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
