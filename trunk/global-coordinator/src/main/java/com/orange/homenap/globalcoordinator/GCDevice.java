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
* Description : Input of the GC. Listen for pre-defined events in order to start
* reconfiguration.
*
*--------------------------------------------------------
*/

package com.orange.homenap.globalcoordinator;

import com.google.gson.Gson;
import com.orange.homenap.api.IGlobalCoordinatorService;
import com.orange.homenap.globalcoordinator.upnp.devices.GlobalCoordinatorDevice;
import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class GCDevice implements IGlobalCoordinatorService
{
    // iPOJO requires
    private OptimizerItf optimizerItf;
    private PlanItf planItf;
    private ControlPointManagerItf controlPointManagerItf;

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

        planItf.addDevice(device);

        controlPointManagerItf.createCP(device.getId());

        return true;
    }

    public boolean unRegister(String deviceId) throws Exception
    {
        planItf.removeDevice(deviceId);

        controlPointManagerItf.removeCP(deviceId);

        return true;
    }

    public void startService(String serviceInfo, String deviceId) throws Exception
    {
        Gson gson = new Gson();

        Service service = gson.fromJson(serviceInfo, Service.class);

        planItf.addService(service, deviceId);
    }

    public void stopService(String serviceId) throws Exception
    {
        planItf.removeService(serviceId);
    }
}