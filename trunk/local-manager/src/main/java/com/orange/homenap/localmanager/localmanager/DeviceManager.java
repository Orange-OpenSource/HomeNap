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
 *  File Name   : DeviceManager.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.localmanager;

import com.google.gson.Gson;
import com.orange.homenap.services.PowerStateManagerItf;
import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Service;
import org.osgi.framework.*;
import org.osgi.service.event.Event;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeviceManager implements LocalManagerEvent, DeployerEvent, ServiceManagerEventItf
{
    // iPOJO requires
    private ServiceManagerItf serviceManagerItf;
    private PowerStateManagerItf powerStateManagerItf;
    private DeviceInfoItf deviceInfoItf;
    private GlobalCoordinatorControlPointItf globalCoordinatorControlPointItf;
    private ControlPointManagerItf controlPointManagerItf;

    // iPOJO properties
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;

    public DeviceManager(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        try {
            java.util.logging.LogManager.getLogManager().readConfiguration(new ByteArrayInputStream((".level=SEVERE").getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.setProperty("ipojo.log.level", "info");
    }

    public void stop() {}

    public void start(String bundleUrl, String migrationState)
    {
        System.out.println("Starting migration");

        serviceManagerItf.startService(bundleUrl, migrationState);
    }

    public void migrateService(String serviceName, String toDeviceId, String wakeUpAddress)
    {
        System.out.println("Migration action");

        powerStateManagerItf.suspendStateChange();

        DeployerControlPointItf deployerControlPointItf = controlPointManagerItf.createCP(toDeviceId, wakeUpAddress);

        Service service = serviceManagerItf.stopService(serviceName);

        Gson gson = new Gson();

        deployerControlPointItf.start(service.getBundleUrl(), gson.toJson(service.getComponents()));

        powerStateManagerItf.releaseStateChange();

        //TODO: take into account capabilities of the DeviceManager (number of services, ...)
    }

    public void notifyPowerStateChange(Event event)
    {
        System.out.println("Sending state change");

        deviceInfoItf.getDevice().setDeviceState(Device.DeviceState.SLEEP);

        globalCoordinatorControlPointItf.updateDeviceState(
                deviceInfoItf.getDevice().getId(),
                deviceInfoItf.getDevice().getDeviceState());
    }

    public void updateService(Service service)
    {
        deviceInfoItf.getDevice().getServicesState().put(service.getName(), service.getServiceState());

        globalCoordinatorControlPointItf.updateServicesState(
                deviceInfoItf.getDevice().getId(),
                deviceInfoItf.getDevice().getServicesState());
    }
}
