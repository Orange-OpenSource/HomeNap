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

import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.utils.Component;

import java.util.HashMap;
import java.util.Map;

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

            if (component.getProperties().get(name) == null)
            {
                component.setProperties(new HashMap<String, Object>());

                return null;
            }
            else
                return component.getProperties();
        }
        else
            return null;
    }

    public void unRegisterComponent(Map<String, Object> propertiesMap, String componentName)
    {
        Component component = localDatabaseItf.get(componentName);

        if (component.getState().equals(Component.State.STATEFUL))
        {
            System.out.println("Removing component " + componentName);

            component.setProperties(propertiesMap);
        }
    }
}