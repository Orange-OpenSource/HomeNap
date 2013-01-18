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

import java.util.*;

public class Device
{
    public static enum DeviceState {ON, SLEEP, HIBERNATE, OFF}

    private String id;
    private String mac;
    private String ip;
    private DeviceState deviceState;
    private List<DeviceState> statesSupported;
    private int consumptionOff;
    private int consumptionOnMin;
    private int consumptionOnMax;
    private List<String> componentsOnDevice = new ArrayList<String>();
    private List<Resource> resources = new ArrayList<Resource>();

    public Device() {}

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

    public void setConsumptionOff(int consumptionOff) { this.consumptionOff = consumptionOff; }

    public int getConsumptionOff() { return this.consumptionOff; }

    public void setConsumptionOnMin(int consumptionOnMin) { this.consumptionOnMin = consumptionOnMin; }

    public int getConsumptionOnMin()  { return this.consumptionOnMin; }

    public void setConsumptionOnMax(int consumptionOnMax) { this.consumptionOnMax = consumptionOnMax; }

    public int getConsumptionOnMax()  { return this.consumptionOnMax; }

    public void addComponent(String componentName) { componentsOnDevice.add(componentName); }

    public List<String> getComponentsOnDevice() { return componentsOnDevice; }

    public void setResources(List<Resource> resources) { this.resources = resources; }

    public List<Resource> getResources() { return resources; }

    //TODO: store power state change policy ?
}
