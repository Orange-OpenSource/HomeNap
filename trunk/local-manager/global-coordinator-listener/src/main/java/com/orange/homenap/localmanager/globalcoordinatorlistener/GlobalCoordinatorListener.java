/*
 * --------------------------------------------------------
 *  Module Name : global-coordinator-listener
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
 *  File Name   : GlobalCoordinatorListener.java
 *
 *  Created     : 06/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.globalcoordinatorlistener;

import com.orange.homenap.localmanager.eventlistener.GlobalCoordinatorEvent;
import org.osgi.framework.*;
import org.osgi.service.upnp.UPnPDevice;

public class GlobalCoordinatorListener implements ServiceListener
{
    private BundleContext bundleContext;

    // iPOJO properties
    private String typeGlobalCoordinator;
    
    private GlobalCoordinatorEvent globalCoordinatorEvent;

    public GlobalCoordinatorListener(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        // Add service listener to GlobalCoordinator
        String filter = "(&" + "(" + Constants.OBJECTCLASS + "=" + UPnPDevice.class.getName() + ")"
                + "(" + UPnPDevice.TYPE + "=" + typeGlobalCoordinator + ")" + ")";

        try {
            bundleContext.addServiceListener(this, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent)
    {
        switch (serviceEvent.getType())
        {
            case ServiceEvent.REGISTERED:
                globalCoordinatorEvent.globalCoordinatorAppear();
                break;
            case ServiceEvent.UNREGISTERING:
                globalCoordinatorEvent.globalCoordinatorDisappear();
                break;
            case ServiceEvent.MODIFIED:
                break;
            default:
                break;
        }
    }
}
