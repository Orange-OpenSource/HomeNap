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
* File Name   : HomeManager.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.olnc.made.homenap.globalcoordinator;

import com.olnc.made.homenap.utils.Device;
import com.olnc.made.homenap.utils.Service;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class HomeManager implements GlobalCoordinatorEvent
{
    // iPOJO requires
    private DeviceInfoDBItf deviceInfoDBItf;
    private ControlPointManagerItf controlPointManagerItf;

    // iPOJO properties
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;

    public HomeManager(BundleContext bundleContext)
    {
        this.bundleContext = bundleContext;
    }

    public void start()
    {

    }

    public void stop()
    {

    }

    public void newDevice(Device device)
    {
        System.out.println("GC: new device " + device.getId());

        Collection<Device> devices = deviceInfoDBItf.values();

        Iterator<Device> it = devices.iterator();

        boolean migration = false;
        String serviceId = null;

/*        while (it.hasNext())
        {
            Device oldDevice = it.next();

            for(Map.Entry<String, Service.ServiceState> oldE : oldDevice.getServicesState().entrySet())
            {
                for(Map.Entry<String, Service.ServiceState> e : device.getServicesState().entrySet())
                {
                    if(e.getKey().equals(oldE.getKey()))
                    {
                        if(e.getValue().equals(Service.ServiceState.STARTED))
                        {
                            if(device.getConsumption() < oldDevice.getConsumption())
                            {
                                migration = true;
                                serviceId = e.getKey();
                            }
                        }
                    }
                }
            }
        }*/

        if (migration = true)
        {
            LocalManagerControlPointItf localManagerControlPointItf = controlPointManagerItf.createCP(device.getId());

            localManagerControlPointItf.migrateService(serviceId, device.getId(), device.getMac());
        }
    }

    public void servicesStateChange(Device device)
    {
        //TODO
    }

    public void deviceStateChange(Device device)
    {
        System.out.println("GC: " + device.getId() + " going to " + device.getDeviceState() + " mode");

        Collection<Device> devices = deviceInfoDBItf.values();

        Iterator<Device> it = devices.iterator();

        boolean migration = false;
        String serviceId = null;
        
        Device newDevice = null;

        while (it.hasNext())
        {
            Device otherDevice = it.next();

            if (!device.getId().equals(otherDevice.getId()))
            {
                for (Map.Entry<String, Service.ServiceState> e : device.getServicesState().entrySet())
                {
                    //System.out.println(e.getValue().getClass() + " | " + Service.ServiceState.STARTED.getClass());

                    //TODO: fix problem with enum type...
                    if (Service.ServiceState.STARTED.name().equals(e.getValue()))
                    {
                        if (otherDevice.getServicesState().containsKey(e.getKey()))
                        {
                            if (otherDevice.getServicesState().get(e.getKey()).equals(Service.ServiceState.UNINSTALLED))
                            {
                                if (otherDevice.getConsumption() < device.getConsumption())
                                {
                                    newDevice = otherDevice;
                                    serviceId = e.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }

        if (newDevice != null)
        {
            LocalManagerControlPointItf localManagerControlPointItf = controlPointManagerItf.createCP(newDevice.getId());

            localManagerControlPointItf.migrateService(serviceId, newDevice.getId(), newDevice.getMac());
        }
        else
        {
            //localManagerControlPointItf.stopStateChange();
        }
    }

    public void goodbyeDevice(Device device)
    {
        //TODO: process bye bye

        controlPointManagerItf.removeCP(device.getId());
    }
}
