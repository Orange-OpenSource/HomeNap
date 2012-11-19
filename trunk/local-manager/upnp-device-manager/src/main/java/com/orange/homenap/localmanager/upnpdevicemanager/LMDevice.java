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
 *  File Name   : LMDevice.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.upnpdevicemanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orange.homenap.api.ILocalManagerService;
import com.orange.homenap.gson.*;
import com.orange.homenap.localmanager.bundlemanager.BundleManagerItf;
import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.localmanager.eventlistener.ActionsEvent;
import com.orange.homenap.localmanager.upnp.devices.LocalManagerDevice;
import com.orange.homenap.utils.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LMDevice implements ILocalManagerService
{
    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;
    private ActionsEvent actionsEvent;
    private BundleManagerItf bundleManagerItf;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private ServiceRegistration serviceRegistration;

    public LMDevice(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        // Expose this device via UPnP
        serviceRegistration = LocalManagerDevice.expose(bundleContext, this, null);

        deviceInfoItf.setUDN((String) serviceRegistration.getReference().getProperty("UPnP.device.UDN"));

        System.out.println("My ID is: " + deviceInfoItf.getDevice().getId());

        Action action = new Action();

        action.setActionName(Action.ActionName.REGISTER);

        List<Action> actionList = new ArrayList<Action>();

        actionsEvent.actionsToTake(actionList);
    }

    public void stop()
    {
        System.out.println("Stopping Local Manager Device");

        serviceRegistration.unregister();
    }

    public void start(String bundleUrl, String migrationState) throws Exception
    {
        bundleManagerItf.start(bundleUrl);
    }

    public void actionsToTake(String actions) throws Exception
    {
        System.out.println("Receiving actions to take");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter((new TypeToken<List<Action>>() {}).getType(), new ActionListAdapter());
        Gson gson = gsonBuilder.create();

        List<Action> actionList = gson.fromJson(actions, (new TypeToken<List<Action>>() {}).getType());

        actionsEvent.actionsToTake(actionList);
    }
}
