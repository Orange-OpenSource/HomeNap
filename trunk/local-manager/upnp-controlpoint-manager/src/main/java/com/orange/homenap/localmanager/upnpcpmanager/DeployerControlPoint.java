/*
 * --------------------------------------------------------
 *  Module Name : upnp-manager
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
 *  File Name   : DeployerControlPoint.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.upnpcpmanager;

import com.orange.homenap.localmanager.wol.WakeOnLanItf;
import org.osgi.framework.*;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;

import java.util.Hashtable;

public class DeployerControlPoint implements ServiceListener, DeployerControlPointItf
{
    // iPOJO requires
    private WakeOnLanItf wakeOnLanItf;

    // iPOJO properties
    private String udnDeployer;
    private String wakeUpAddress;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private boolean remoteDeviceOn;
    private UPnPDevice deployer;

    public DeployerControlPoint(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        // Add service listener
        String filter = "(&" + "(" + Constants.OBJECTCLASS + "=" + UPnPDevice.class.getName() + ")"
                + "(" + UPnPDevice.UDN + "=" + udnDeployer + ")" + ")";

        try {
            bundleContext.addServiceListener(this, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        remoteDeviceOn = false;

        wakeOnLanItf.wakeUp(wakeUpAddress);

/*        ServiceReference sr[] = null;

        try {
            sr = bundleContext.getServiceReferences(UPnPDevice.class.getName(), filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        if(sr != null)
            if(sr.length == 1)
                deployer = (UPnPDevice) bundleContext.getService(sr[0]);*/
    }

    public void stop()
    {
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent)
    {
        deployer = (UPnPDevice) bundleContext.getService(serviceEvent.getServiceReference());

        System.out.println(deployer.getDescriptions(null).get("UPnP.device.UDN"));

        switch (serviceEvent.getType())
        {
            case ServiceEvent.REGISTERED:
                remoteDeviceOn = true;
                break;
            case ServiceEvent.UNREGISTERING:
                remoteDeviceOn = false;
                deployer = null;
                break;
            default:
                break;
        }
    }

    public void start(String bundleUrl, String migrationState)
    {
        if(deployer != null && remoteDeviceOn)
        {
            try {
                UPnPService service = deployer.getService("urn:upnp-org:serviceId:Deployer.1");
                UPnPAction action = service.getAction("Start");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                dico.put("BundleUrl", bundleUrl);
                dico.put("MigrationState", migrationState);

                action.invoke(dico);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Unable to migrate " + bundleUrl);
        }
    }
}
