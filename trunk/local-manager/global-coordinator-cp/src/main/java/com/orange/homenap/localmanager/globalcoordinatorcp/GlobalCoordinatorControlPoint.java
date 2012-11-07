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
 *  File Name   : GlobalCoordinatorControlPoint.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.globalcoordinatorcp;

import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.localmanager.json.GsonServiceItf;
import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Device;
import org.osgi.framework.*;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;

import java.util.*;

public class GlobalCoordinatorControlPoint implements ServiceListener, GlobalCoordinatorControlPointItf
{
    // TODO: iPOJO properties ?
    //private String UDN_GLOBAL_COORDINATOR = "uuid:FTRD-MAPS-GlobalCoordinator-UPnPGEN";

    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;
    private GsonServiceItf gsonServiceItf;

    // iPOJO properties
    private String typeGlobalCoordinator;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private UPnPDevice globalCoordinator;
    private List<Architecture> tempArchitecture;

    public GlobalCoordinatorControlPoint(BundleContext bc)
    {
        this.bundleContext = bc;
        tempArchitecture = new ArrayList<Architecture>();
    }

    public void start()
    {
        // Add service listener to GlobalCoordinator
        String globalCoordinatorFilter = "(&" + "(" + Constants.OBJECTCLASS + "=" + UPnPDevice.class.getName() + ")"
                + "(" + UPnPDevice.TYPE + "=" + typeGlobalCoordinator + ")" + ")";

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
                    
                    if(!tempArchitecture.isEmpty())
                    {
                        System.out.println("Sending architecture stored locally");

                        Iterator<Architecture> it = tempArchitecture.iterator();

                        while(it.hasNext())
                        {
                            startArchitecture(it.next());

                            it.remove();
                        }
                    }
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

    public boolean startArchitecture(Architecture architecture)
    {
        System.out.println("Sending architecture " + architecture.getName() + " to GC");

        if (gcExists())
        {
            try {
                UPnPService uPnPService = globalCoordinator.getService("urn:upnp-org:serviceId:GlobalCoordinator.1");
                UPnPAction action = uPnPService.getAction("StartService");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                dico.put("ServiceInfo", gsonServiceItf.toJson(architecture));
                dico.put("DeviceId", deviceInfoItf.getDevice().getId());

                action.invoke(dico);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
        else
        {
            System.out.println("GlobalCoordinator not connected! Storing architecture locally");

            tempArchitecture.add(architecture);

            return false;
        }
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
        if(globalCoordinator != null)
            return true;
        else
            return false;
    }
}
