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

import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class LocalDatabase implements LocalDatabaseItf
{
    // iPOJO properties
    private Map<String, Architecture> infoMap;

    public void put(String name, Architecture architecture) { infoMap.put(name, architecture); }

    public Architecture get(String name) { return infoMap.get(name); }

    public boolean containsKey(String name) { return infoMap.containsKey(name);}

    public Architecture remove(String name) { return infoMap.remove(name); }

    public Collection<Architecture> values() { return infoMap.values(); }

    //public String getServiceName(Long serviceId) { return infoMap.get(serviceId).getName(); }

/*
    public Long getComponentId(String componentName)
    {
        for(Map.Entry<String, Architecture> e : infoMap.entrySet())
        {
            Iterator<Component> it = e.getValue().getComponent().iterator();

            while(it.hasNext())
            {
                Component component = it.next();

                if(component.getName().equals(componentName))
                    return component.getId();
            }
        }

        return null;
    }
*/

    public Component getComponent(String componentName)
    {
        for(Map.Entry<String, Architecture> e : infoMap.entrySet())
        {
            Iterator<Component> it = e.getValue().getComponent().iterator();

            while(it.hasNext())
            {
                Component component = it.next();

                if(component.getName().equals(componentName))
                    return component;
            }
        }

        return null;
    }

    /*public Architecture getParent(String componentName)
    {
        Architecture architecture = null;

        for(Map.Entry<String, Architecture> e : infoMap.entrySet())
        {
            Iterator<Component> it = e.getValue().getComponent().iterator();

            while(it.hasNext())
            {
                Component component = it.next();

                if(component.getName().equals(componentName))
                    architecture = e.getValue();
            }
        }

        return architecture;
    }*/
}
