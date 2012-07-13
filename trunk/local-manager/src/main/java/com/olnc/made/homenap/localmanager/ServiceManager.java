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
 *  File Name   : ServiceManager.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.olnc.made.homenap.localmanager;

import com.olnc.made.homenap.utils.Service;
import org.osgi.framework.*;

import java.io.*;

public class ServiceManager implements ServiceManagerItf
{
    // iPOJO requires
    private ServiceInfoDBItf serviceInfoDBItf;

    // iPOJO properties
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;

    public ServiceManager(BundleContext bundleContext)
    {
        this.bundleContext = bundleContext;
    }

    protected void start()
    {
        // Attach handler to iPOJO components
        System.setProperty("org.apache.felix.ipojo.handler.auto.primitive", "com.olnc.made.device-state.handler:migration-handler");
    }

    public void startService(String bundleUrl, String migrationState)
    {
        Bundle bundle = null;

        try {
            bundle = bundleContext.installBundle(bundleUrl);
        } catch (BundleException e) {
            e.printStackTrace();
        }

        System.out.println("Bundle " + bundle.getSymbolicName() + " installed");

        try {
            bundle.start();
        } catch (BundleException e) {
            e.printStackTrace();
        }

        System.out.println("Bundle " + bundle.getSymbolicName() + " started");
    }

    public Service stopService(String serviceName)
    {
        Service service = serviceInfoDBItf.get(serviceInfoDBItf.getServiceId(serviceName));

        Bundle bundle = bundleContext.getBundle(service.getId());

        try {
            bundle.stop();
        } catch (BundleException e) {
            e.printStackTrace();
        }

        System.out.println("Bundle " + service.getName() + " stopped");

        try {
            bundle.uninstall();
        } catch (BundleException e) {
            e.printStackTrace();
        }

        System.out.println("Bundle " + service.getName() + " uninstalled");

        return service;
    }
}
