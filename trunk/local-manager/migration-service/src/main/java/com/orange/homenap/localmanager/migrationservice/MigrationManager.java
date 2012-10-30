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
import com.orange.homenap.utils.Service;
import com.orange.homenap.utils.StatefulComponent;

import java.util.Map;

public class MigrationManager implements MigrationHandlerManagerItf
{
    // iPOJO requires
    private LocalDatabaseItf localDatabaseItf;

    // iPOJO properties
    private String directory;

    protected void start()
    {
        // Attach handler to iPOJO components (TODO: to move to pom.xml?)
        System.setProperty("org.apache.felix.ipojo.handler.auto.primitive", "com.orange.homenap.handler:migration-handler");
    }

    public Map<String, Object> registerComponent(Long bundleId, String instanceName)
    {
        System.out.println("Adding component " + instanceName + " to bundle " + bundleId);

        Service service = localDatabaseItf.get(bundleId);

        if (service.getComponents().get(instanceName) == null)
        {
            StatefulComponent statefulComponent = new StatefulComponent();

            statefulComponent.setComponentName(instanceName);

            service.getComponents().put(instanceName, statefulComponent);

            return null;
        }

        return service.getComponents().get(instanceName).getProperties();
    }

    public void unRegisterComponent(Long bundleId, Map<String, Object> propertiesMap, String instanceName)
    {
        System.out.println("Removing component " + instanceName + " from bundle " + bundleId);

        Service service = localDatabaseItf.get(bundleId);

        service.getComponents().get(instanceName).setProperties(propertiesMap);
    }
}