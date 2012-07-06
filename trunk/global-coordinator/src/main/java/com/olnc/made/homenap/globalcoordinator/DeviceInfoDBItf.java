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
* File Name   : DeviceInfoDBItf.java
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

import java.util.Collection;

public interface DeviceInfoDBItf
{
    public void put(String deviceId, Device device);

    public Device get(String deviceId);
    
    public boolean containsKey(String deviceId);
    
    public void remove(String deviceId);

    public Collection<Device> values();
}
