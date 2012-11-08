/*
 * --------------------------------------------------------
 *  Module Name : event-listener
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
 *  File Name   : EventListener.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.eventlistener;

import com.orange.homenap.localmanager.bundlemanager.BundleManagerItf;
import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.localmanager.globalcoordinatorcp.GlobalCoordinatorControlPointItf;
import com.orange.homenap.localmanager.json.GsonServiceItf;
import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.localmanager.powerstate.PowerStateManagerItf;
import com.orange.homenap.localmanager.upnpcpmanager.ControlPointManagerItf;
import com.orange.homenap.localmanager.upnpcpmanager.DeployerControlPointItf;
import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Device;
import org.osgi.service.event.Event;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventListener implements MigrationEvent, ArchitectureEvent, GlobalCoordinatorEvent
{
    // iPOJO requires
    private BundleManagerItf bundleManagerItf;
    private PowerStateManagerItf powerStateManagerItf;
    private DeviceInfoItf deviceInfoItf;
    private GlobalCoordinatorControlPointItf globalCoordinatorControlPointItf;
    private ControlPointManagerItf controlPointManagerItf;
    private GsonServiceItf gsonServiceItf;
    private LocalDatabaseItf localDatabaseItf;

    private List<Architecture> architectures;

    public void start()
    {
        try {
            java.util.logging.LogManager.getLogManager().readConfiguration(new ByteArrayInputStream((".level=SEVERE").getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.setProperty("ipojo.log.level", "info");

        architectures = new ArrayList<Architecture>();
    }

    public void stop() {}

    public void startArchitecture(Architecture architecture)
    {
        boolean success = globalCoordinatorControlPointItf.startArchitecture(architecture);

        if(!success)
        {
            architectures.add(architecture);

            Iterator<Component> it = architecture.getComponent().iterator();

            while(it.hasNext())
            {
                Component component = it.next();

                localDatabaseItf.put(component.getName(), component);

                bundleManagerItf.start(component.getUrl());

                deviceInfoItf.getDevice().getComponentsOnDevice().add(component.getName());
            }
        }
    }

    public void stopArchitecture(String name)
    {
        globalCoordinatorControlPointItf.stopArchitecture(name);

        //localDatabaseItf.remove(architectureName);

/*        Iterator<Component> it = architecture.getComponent().iterator();

        boolean success = true;

        while(it.hasNext())
            success = success & bundleManagerItf.stop(it.next().getName());

        if(success)
        {
            architecture.setStatus(Architecture.Status.STOPPED);

            architectureEvent.architectureStopped(architecture);
        }*/
    }

    public void architectureStarted(Architecture architecture)
    {
        globalCoordinatorControlPointItf.startArchitecture(architecture);
    }

    public void architectureStopped(Architecture architecture)
    {

    }

    public void globalCoordinatorAppear()
    {
        globalCoordinatorControlPointItf.register();

        // Sending architecture because LM was offline
        if(!architectures.isEmpty())
        {
            Iterator<Architecture> it = architectures.iterator();

            while(it.hasNext())
            {
                globalCoordinatorControlPointItf.startArchitecture(it.next());

                it.remove();
            }
        }
    }

    public void globalCoordinatorDisappear()
    {
        //TODO
    }

    public void migrateComponent(String componentName, String toDeviceId, String wakeUpAddress)
    {
        System.out.println("Migration action");

        powerStateManagerItf.suspendStateChange();

        DeployerControlPointItf deployerControlPointItf = controlPointManagerItf.createCP(toDeviceId, wakeUpAddress);

        Component component = localDatabaseItf.get(componentName);

        deployerControlPointItf.start(component.getUrl(), gsonServiceItf.toJson(component.getProperties()));

        powerStateManagerItf.releaseStateChange();

        //TODO: take into account capabilities of the EventListener (number of services, ...)
    }

    public void notifyPowerStateChange(Event event)
    {
        System.out.println("Sending state change");

        deviceInfoItf.getDevice().setDeviceState(Device.DeviceState.SLEEP);

        globalCoordinatorControlPointItf.updateDeviceState(
                deviceInfoItf.getDevice().getId(),
                deviceInfoItf.getDevice().getDeviceState());
    }
}
