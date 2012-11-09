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
import com.google.gson.reflect.TypeToken;
import com.orange.homenap.api.IDeployerService;
import com.orange.homenap.api.ILocalManagerService;
import com.orange.homenap.localmanager.bundlemanager.BundleManagerItf;
import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.localmanager.eventlistener.MigrationEvent;
import com.orange.homenap.localmanager.upnp.devices.LocalManagerDevice;
import com.orange.homenap.utils.Action;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.lang.reflect.Type;
import java.util.List;

public class LMDevice implements IDeployerService, ILocalManagerService
{
    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;
    private MigrationEvent migrationEvent;
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
        serviceRegistration = LocalManagerDevice.expose(bundleContext, this, this, null);

        deviceInfoItf.setUDN((String) serviceRegistration.getReference().getProperty("UPnP.device.UDN"));
    }

    public void stop()
    {
        serviceRegistration.unregister();
    }

    public void start(String bundleUrl, String migrationState) throws Exception
    {
        bundleManagerItf.start(bundleUrl);
    }

    public void actionsToTake(String actions) throws Exception
    {
        Gson gson = new Gson();

        //TODO: fix
        Type listType = new TypeToken<List<Action>>(){}.getType();

        List<Action> actionList = gson.fromJson(actions, listType);
        //ActionList actionList = gson.fromJson(actions, ActionList.class);
        
        //migrationEvent.actionsToTake(actionList.getActions());
        //migrationEvent.actionsToTake(actionList);
    }
/*
    public class ActionList
    {
        private List<Action> actions;

        public ActionList() {}

        public List<Action> getActions() { return actions; }

        public void setActions(List<Action> actions) { this.actions = actions; }
    }*/
}
