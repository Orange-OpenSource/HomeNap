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
 *  File Name   : ServiceInfoDB.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager;

import java.util.Collection;
import java.util.Map;
import com.orange.homenap.utils.Service;

public class ServiceInfoDB implements ServiceInfoDBItf
{
    // iPOJO properties
    private boolean stateful;
    private Map<Long, Service> serviceInfoMap;

    public void put(Long serviceId, Service service) { serviceInfoMap.put(serviceId, service); }

    public Service get(Long serviceId) { return serviceInfoMap.get(serviceId); }

    public boolean containsKey(Long serviceId) { return serviceInfoMap.containsKey(serviceId); }

    public void remove(Long serviceId) { serviceInfoMap.remove(serviceId); }

    public Collection<Service> values() { return serviceInfoMap.values(); }
    
    public String getServiceName(Long serviceId) { return serviceInfoMap.get(serviceId).getName(); }
    
    public Long getServiceId(String serviceName)
    {
        for(Map.Entry<Long, Service> e : serviceInfoMap.entrySet())
        {
            if(e.getValue().getName().equals(serviceName))
                return e.getKey();
        }

        return null;
    }
}
