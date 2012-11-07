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

import java.util.Collection;
import java.util.Map;

public class LocalDatabase implements LocalDatabaseItf
{
    // iPOJO properties
    private Map<String, Component> infoMap;

    public void put(String name, Component component) { infoMap.put(name, component); }

    public Component get(String name) { return infoMap.get(name); }

    public boolean containsKey(String name) { return infoMap.containsKey(name);}

    public Component remove(String name) { return infoMap.remove(name); }

    public Collection<Component> values() { return infoMap.values(); }

    //public String getServiceName(Long serviceId) { return infoMap.get(serviceId).getName(); }

}
