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
* File Name   : DeviceInfoDB.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.globalcoordinator;

import com.orange.homenap.utils.Device;

import java.util.Collection;
import java.util.Map;

public class DeviceInfoDB implements DeviceInfoDBItf
{
    // iPOJO properties
    private boolean stateful;
    private Map<String, Device> deviceInfoMap;

    public void start()
    {
        if(!deviceInfoMap.isEmpty())
        {
            for(Map.Entry<String, Device> e : deviceInfoMap.entrySet())
            {
                // TODO: create CP
            }
        }
    }

    public void put(String deviceId, Device device) { deviceInfoMap.put(deviceId, device); }

    public Device get(String deviceId) { return deviceInfoMap.get(deviceId); }

    public boolean containsKey(String deviceId) { return deviceInfoMap.containsKey(deviceId); }

    public void remove(String deviceId) { deviceInfoMap.remove(deviceId); }

    public Collection<Device> values() { return deviceInfoMap.values(); }
}
