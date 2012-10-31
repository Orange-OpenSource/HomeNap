/*
 * --------------------------------------------------------
 *  Module Name : bundle-listener
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
 *  File Name   : ServiceManagerEvent.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.bundlelistener;

import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.localmanager.migrationservice.StateFileManagerItf;
import com.orange.homenap.localmanager.repositorymanager.RepositoryManagerItf;
import com.orange.homenap.utils.Service;
import org.apache.felix.framework.util.manifestparser.ManifestParser;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;

public class BundleListenerService implements BundleListener
{
    // iPOJO requires
    private LocalDatabaseItf localDatabaseItf;
    private StateFileManagerItf stateFileManagerItf;
    private RepositoryManagerItf repositoryManagerItf;

    // iPOJO injection
    private BundleContext bundleContext;

    public BundleListenerService(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        bundleContext.addBundleListener(this);
    }

    public void stop()
    {
        bundleContext.removeBundleListener(this);
    }

    public void bundleChanged(BundleEvent be)
    {
        Dictionary<String, String> dico = be.getBundle().getHeaders();
        Service service = null;

        switch (be.getType()) {
            case BundleEvent.INSTALLED:
                String bundleLocation = repositoryManagerItf.addBundleToRepository(be.getBundle().getLocation());

                System.out.println("BundleLocation: " + bundleLocation);

                service = new Service(be.getBundle().getBundleId(),
                        be.getBundle().getSymbolicName(),
                        bundleLocation,
                        Service.BundleState.INSTALLED);

                Service.ServiceDeployment serviceDeployment = Service.ServiceDeployment.valueOf(be.getBundle().getHeaders().get("Deployment").toUpperCase());
                Service.ServiceMigrability serviceMigrability = Service.ServiceMigrability.valueOf(be.getBundle().getHeaders().get("Migration").toUpperCase());
                Service.ServiceState serviceState = Service.ServiceState.valueOf(be.getBundle().getHeaders().get("State").toUpperCase());
                Service.Execution execution = Service.Execution.valueOf(be.getBundle().getHeaders().get("Execution").toUpperCase());

                service.setServiceDeployment(serviceDeployment);
                service.setServiceMigrability(serviceMigrability);
                service.setServiceState(serviceState);
                service.setExecution(execution);

                localDatabaseItf.put(service.getId(), service);
                stateFileManagerItf.load(service.getName());
                break;

            case BundleEvent.STARTED:
                service = localDatabaseItf.get(be.getBundle().getBundleId());
                service.setBundleState(Service.BundleState.STARTED);
                localDatabaseItf.put(service.getId(), service);
                break;

            case BundleEvent.STOPPED:
                service = localDatabaseItf.get(be.getBundle().getBundleId());
                service.setBundleState(Service.BundleState.STOPPED);
                localDatabaseItf.put(service.getId(), service);
                stateFileManagerItf.save(service.getName());
                break;

            case BundleEvent.UNINSTALLED:
                service = localDatabaseItf.get(be.getBundle().getBundleId());
                service.setBundleState(Service.BundleState.UNINSTALLED);
                localDatabaseItf.put(service.getId(), service);
                break;

            default:
                break;
        }
    }
}
