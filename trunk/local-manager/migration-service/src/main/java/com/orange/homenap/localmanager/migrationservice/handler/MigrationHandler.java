/*--------------------------------------------------------
* Module Name : migration-service
* Version : 0.1-SNAPSHOT
*
* Software Name : HomeNap
* Version : 0.1-SNAPSHOT
*
* Copyright © 28/06/2012 – 28/06/2012 France Télécom
* This software is distributed under the Apache 2.0 license,
* the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
* or see the "LICENSE-2.0.txt" file for more details.
*
*--------------------------------------------------------
* File Name   : MigrationHandler.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.localmanager.migrationservice.handler;

import com.orange.homenap.localmanager.migrationservice.MigrationManagerItf;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.InstanceManager;
import org.apache.felix.ipojo.PrimitiveHandler;
import org.apache.felix.ipojo.architecture.PropertyDescription;
import org.apache.felix.ipojo.metadata.Element;
import org.apache.felix.ipojo.parser.FieldMetadata;
import org.apache.felix.ipojo.parser.PojoMetadata;
import org.osgi.framework.Bundle;

import java.util.*;

public class MigrationHandler extends PrimitiveHandler
{
    // iPOJO requires
    private MigrationManagerItf migrationManagerItf;

    // Global variables
    private Map<String, Object> properties = new HashMap<String, Object>();

    public void configure(Element metadata, Dictionary config) throws ConfigurationException
    {
        InstanceManager im = getInstanceManager();

        Bundle bundle = im.getContext().getBundle();

        System.out.println("Configure Migration Handler for " + im.getContext().getBundle().getSymbolicName());

        PropertyDescription[] propDesc = im.getInstanceDescription().getComponentDescription().getProperties();

        for(PropertyDescription property : propDesc)
            properties.put(property.getName(), property.getValue());

        if(!properties.isEmpty())
        {
            Enumeration<String> key = config.keys();

            while(key.hasMoreElements())
            {
                String tempKey = key.nextElement();

                if(properties.containsKey(tempKey))
                    properties.put(tempKey, config.get(tempKey));
            }
        }
    }

    public void start()
    {
        System.out.println("Starting " + getInstanceManager().getContext().getBundle().getSymbolicName());

        InstanceManager im = getInstanceManager();
        Bundle bundle = im.getContext().getBundle();

        Map<String, Object> migrationProp = migrationManagerItf.registerComponent(bundle.getSymbolicName());

        if(migrationProp != null)
            properties = migrationProp;

        if(!properties.isEmpty())
        {
            PojoMetadata pojoMetadata = getPojoMetadata();

            for(Map.Entry<String, Object> e : properties.entrySet())
            {
                System.out.println(e.getKey() + " : " + e.getValue());

                FieldMetadata fm = pojoMetadata.getField(e.getKey());

                if(fm != null)
                    getInstanceManager().register(fm, this);
            }
        }
    }

    public void stop()
    {
        InstanceManager im = getInstanceManager();

        System.out.println("Stopping " + im.getContext().getBundle().getSymbolicName());

        migrationManagerItf.unRegisterComponent(properties, im.getInstanceName());
    }

    public Object onGet(Object pojo, String field, Object o)
    {
        System.out.println("Getting " + field + " (" + o.toString() + ")");

        return properties.get(field);
    }

    public void onSet(Object pojo, String field, Object o)
    {
        System.out.println("Setting " + field + " to " + o.toString());

        properties.put(field, o);
    }
}