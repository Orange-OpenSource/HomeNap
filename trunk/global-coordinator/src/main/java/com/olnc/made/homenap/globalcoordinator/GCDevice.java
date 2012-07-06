/*--------------------------------------------------------
* Module Name : global-coordinator
* Version : 0.1-SNAPSHOT
*
* Software Name : HomeNap
* Version : 0.1-SNAPSHOT
*
* Copyright © 28/06/2012 – 28/06/2012 France Télécom
* This software is distributed under the Apache 2.0 license,
* the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
* or see the "LICENSE-2.0.txt" file for more details.
*
*--------------------------------------------------------
* File Name   : GCDevice.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.olnc.made.homenap.globalcoordinator;

import com.google.gson.Gson;
import com.olnc.made.homenap.api.IGlobalCoordinatorService;
import com.olnc.made.homenap.globalcoordinator.upnp.devices.GlobalCoordinatorDevice;
import com.olnc.made.homenap.utils.Device;
import com.olnc.made.homenap.utils.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.HashMap;
import java.util.Map;

public class GCDevice implements IGlobalCoordinatorService
{
    // iPOJO requires
    private DeviceInfoDBItf deviceInfoDBItf;
    private GlobalCoordinatorEvent globalCoordinatorEvent;

    // iPOJO properties
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private ServiceRegistration serviceRegistration;

    public GCDevice(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        // Expose on network via UPnP
        serviceRegistration = GlobalCoordinatorDevice.expose(bundleContext, this, null);
    }

    public void stop()
    {
        serviceRegistration.unregister();
    }

    public boolean register(String deviceInfo) throws Exception
    {
        Gson gson = new Gson();

        System.out.println(deviceInfo);

        Device device = gson.fromJson(deviceInfo, Device.class);

        deviceInfoDBItf.put(device.getId(), device);

        //controlPointManagerItf.createCP(device.getId());

        System.out.println("Device " + device.getId() + " registered");

        globalCoordinatorEvent.newDevice(device);

        return true;
    }

    public boolean unRegister(String deviceId) throws Exception
    {
        if(deviceInfoDBItf.containsKey(deviceId))
        {
            deviceInfoDBItf.remove(deviceId);

            //controlPointManagerItf.removeCP(deviceId);

            globalCoordinatorEvent.goodbyeDevice(deviceInfoDBItf.get(deviceId));
        }

        return true;
    }

    public void updateServicesState(String deviceId, String servicesState) throws Exception
    {
        Gson gson = new Gson();

        System.out.println(deviceId + " | " + servicesState);

        deviceInfoDBItf.get(deviceId).setServicesState(gson.fromJson(servicesState, HashMap.class));

        globalCoordinatorEvent.servicesStateChange(deviceInfoDBItf.get(deviceId));
    }

    public void updateDeviceState(String deviceId, String state) throws Exception
    {
        if(state.equals("ON"))
            deviceInfoDBItf.get(deviceId).setDeviceState(Device.DeviceState.ON);
        else if(state.equals("SLEEP"))
            deviceInfoDBItf.get(deviceId).setDeviceState(Device.DeviceState.SLEEP);
        else if(state.equals("HIBERNATE"))
            deviceInfoDBItf.get(deviceId).setDeviceState(Device.DeviceState.HIBERNATE);
        else if(state.equals("OFF"))
            deviceInfoDBItf.get(deviceId).setDeviceState(Device.DeviceState.OFF);
        else
            System.out.println("State " + state + " doesn't exist");

        globalCoordinatorEvent.deviceStateChange(deviceInfoDBItf.get(deviceId));
    }
}
