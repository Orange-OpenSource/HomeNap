/*
 * --------------------------------------------------------
 *  Module Name : service-database
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
 *  File Name   : ServiceInfoDB.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.localdatabase;

import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Property;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class LocalDatabase implements LocalDatabaseItf
{
    // iPOJO properties
    private Map<String, Component> componentMap;
    //private Map<String, List<Property>> propertiesMap;
    private Map<String, Map<String, Object>> propertiesMap;

    public void put(String name, Component component) { componentMap.put(name, component); }

    public Component get(String name) { return componentMap.get(name); }

    public boolean containsKey(String name) { return componentMap.containsKey(name);}

    public Component remove(String name) { return componentMap.remove(name); }

    public Collection<Component> values() { return componentMap.values(); }

    //public String getServiceName(Long serviceId) { return componentMap.get(serviceId).getName(); }

    /*public void put(String name, List<Property> properties) { propertiesMap.put(name, properties); }

    public List<Property> getProperties(String name) { return propertiesMap.get(name); }*/

    public void put(String name, Map<String, Object> properties) { propertiesMap.put(name, properties); }

    public Map<String, Object> getProperties(String name) { return propertiesMap.get(name); }
}
