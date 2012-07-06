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
 *  File Name   : MigrationReader.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.olnc.made.homenap.xml;

import com.olnc.made.homenap.utils.Properties;
import com.olnc.made.homenap.utils.Property;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MigrationReader implements XMLReader
{
    private ContentHandler handler;
    private AttributesImpl atts = new AttributesImpl();

    public ContentHandler getContentHandler() { return handler; }

    public void setContentHandler(ContentHandler handler) { this.handler = handler; }

    public void parse(InputSource input) throws IOException,SAXException
    {
        if(!(input instanceof MigrationSource))
            throw new SAXException("MigrationSource not found");

        if(handler == null)
            throw new SAXException("No ContentHandler");

        MigrationSource source = (MigrationSource) input;
        List<Properties> propertiesList = source.getProperties();

        handler.startDocument();

        handler.startElement("", "state", "state", null);

        for(Properties properties : propertiesList)
        {
            atts.addAttribute("", "class", "class", "", properties.getClassName());
            handler.startElement("", "properties", "properties", atts);
            atts.clear();

            for (Property property : properties.getProperty())
            {
                if(property.getValue().getClass().getCanonicalName().equals("java.lang.String"))
                {
                    atts.addAttribute("", "name", "name", "", String.valueOf(property.getName()));
                    atts.addAttribute("", "value", "value", "", String.valueOf(property.getValue()));
                    handler.startElement("", "string", "string", atts);
                    atts.clear();
                    handler.endElement("", "string", "string");
                }
                else if(property.getValue().getClass().getCanonicalName().equals("java.lang.Boolean"))
                {
                    atts.addAttribute("", "name", "name", "", String.valueOf(property.getName()));
                    atts.addAttribute("", "value", "value", "", String.valueOf(property.getValue()));
                    handler.startElement("", "boolean", "boolean" , atts);
                    atts.clear();
                    handler.endElement("", "boolean", "boolean");
                }
                else if(property.getValue().getClass().getCanonicalName().equals("java.lang.Long"))
                {
                    atts.addAttribute("", "name", "name", "", String.valueOf(property.getName()));
                    atts.addAttribute("", "value", "value", "", String.valueOf(property.getValue()));
                    handler.startElement("", "long", "long" , atts);
                    atts.clear();
                    handler.endElement("", "long", "long");
                }
                else if(property.getValue().getClass().getCanonicalName().equals("java.util.ArrayList"))
                {
                    atts.addAttribute("", "name", "name", "", String.valueOf(property.getName()));
                    handler.startElement("", "list", "list" , atts);
                    atts.clear();

                    List<String> tempList = (ArrayList<String>) property.getValue();

                    for(int i = 0; i < tempList.size(); i++)
                    {
                        atts.addAttribute("", "value", "value", "", tempList.get(i));
                        handler.startElement("", "string", "string", atts);
                        atts.clear();
                        handler.endElement("", "string", "string");
                    }

                    handler.endElement("", "list", "list");
                }
            }

            handler.endElement("", "properties", "properties");
        }

        handler.endElement("", "state", "state");

        handler.endDocument();
    }

    public void parse(String systemId) throws IOException,SAXException { throw new SAXException("MigrationSource not found"); }

    public DTDHandler getDTDHandler() { return null; }

	public EntityResolver getEntityResolver() { return null; }

	public ErrorHandler getErrorHandler() { return null; }

	public boolean getFeature(String name) { return false; }

	public Object getProperty(String name) { return null; }

	public void setDTDHandler(DTDHandler handler) {}

	public void setEntityResolver(EntityResolver resolver) {}

	public void setErrorHandler(ErrorHandler handler) {}

	public void setFeature(String name, boolean value) {}

	public void setProperty(String name, Object value) {}
}
