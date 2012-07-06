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

package com.olnc.made.homenap.utils;

import java.util.List;
import java.util.Map;

public class Device
{
    public static enum DeviceState {ON, SLEEP, HIBERNATE, OFF};

    private String id;
    private String mac;
    private DeviceState deviceState;
    private List<DeviceState> statesSupported;
    private Map<String, Service.ServiceState> servicesState;
    private long consumption;

    public Device() {}

    public Device(String id) { this.id = id; }

    public void setId(String id) { this.id = id; }

    public String getId() { return this.id; }

    public void setMac(String mac) { this.mac = mac; }

    public String getMac() { return this.mac; }

    public void setDeviceState(DeviceState deviceState) { this.deviceState = deviceState; }

    public DeviceState getDeviceState() { return this.deviceState; }

    public void setStatesSupported(List<DeviceState> statesSupported) { this.statesSupported = statesSupported; }

    public List<DeviceState> getStatesSupported() { return this.statesSupported; }

    public void setServicesState(Map<String, Service.ServiceState> servicesState) { this.servicesState = servicesState; }

    public Map<String, Service.ServiceState> getServicesState() { return this.servicesState; }

    public void setConsumption(long consumption) { this.consumption = consumption; }

    public long getConsumption() { return this.consumption; }

    //TODO: stocker la politique de mise en veille ?
}
