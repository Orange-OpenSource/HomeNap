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

package com.orange.homenap.globalcoordinator.globaldatabase;

import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GlobalDatabase implements GlobalDatabaseItf
{
    // iPOJO properties
    private List<Device> devices;
    private List<Architecture> architectures;
    private List<Component> components;
    private List<String> resources;

    public GlobalDatabase() {}

    public void start()
    {
        System.out.println("Devices size: " + devices.size());
        System.out.println("Resources size: " + resources.size());
        System.out.println("Architectures size: " + architectures.size());
        System.out.println("Components size: " + components.size());

        Device device = new Device();

        device.setId("VirtualDevice");
        device.setConsumptionOff(0);
        device.setConsumptionOnMin(500);
        device.setConsumptionOnMax(1000);

        List<Resource> resources = new ArrayList<Resource>();

        Resource cpu = new Resource();
        cpu.setName("CPU");
        cpu.setValue(1);

        resources.add(cpu);

        device.setResources(resources);

        addDevice(device);
    }

    public void addDevice(Device device)
    {
        boolean deviceRegistered = false;

        if (!devices.isEmpty())
        {
            for(Device tmpDevice : devices)
                if(tmpDevice.getId().equals(device.getId()))
                    deviceRegistered = true;
        }

        if(deviceRegistered)
            System.out.println("Device already registered");
        else
        {
            System.out.println("Adding device " + device.getId());

            devices.add(device);

            if(!device.getResources().isEmpty())
                addRessources(device.getResources());
        }
    }

    public void removeDevice(String id)
    {
        System.out.println("Removing device " + id);

        for(Device device : devices)
            if(device.getId().equals(id))
                devices.remove(device);
    }

    public void addArchitecture(Architecture architecture)
    {
        System.out.println("Adding architecture " + architecture.getName());

        architectures.add(architecture);
    }

    public void removeArchitecture(String name)
    {
        System.out.println("Removing architecture " + name);

        for(Architecture architecture : architectures)
            if(architecture.getName().equals(name))
                architectures.remove(architecture);
    }

    public void addComponent(Component component)
    {
        System.out.println("Adding component " + component.getName());

        components.add(component);

        addRessources(component.getResources());
    }

    public void removeComponent(String name)
    {
        System.out.println("Removing component " + name);

        for(Component component : components)
            if(component.getName().equals(name))
                components.remove(component);
    }

    private void addRessources(List<Resource> resourceList)
    {
        for(Resource resource : resourceList)
            if (!resources.contains(resource.getName()))
                resources.add(resource.getName());
    }

    public void addComponentOnDevice(String name, String deviceId)
    {
        for(Device device : devices)
            if(device.getId().equals(deviceId))
                device.addComponent(name);
    }

    public Component getComponent(int i) { return components.get(i); }

    public int getComponentsSize() { return components.size(); }

    public Device getDevice(int i) { return devices.get(i); }

    public int getDevicesSize() { return devices.size(); }

    public String getResource(int i) { return resources.get(i); }

    public int getResourcesSize() { return resources.size(); }

    public List<Device> getDevices() { return devices; }

    public void setDevices(List<Device> devices) { this.devices = devices; }

    public List<Architecture> getArchitectures() { return architectures; }

    public void setArchitectures(List<Architecture> architectures) { this.architectures = architectures; }

    public List<Component> getComponents() { return components; }

    public void setComponents(List<Component> components) { this.components = components; }

    public List<String> getResources() { return resources; }

    public void setResources(List<String> resources) { this.resources = resources; }

    public Component getComponentByName(String name)
    {
        Component component = null;

        Iterator<Component> it = components.iterator();

        while(it.hasNext())
        {
            Component tmp = it.next();

            if(tmp.getName().equals(name))
                component = tmp;
        }

        return component;
    }

/*    public Architecture getParent(Component component)
    {
        Architecture architecture = null;

        Iterator<Architecture> itArchi = architectures.iterator();

        while(itArchi.hasNext())
        {
            architecture = itArchi.next();

            Iterator<Component> itComponent = architecture.getComponents().iterator();

            boolean rightOne = false;

            while(itComponent.hasNext())
            {
                Component temp = itComponent.next();

                if(component.getName().equals(temp.getName()))
                    rightOne = true;
                    break;
            }

            if (rightOne)
                break;
        }

        return architecture;
    }*/
}