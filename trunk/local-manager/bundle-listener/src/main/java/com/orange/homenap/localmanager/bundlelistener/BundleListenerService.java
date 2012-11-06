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
import com.orange.homenap.utils.Component;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import java.util.Dictionary;

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
        Component component = localDatabaseItf.getComponent(be.getBundle().getSymbolicName());

        switch (be.getType()) {
            case BundleEvent.INSTALLED:
                String bundleLocation = repositoryManagerItf.addBundleToRepository(be.getBundle().getLocation());

                component.setId(be.getBundle().getBundleId());
                component.setUrl(bundleLocation);
                component.setBundleEvent(BundleEvent.INSTALLED);

                stateFileManagerItf.load(component.getName());
                break;

            case BundleEvent.STARTED:
                component.setBundleEvent(BundleEvent.STARTED);
                break;

            case BundleEvent.STOPPED:
                stateFileManagerItf.save(component.getName());
                component.setBundleEvent(BundleEvent.STOPPED);
                break;

            case BundleEvent.UNINSTALLED:
                component.setBundleEvent(BundleEvent.UNINSTALLED);
                break;

            default:
                break;
        }
    }
}
