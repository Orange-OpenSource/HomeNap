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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GlobalDatabase implements GlobalDatabaseItf
{
    // iPOJO properties
    private List<Device> devices;
    private List<Architecture> architectures;
    private List<Component> components;
    private List<String> resources;

    public GlobalDatabase() {}

    public void addDevice(Device device)
    {
        System.out.println("Adding device " + device.getId());

        devices.add(device);

        addRessources(device.getResources());
    }

    public void removeDevice(String id)
    {
        System.out.println("Removing device " + id);

        Iterator<Device> iterator = devices.iterator();

        while(iterator.hasNext())
        {
            Device tmp = iterator.next();

            if(tmp.getId().equals(id))
                iterator.remove();
        }
    }

    public void addArchitecture(Architecture architecture)
    {
        System.out.println("Adding architecture " + architecture.getName());

        architectures.add(architecture);

        Iterator<Component> it = architecture.getComponent().iterator();

        while(it.hasNext())
            addComponent(it.next());
    }

    public void removeArchitecture(String name)
    {
        System.out.println("Removing architecture " + name);

        Iterator<Architecture> it = architectures.iterator();

        while(it.hasNext())
        {
            Architecture tmp = it.next();

            if(tmp.getName().equals(name))
            {
                it.remove();
            }
        }
    }

    private void addComponent(Component component)
    {
        System.out.println("Adding component " + component.getName());

        components.add(component);

        addRessources(component.getResources());
    }

    private void removeComponent(String name)
    {
        System.out.println("-- Removing component " + name);

        Iterator<Component> it = components.iterator();

        while(it.hasNext())
        {
            Component tmp = it.next();

            if(tmp.getName().equals(name))
                it.remove();
        }
    }

    private void addRessources(List<Resource> tmp)
    {
        Iterator<Resource> it = tmp.iterator();

        while(it.hasNext())
        {
            Resource resource = it.next();

            if (!resources.contains(resource.getName()))
                resources.add(resource.getName());
        }
    }

    public void addComponentOnDevice(String name, String deviceId)
    {
        Iterator<Device> it = devices.iterator();

        while(it.hasNext())
        {
            Device device = it.next();

            if(device.getId().equals(deviceId))
                device.addComponent(name);
        }
    }

    public void removeComponentFromDevice(String componentName, String deviceId)
    {

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
}
