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
 *  File Name   : MigrationManager.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.migrationservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.utils.*;

import java.lang.reflect.Type;
import java.util.*;

public class MigrationManager implements MigrationManagerItf
{
    // iPOJO requires
    private LocalDatabaseItf localDatabaseItf;

    protected void start()
    {
        // Attach handler to iPOJO components (TODO: to move to pom.xml?)
        //System.setProperty("org.apache.felix.ipojo.handler.auto.primitive", "com.orange.homenap.localmanager.migrationservice.handler:migration-handler");
    }

    public Map<String, Object> registerComponent(String name)
    {
        Component component = localDatabaseItf.get(name);

        if (component.getState().equals(Component.State.STATEFUL))
        {
            System.out.println("Adding component " + name);

            Map<String, Object> propertiesMap = localDatabaseItf.getProperties(name);

            // List<Property> properties = localDatabaseItf.getProperties(name);

            /*if(!properties.isEmpty())
            {*/
                /*localDatabaseItf.put(component.getName(), new ArrayList<Property>());
                //component.setProperties(new HashMap<String, Object>());
                //component.setPropertiesType(new HashMap<String, String>());
            }
            else
            {*/
                /*System.out.println("Starting reading properties");

                Map<String, Object> map = new HashMap<String, Object>();*/

                /*Map<String, Object> properties = component.getProperties();
                Map<String, String> propertiesType = component.getPropertiesType();*/

                //System.out.println(e.getValue().getClass());

               /* Iterator<Property> it = properties.iterator();

                while(it.hasNext())
                {
                    Property property = it.next();

                    map.put(property.getName(), property.getValue());
                }*/

                /*for(Map.Entry<String, Object> map : properties.entrySet())
                {
                    if(map.getValue() instanceof List)
                    {
                        List<Object> list = (List) map.getValue();

                        System.out.println("--> " + map.getKey());

                        Gson gson = new Gson();

                        if(propertiesType.get(map.getKey()))

                            for(int i = 0; i < list.size(); i++)
                                list.add(gson.fromJson(map.getValue().toString(), Class<propertiesType.get(map.getKey())>.getClass()));

                        /*if(e.getKey().equals("architectures"))
                            Architecture architecture = gson.fromJson((String) list.get(0), Architecture.class);*/

                /*for(int i = 0; i < list.size(); i++)
                        {
                            //System.out.println(list.get(i).getClass());

                            System.out.println(list.get(i));
                        }
                    }
                }*/

                //return properties;
             /*   return map;
            }*/
        }

        return null;
    }

    public void unRegisterComponent(Map<String, Object> propertiesMap, String name)
    {
        Component component = localDatabaseItf.get(name);

        if (component.getState().equals(Component.State.STATEFUL))
        {
            System.out.println("Removing component " + name + " (" + propertiesMap.size() + " properties)");

            List<Property> properties = new ArrayList<Property>();

            for(Map.Entry<String, Object> map: propertiesMap.entrySet())
            {
                System.out.println(map.getKey());

                //Property property = setProperties(map.getValue());

                Property property = new Property();

                property.setName(map.getKey());
                property.setValue(map.getValue());

                properties.add(property);

                /*String type = null;

                if(map.getValue() instanceof List)
                {
                    List<Object> list = (List) map.getValue();

                    Type elementType = list.get(0).getClass();

                    Type typeOfObjects = new TypeToken<List<el>>(){}.getType();

                    type = "java.util.List<";

                    System.out.println(list.get(0).getClass());

                    type += list.get(0).getClass() + ">";
                }
                else
                {
                    type = map.getValue().getClass().getName();
                }

                System.out.println("Type is " + type);

                propertiesType.put(map.getKey(), type);*/
            }

            System.out.println("Putting " + properties.size() + " properties to DB");

            //localDatabaseItf.put(component.getName(), properties);
            localDatabaseItf.put(component.getName(), propertiesMap);

            //component.setProperties(properties);

            //component.setPropertiesType(propertiesType);
        }
    }

/*    private Property setProperties(Object object)
    {
        Property property = new Property();

        //System.out.println("Inside " + object.getClass());

        property.setType(object.getClass().getName());

        if(object instanceof List)
        {
            List<Object> list = (List) object;

            Iterator<Object> it = list.iterator();

            property.setValue(new ArrayList<Property>());
            
            List<Property> propertyList = (ArrayList) property.getValue();
            
            while(it.hasNext())
                propertyList.add(setProperties(it.next()));
        }
        else
        {
            //Gson gson = new Gson();

            //System.out.println(gson.toJson(object));

            property.setValue(object);
        }

        return property;
    }*/
}