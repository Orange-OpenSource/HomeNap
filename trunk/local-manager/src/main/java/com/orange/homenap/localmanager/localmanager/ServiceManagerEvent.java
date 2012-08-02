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
 *  File Name   : ServiceManagerEvent.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.localmanager;

import com.orange.homenap.utils.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import java.util.Dictionary;

public class ServiceManagerEvent implements BundleListener
{
    // iPOJO requires
    private ServiceManagerEventItf serviceManagerEventItf;
    private ServiceInfoDBItf serviceInfoDBItf;
    private StateFileManagerItf stateFileManagerItf;

    // iPOJO properties
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;    
    
    public ServiceManagerEvent(BundleContext bc)
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
                service = new Service(be.getBundle().getBundleId(),
                        be.getBundle().getSymbolicName(),
                        be.getBundle().getLocation(),
                        Service.ServiceState.INSTALLED);

                serviceInfoDBItf.put(service.getId(), service);
                serviceManagerEventItf.updateService(service);
                stateFileManagerItf.load(service.getName());
                break;

            case BundleEvent.STARTED:
                service = serviceInfoDBItf.get(be.getBundle().getBundleId());
                service.setServiceState(Service.ServiceState.STARTED);
                serviceManagerEventItf.updateService(service);
                break;

            case BundleEvent.STOPPED:
                service = serviceInfoDBItf.get(be.getBundle().getBundleId());
                service.setServiceState(Service.ServiceState.STOPPED);
                serviceManagerEventItf.updateService(service);
                stateFileManagerItf.save(service.getName());
                break;

            case BundleEvent.UNINSTALLED:
                service = serviceInfoDBItf.get(be.getBundle().getBundleId());
                service.setServiceState(Service.ServiceState.UNINSTALLED);
                serviceManagerEventItf.updateService(service);
                break;

            default:
                break;
        }
    }
}
