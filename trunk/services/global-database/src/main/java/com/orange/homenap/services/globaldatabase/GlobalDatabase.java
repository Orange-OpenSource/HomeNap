/*--------------------------------------------------------
* Module Name : global-database
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
* File Name   : GlobalDatabase.java
*
* Created     : 30/10/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.services.globaldatabase;

import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GlobalDatabase implements GlobalDatabaseItf
{
    // iPOJO properties
    private List<Device> devices;
    private List<Service> services;
    private List<String> resources;

    public void addDevice(Device device)
    {
        System.out.println("Adding device: " + device.getId());

        devices.add(device);

        for (Map.Entry<String,Integer> map : device.getResources().entrySet())
            if (!resources.contains(map.getKey()))
                resources.add(map.getKey());
    }

    public void addService(Service service, String deviceId)
    {
        System.out.println("Adding service: " + service.getId() + " on " + deviceId);

        services.add(service);

        for (Map.Entry<String,Integer> map : service.getResources().entrySet())
            if (!resources.contains(map.getKey()))
                resources.add(map.getKey());

        Iterator<Device> iterator = devices.iterator();

        while(iterator.hasNext())
        {
            Device device = iterator.next();

            if(device.getId().equals(deviceId))
                device.addService(service);
        }
    }

    public void removeDevice(String deviceId)
    {
        System.out.println("Removing device: " + deviceId);

        Iterator<Device> iterator = devices.iterator();

        while(iterator.hasNext())
        {
            Device tmp = iterator.next();

            if(tmp.getId().equals(deviceId))
                iterator.remove();
        }
    }

    public void removeService(String serviceId)
    {
        System.out.println("Removing service: " + serviceId);

        Iterator<Service> servIterator = services.iterator();
        Service service = null;

        while(servIterator.hasNext())
        {
            Service tmp = servIterator.next();

            if(tmp.getId().equals(serviceId))
            {
                service = tmp;
                servIterator.remove();
            }
        }

        Iterator<Device> devIterator = devices.iterator();

        while(devIterator.hasNext())
        {
            Device device = devIterator.next();

            device.getServicesOnDevice().remove(service);
        }
    }

    public Service getService(int i) { return services.get(i); }

    public int getServicesSize() { return services.size(); }

    public Device getDevice(int i) { return devices.get(i); }

    public int getDevicesSize() { return devices.size(); }

    public String getResource(int i) { return resources.get(i); }

    public int getResourcesSize() { return resources.size(); }
}
