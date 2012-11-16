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

package com.orange.homenap.globalcoordinator.upnpdevicemanager;

import com.google.gson.Gson;
import com.orange.homenap.api.IGlobalCoordinatorService;
import com.orange.homenap.globalcoordinator.analyser.AnalyserItf;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.globalcoordinator.upnp.devices.GlobalCoordinatorDevice;
import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Device;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class GCDevice implements IGlobalCoordinatorService
{
    // iPOJO requires
    private AnalyserItf analyserItf;
    private GlobalDatabaseItf globalDatabaseItf;

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
        System.out.println("Stopping Global Coordinator Device");

        serviceRegistration.unregister();
    }

    public boolean register(String deviceInfo) throws Exception
    {
        System.out.println(deviceInfo);

        Gson gson = new Gson();
        
        Device device = gson.fromJson(deviceInfo, Device.class);
        
        if(device.getId() == null)
            return false;

        globalDatabaseItf.addDevice(device);

        analyserItf.analyse();

        return true;
    }

    public boolean unRegister(String deviceId) throws Exception
    {
        globalDatabaseItf.removeDevice(deviceId);

        analyserItf.analyse();

        return true;
    }

    public void startService(String architectureInfo, String deviceId) throws Exception
    {
        Gson gson = new Gson();

        Architecture architecture = gson.fromJson(architectureInfo, Architecture.class);

        globalDatabaseItf.addArchitecture(architecture);

        analyserItf.analyse();

        //System.out.println(gsonServiceItf.toJson(service));
    }

    public void stopService(String architectureName) throws Exception
    {
        globalDatabaseItf.removeArchitecture(architectureName);

        analyserItf.analyse();
    }
}
