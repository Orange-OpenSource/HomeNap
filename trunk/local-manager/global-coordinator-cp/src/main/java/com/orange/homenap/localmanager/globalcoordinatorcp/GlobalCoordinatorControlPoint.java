/*
 * --------------------------------------------------------
 *  Module Name : global-coordinator-cp
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
 *  Created     : 06/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.globalcoordinatorcp;

import com.google.gson.Gson;
import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Device;
import org.osgi.framework.*;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;

import java.util.*;

public class GlobalCoordinatorControlPoint implements GlobalCoordinatorControlPointItf
{
    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;

    // iPOJO properties
    private String typeGlobalCoordinator;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private UPnPDevice globalCoordinator;

    public GlobalCoordinatorControlPoint(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void stop()
    {
        unRegister();
    }

    public void register()
    {
        System.out.println("Registering: " + deviceInfoItf.getDevice().getId());

        // Register to GlobalCoordinator
        if (gcExists())
        {
            try {
                UPnPService service = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                UPnPAction action = service.getAction("Register");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                Gson gson = new Gson();

                dico.put("DeviceInfo", gson.toJson(deviceInfoItf.getDevice()));

                Dictionary<String, Boolean> result = action.invoke(dico);

                /*System.out.println("Registration success: " + result.get("Success"));

                if(!result.get("Success"))
                {
                    //TODO increment

                    Thread.sleep(1000);

                    register();
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void unRegister()
    {
        System.out.println("Unregistering Local Manager");

        // UnRegister from GlobalCoordinator
        if (gcExists())
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
    }

    public boolean startArchitecture(Architecture architecture)
    {
        System.out.println("Sending architecture " + architecture.getName() + " to GC");

        if (gcExists())
        {
            try {
                UPnPService uPnPService = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                UPnPAction action = uPnPService.getAction("StartService");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                Gson gson = new Gson();

                dico.put("ServiceInfo", gson.toJson(architecture));
                dico.put("DeviceId", deviceInfoItf.getDevice().getId());

                action.invoke(dico);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        System.out.println("Global Coordinator is not available");

        return false;
    }

    public void stopArchitecture(String name)
    {
        System.out.println("Updating services state");

        if (globalCoordinator != null)
        {
            try {
                UPnPService uPnPService = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                UPnPAction action = uPnPService.getAction("StopService");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                dico.put("ServiceId", name);
                dico.put("DeviceId", deviceInfoItf.getDevice().getId());

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

    private boolean gcExists()
    {
        String filter = "(&" + "(" + Constants.OBJECTCLASS + "=" + UPnPDevice.class.getName() + ")"
                + "(" + UPnPDevice.TYPE + "=" + typeGlobalCoordinator + ")" + ")";

        ServiceReference sr[] = null;

        try {
            sr = bundleContext.getServiceReferences(UPnPDevice.class.getName(), filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        if(sr != null)
            if(sr.length == 1)
                globalCoordinator = (UPnPDevice) bundleContext.getService(sr[0]);
            else
                System.out.println("Multiple GlobalCoordinator! Error!");

        if(globalCoordinator != null)
            return true;
        else
            return false;
    }
}

