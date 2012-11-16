/*
 * --------------------------------------------------------
 * Module Name : controlpoint-factory
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : $projectName
 * Version : 0.1-SNAPSHOT
 *
 * Copyright © 28/06/2012 – 31/12/2013 France Télécom
 * This software is distributed under the Apache 2.0 license,
 * the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 * or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 * File Name   : ${NAME}
 *
 * Created     :
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.controlpointfactory;

import org.apache.felix.ipojo.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ControlPointFactory implements ControlPointFactoryItf
{
    // iPOJO requires
    private Factory factory;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private Map<String, ComponentInstance> lMcontrolPointMap;
    private int instanceNumber;

    public ControlPointFactory(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        lMcontrolPointMap = new HashMap<String, ComponentInstance>();
        instanceNumber = 0;
    }

    public LocalManagerControlPointItf createCP(String id)
    {
        ComponentInstance localManagerInstance = null;

        if (!lMcontrolPointMap.containsKey(id))
        {
            System.out.println("Creating CP: " + id);

            Properties properties = new Properties();

            properties.put("instance.name","local-manager-control-point-" + instanceNumber);
            properties.put("udnLocalManager", id);

            try {
                localManagerInstance = factory.createComponentInstance(properties);
            } catch(UnacceptableConfiguration e) {
                e.printStackTrace();
            } catch(ConfigurationException e) {
                e.printStackTrace();
            } catch(MissingHandlerException e) {
                e.printStackTrace();
            }

            lMcontrolPointMap.put(id, localManagerInstance);
            instanceNumber++;
        }
        else
        {
            localManagerInstance = lMcontrolPointMap.get(id);
        }

        LocalManagerControlPointItf localManagerControlPointItf = null;

        try {
            ServiceReference[] refs = bundleContext.getServiceReferences(LocalManagerControlPointItf.class.getName(),
                    "(instance.name=" + localManagerInstance.getInstanceName() +")");

            if (refs != null)
                localManagerControlPointItf = (LocalManagerControlPointItf) bundleContext.getService(refs[0]);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        return localManagerControlPointItf;
    }

    public void removeCP(String udn)
    {
        System.out.println("Removing CP: " + udn);

        if (lMcontrolPointMap.containsKey(udn))
        {
            ComponentInstance localManagerInstance = lMcontrolPointMap.get(udn);

            localManagerInstance.stop();

            localManagerInstance.dispose();

            lMcontrolPointMap.remove(udn);
            instanceNumber--;
        }
    }
}
