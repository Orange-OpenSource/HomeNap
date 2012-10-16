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
 *  File Name   : ControlPointManager.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ControlPointManager implements ControlPointManagerItf
{
    // iPOJO requires
    private Factory factory;

    // iPOJO properties
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private Map<String, ComponentInstance> deployerControlPointMap;

    public ControlPointManager(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        deployerControlPointMap = new HashMap<String, ComponentInstance>();
    }

    public DeployerControlPointItf createCP(String udn, String wakeUpAddress)
    {
        ComponentInstance deployerInstance = null;

        if(!deployerControlPointMap.containsKey(udn))
        {
            Properties properties = new Properties();
            deployerInstance = null;

            properties.put("instance.name","deployer-control-point");
            properties.put("udnDeployer", udn);
            properties.put("wakeUpAddress", wakeUpAddress);

            try {
                deployerInstance = factory.createComponentInstance(properties);
            } catch(Exception e) {
                e.printStackTrace();
            }

            deployerInstance.start();

            deployerControlPointMap.put(udn, deployerInstance);
        }
        else
        {
            deployerInstance = deployerControlPointMap.get(udn);
        }

        DeployerControlPointItf deployerControlPointItf = null;

        try {
            ServiceReference[] refs = bundleContext.getServiceReferences(DeployerControlPointItf.class.getName(),
                            "(instance.name=" + deployerInstance.getInstanceName() +")");

            if (refs != null)
                deployerControlPointItf = (DeployerControlPointItf) bundleContext.getService(refs[0]);
        } catch (InvalidSyntaxException e) {
            // Should not happen
        }

        return deployerControlPointItf;
    }

    public void removeCP(String udn)
    {
        if (deployerControlPointMap.containsKey(udn))
        {
            ComponentInstance deployerInstance = deployerControlPointMap.get(udn);

            deployerInstance.stop();

            deployerInstance.dispose();

            deployerControlPointMap.remove(udn);
        }
    }
}
