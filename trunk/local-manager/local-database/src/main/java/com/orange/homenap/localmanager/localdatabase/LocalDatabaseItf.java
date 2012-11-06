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

import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Service;

import java.util.Collection;

public interface LocalDatabaseItf
{
    public void put(String name, Architecture architecture);

    public Architecture get(String name);

    public boolean containsKey(String name);

    public Architecture remove(String name);

    public Collection<Architecture> values();
    
    //public String getServiceName(String name);
    
    //public Long getComponentId(String componentName);

    public Component getComponent(String componentName);
}
