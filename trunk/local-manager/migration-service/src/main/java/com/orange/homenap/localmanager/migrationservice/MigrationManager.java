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

import com.orange.homenap.api.MigrationHandlerManagerItf;
import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.utils.Component;

import java.util.HashMap;
import java.util.Map;

public class MigrationManager implements MigrationHandlerManagerItf
{
    // iPOJO requires
    private LocalDatabaseItf localDatabaseItf;

    protected void start()
    {
        // Attach handler to iPOJO components (TODO: to move to pom.xml?)
        System.setProperty("org.apache.felix.ipojo.handler.auto.primitive", "com.orange.homenap.handler:migration-handler");
    }

    public Map<String, Object> registerComponent(Long bundleId, String componentName)
    {
        Component component = localDatabaseItf.getComponent(componentName);

        if (component.getState().equals(Component.State.STATEFUL))
        {
            System.out.println("Adding component " + componentName + " to bundle " + bundleId);

            if (component.getProperties().get(componentName) == null)
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

    public void unRegisterComponent(Long bundleId, Map<String, Object> propertiesMap, String componentName)
    {
        Component component = localDatabaseItf.getComponent(componentName);

        if (component.getState().equals(Component.State.STATEFUL))
        {
            System.out.println("Removing component " + componentName + " from bundle " + bundleId);

            component.setProperties(propertiesMap);
        }
    }
}