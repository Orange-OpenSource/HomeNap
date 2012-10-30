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

package com.orange.homenap.services.localdatabase;

import com.orange.homenap.utils.Service;

import java.util.Collection;
import java.util.Map;

public class LocalDatabase implements LocalDatabaseItf
{
    // iPOJO properties
    private Map<Long, Service> infoMap;

    public void put(Long serviceId, Service service) { infoMap.put(serviceId, service); }

    public Service get(Long serviceId) { return infoMap.get(serviceId); }

    public boolean containsKey(Long serviceId) { return infoMap.containsKey(serviceId);}

    public void remove(Long serviceId) { infoMap.remove(serviceId); }

    public Collection<Service> values() { return infoMap.values(); }
    
    public String getServiceName(Long serviceId) { return infoMap.get(serviceId).getName(); }
    
    public Long getServiceId(String serviceName)
    {
        for(Map.Entry<Long, Service> e : infoMap.entrySet())
            if(e.getValue().getName().equals(serviceName))
                return e.getKey();

        return null;
    }
}
