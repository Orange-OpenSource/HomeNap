/*
 * --------------------------------------------------------
 *  Module Name : service-manager
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

package com.orange.homenap.localmanager.bundlemanager;

import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.utils.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class BundleManager implements BundleManagerItf
{
    // iPOJO requires
    private LocalDatabaseItf localDatabaseItf;

    // iPOJO injection
    private BundleContext bundleContext;

    public BundleManager(BundleContext bundleContext)
    {
        this.bundleContext = bundleContext;
    }

    public void start(String url, String migrationState)
    {
        Bundle bundle = null;

        try {
            bundle = bundleContext.installBundle(url);
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

    public Service stop(String serviceName)
    {
        Service service = localDatabaseItf.get(localDatabaseItf.getServiceId(serviceName));

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
