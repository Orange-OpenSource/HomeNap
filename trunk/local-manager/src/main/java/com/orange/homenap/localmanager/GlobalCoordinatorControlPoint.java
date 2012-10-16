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
 *  File Name   : GlobalCoordinatorControlPoint.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager;

import com.orange.homenap.services.deviceinfo.DeviceInfoItf;
import com.orange.homenap.services.json.GsonServiceItf;
import com.orange.homenap.utils.Service;
import com.orange.homenap.utils.Device;
import org.osgi.framework.*;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class GlobalCoordinatorControlPoint implements ServiceListener, GlobalCoordinatorControlPointItf
{
    // TODO: iPOJO properties ?
    //private String UDN_GLOBAL_COORDINATOR = "uuid:FTRD-MAPS-GlobalCoordinator-UPnPGEN";
    //private String TYPE_GLOBAL_COORDINATOR = "urn:schemas-upnp-org:device:GlobalCoordinator:1";

    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;
    private GsonServiceItf gsonServiceItf;

    // iPOJO properties
    private String typeGlobalCoordinator;
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private UPnPDevice globalCoordinator;

    public GlobalCoordinatorControlPoint(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        // Add service listener to GlobalCoordinator
        String globalCoordinatorFilter = "(&" + "(" + Constants.OBJECTCLASS + "=" + UPnPDevice.class.getName() + ")"
                //+ "(" + UPnPDevice.UDN + "=" + UDN_GLOBAL_COORDINATOR + ")" + ")";
                + "(" + UPnPDevice.TYPE + "=" + typeGlobalCoordinator + ")" + ")";

        //System.out.println("Filtering on " + globalCoordinatorFilter);

        try {
            bundleContext.addServiceListener(this, globalCoordinatorFilter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        ServiceReference sr[] = null;

        try {
            sr = bundleContext.getServiceReferences(UPnPDevice.class.getName(), globalCoordinatorFilter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        if(sr != null)
        {
            if(sr.length == 1)
            {
                globalCoordinator = (UPnPDevice) bundleContext.getService(sr[0]);

                try {
                    UPnPService service = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                    UPnPAction action = service.getAction("Register");

                    Hashtable<String, Object> dico = new Hashtable<String, Object>();

                    dico.put("DeviceInfo", gsonServiceItf.toJson(deviceInfoItf.getDevice()));

                    action.invoke(dico);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                System.out.println("Multiple GlobalCoordinator! Error!");
        }
    }

    public void stop()
    {
        // UnRegister from GlobalCoordinator
        if (globalCoordinator != null)
        {
            try {
                UPnPService service = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                UPnPAction action = service.getAction("UnRegister");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                dico.put("DeviceId", deviceInfoItf.getDevice().getId());

                Dictionary<String, Object> result = action.invoke(dico);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent)
    {
        globalCoordinator = (UPnPDevice) bundleContext.getService(serviceEvent.getServiceReference());

        switch (serviceEvent.getType())
        {
            case ServiceEvent.REGISTERED:
                try {
                    UPnPService service = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                    UPnPAction action = service.getAction("Register");

                    Hashtable<String, Object> dico = new Hashtable<String, Object>();

                    dico.put("DeviceInfo", gsonServiceItf.toJson(deviceInfoItf.getDevice()));

                    action.invoke(dico);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case ServiceEvent.UNREGISTERING:
                globalCoordinator = null;

                break;
            case ServiceEvent.MODIFIED:
                break;
            default:
                break;
        }
    }

    public void updateServicesState(String deviceId, Map<String, Service.ServiceState> servicesState)
    {
        System.out.println("Updating services state");

        if (globalCoordinator != null)
        {
            try {
                UPnPService uPnPService = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                UPnPAction action = uPnPService.getAction("UpdateServicesState");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                dico.put("DeviceId", deviceId);
                dico.put("ServicesState", gsonServiceItf.toJson(servicesState));

                action.invoke(dico);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            System.out.println("GlobalCoordinator not connected");
    }

    public void updateDeviceState(String deviceId, Device.DeviceState state)
    {
        System.out.println("Updating device state to " + state);

        if (globalCoordinator != null)
        {
            try {
                UPnPService service = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                UPnPAction action = service.getAction("UpdateDeviceState");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                dico.put("DeviceId", deviceId);
                dico.put("State", state.toString());

                action.invoke(dico);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            System.out.println("GlobalCoordinator not connected");
    }
}
