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

import com.orange.homenap.api.IDeployerService;
import com.orange.homenap.api.ILocalManagerService;
import com.orange.homenap.localmanager.bundlemanager.BundleManagerItf;
import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.localmanager.eventlistener.MigrationEvent;
import com.orange.homenap.localmanager.upnp.devices.LocalManagerDevice;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

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

    public void migrateService(String componentName, String toDeviceId, String wakeUpAddress) throws Exception
    {
        migrationEvent.migrateComponent(componentName, toDeviceId, wakeUpAddress);
    }
}
