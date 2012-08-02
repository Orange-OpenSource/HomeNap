package com.orange.homenap.globalcoordinator;

import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Service;

public interface PlanItf
{
    public void addDevice(Device device);

    public void addService(Service service, Device device);

    public void removeDevice(Device device);

    public void removeService(Service service);
    
    public Service getService(int i);
    
    public int getServicesSize();

    public Device getDevice(int i);

    public int getDevicesSize();

    public String getResource(int i);

    public int getResourcesSize();

    public int[][] createPlan();

    public void printPlan();

    public int getPlanConsumption();

    public void migrateService(Service service, Device device);
}
