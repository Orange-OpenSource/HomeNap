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
 *  File Name   : PropertiesHandler.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.xml;

import com.orange.homenap.utils.Properties;
import com.orange.homenap.utils.Property;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class PropertiesHandler extends DefaultHandler
{
    private List<Properties> propertiesList;
    private Properties properties;
    private Property property;
    private List<String> list;

    private String parent;
    private boolean reading;

    public PropertiesHandler()
    {
        super();
        propertiesList = new ArrayList<Properties>();
    }

    public void startDocument() throws SAXException
    {
        reading = true;
    }

    public void endDocument() throws SAXException
    {
        reading = false;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        if(qName.equals("properties"))
        {
            properties = new Properties();

            for (int i = 0; i < attributes.getLength(); i++)
                if(attributes.getQName(i).equals("class"))
                    properties.setClassName(attributes.getValue(i));

            parent = "properties";
        }
        else if(qName.equals("string"))
        {
            if(parent.equals("properties"))
            {
                property = new Property();

                for (int i = 0; i < attributes.getLength(); i++)
                {
                    if(attributes.getQName(i).equals("name"))
                        property.setName(attributes.getValue(i));
                    else if(attributes.getQName(i).equals("value"))
                        property.setValue(attributes.getValue(i));
                }
            }
            else
                for (int i = 0; i < attributes.getLength(); i++)
                    if(attributes.getQName(i).equals("value"))
                        list.add(attributes.getValue(i));
        }
        else if(qName.equals("boolean"))
        {
            if(parent.equals("properties"))
            {
                property = new Property();

                for (int i = 0; i < attributes.getLength(); i++)
                {
                    if(attributes.getQName(i).equals("name"))
                        property.setName(attributes.getValue(i));
                    else if(attributes.getQName(i).equals("value"))
                        property.setValue(new Boolean(attributes.getValue(i)));
                }
            }
            else
                for (int i = 0; i < attributes.getLength(); i++)
                    if(attributes.getQName(i).equals("value"))
                        list.add(attributes.getValue(i));
        }
        else if(qName.equals("long"))
        {
            if(parent.equals("properties"))
            {
                property = new Property();

                for (int i = 0; i < attributes.getLength(); i++)
                {
                    if(attributes.getQName(i).equals("name"))
                        property.setName(attributes.getValue(i));
                    else if(attributes.getQName(i).equals("value"))
                        property.setValue(new Long(attributes.getValue(i)));
                }
            }
            else
                for (int i = 0; i < attributes.getLength(); i++)
                    if(attributes.getQName(i).equals("value"))
                        list.add(attributes.getValue(i));
        }
        else if(qName.equals("list"))
        {
            property = new Property();
            list = new ArrayList<String>();

            for (int i = 0; i < attributes.getLength(); i++)
                if(attributes.getQName(i).equals("name"))
                    property.setName(attributes.getValue(i));

            parent = "list";
        }
    }

    public void endElement(String uri, String localName, String qName)
    {
        if(qName.equals("properties"))
        {
            propertiesList.add(properties);
        }
        else if(qName.equals("list"))
        {
            property.setValue(list);
            properties.getProperty().add(property);
            parent = "properties";
        }
        else
        {
            properties.getProperty().add(property);
        }
    }

    public List<Properties> getProperties()
    {
        return propertiesList;
    }

    public boolean isReading()
    {
        return reading;
    }
}
