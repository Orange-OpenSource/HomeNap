/*
 * --------------------------------------------------------
 * Module Name : controlpoint-factory
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : $projectName
 * Version : 0.1-SNAPSHOT
 *
 * Copyright © 28/06/2012 – 31/12/2013 France Télécom
 * This software is distributed under the Apache 2.0 license,
 * the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 * or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 * File Name   : ${NAME}
 *
 * Created     :
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.controlpointfactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orange.homenap.localmanager.wol.WakeOnLanItf;
import com.orange.homenap.utils.*;
import org.osgi.framework.*;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;

import java.util.Hashtable;
import java.util.List;

public class LocalManagerControlPoint implements ServiceListener, LocalManagerControlPointItf
{
    // iPOJO properties
    private String udnLocalManager;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private UPnPDevice localManagerDevice;

    // iPOJO requires
    private WakeOnLanItf wakeOnLanItf;

    public LocalManagerControlPoint(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        // Add service listener
        String filter = "(&" + "(" + Constants.OBJECTCLASS + "=" + UPnPDevice.class.getName() + ")"
                + "(" + UPnPDevice.UDN + "=" + udnLocalManager + ")" + ")";

        //System.out.println("Filtering on " + filter);

        try {
            bundleContext.addServiceListener(this, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        ServiceReference sr[] = null;

        try {
            sr = bundleContext.getServiceReferences(UPnPDevice.class.getName(), filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        if(sr != null)
        {
            if(sr.length == 1)
                localManagerDevice = (UPnPDevice) bundleContext.getService(sr[0]);
        }
        else
        {
            if(!udnLocalManager.contains("uuid:"))
            {
                udnLocalManager = "uuid:" + udnLocalManager;
                start();
            }
        }
    }

    public void stop()
    {
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent)
    {
        localManagerDevice = (UPnPDevice) bundleContext.getService(serviceEvent.getServiceReference());

        //System.out.println(localManagerDevice.getDescriptions(null).get("UPnP.device.UDN"));

        switch (serviceEvent.getType())
        {
            case ServiceEvent.REGISTERED:
                break;
            case ServiceEvent.UNREGISTERING:
                localManagerDevice = null;

                break;
            case ServiceEvent.MODIFIED:
                break;
            default:
                break;
        }
    }

    public boolean actions(List<Action> actions)
    {
        if(localManagerDevice != null)
        {
            try {
                UPnPService service = localManagerDevice.getService("urn:upnp-org:serviceId:LocalManager.1");
                UPnPAction action = service.getAction("ActionsToTake");

                Hashtable<String, Object> dico = new Hashtable<String, Object>();

                Gson gson  = new Gson();

                dico.put("Actions", gson.toJson(actions, (new TypeToken<List<Action>>() {}).getType()));

                action.invoke(dico);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
