/*--------------------------------------------------------
* Module Name : API
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
* File Name   : Device.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device
{
    public static enum DeviceState {ON, SLEEP, HIBERNATE, OFF};

    private String id;
    private String mac;
    private String ip;
    private DeviceState deviceState;
    private List<DeviceState> statesSupported;
    private Map<String, Service.BundleState> servicesState;
    private int consumptionOff;
    private int consumptionOn;
    private List<Service> servicesOnDevice = new ArrayList<Service>();
    private Map<String, Integer> resources = new HashMap<String, Integer>();

    public Device() {}

    public Device(String id) { this.id = id; }

    public void setId(String id) { this.id = id; }

    public String getId() { return this.id; }

    public void setMac(String mac) { this.mac = mac; }

    public String getMac() { return this.mac; }

    public void setIp(String ip) { this.ip = ip; }

    public String getIp() { return this.ip; }

    public void setDeviceState(DeviceState deviceState) { this.deviceState = deviceState; }

    public DeviceState getDeviceState() { return this.deviceState; }

    public void setStatesSupported(List<DeviceState> statesSupported) { this.statesSupported = statesSupported; }

    public List<DeviceState> getStatesSupported() { return this.statesSupported; }

    public void setServicesState(Map<String, Service.BundleState> servicesState) { this.servicesState = servicesState; }

    public Map<String, Service.BundleState> getServicesState() { return this.servicesState; }

    public void setConsumptionOff(int consumptionOff) { this.consumptionOff = consumptionOff; }

    public int getConsumptionOff() { return this.consumptionOff; }

    public void setConsumptionOn(int consumptionOn) { this.consumptionOn = consumptionOn; }

    public int getConsumptionOn() { return this.consumptionOn; }

    public void addService(Service service) { servicesOnDevice.add(service); }

    public List<Service> getServicesOnDevice() { return servicesOnDevice; }

    public void setResources(Map<String, Integer> resources) { this.resources = resources; }

    public Map<String, Integer> getResources() { return resources; }

    public Map<String, Integer> getQorDeviceAvailable()
    {
        Map<String, Integer> qorDeviceAvailable = resources;

        for(int i = 0 ; i < servicesOnDevice.size(); i++)
        {
            Service service = servicesOnDevice.get(i);

            for(Map.Entry<String, Integer> map : service.getResources().entrySet())
            {
                qorDeviceAvailable.put(map.getKey(),
                        qorDeviceAvailable.get(map.getKey()) - service.getResources().get(map.getKey()));
            }
        }

        return qorDeviceAvailable;
    }

    //TODO: stocker la politique de mise en veille ?
}
