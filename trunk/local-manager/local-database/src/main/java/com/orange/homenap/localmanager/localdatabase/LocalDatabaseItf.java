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
 *  File Name   : ServiceInfoDBItf.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.localdatabase;

import com.orange.homenap.utils.Service;

import java.util.Collection;

public interface LocalDatabaseItf
{
    public void put(Long serviceId, Service service);

    public Service get(Long serviceId);

    public boolean containsKey(Long serviceId);

    public void remove(Long serviceId);

    public Collection<Service> values();
    
    public String getServiceName(Long serviceId);
    
    public Long getServiceId(String serviceName);
}
