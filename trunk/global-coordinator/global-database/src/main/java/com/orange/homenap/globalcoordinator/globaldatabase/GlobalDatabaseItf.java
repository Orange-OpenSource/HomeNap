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
* File Name   : GlobalDatabaseItf.java
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

import java.util.List;

public interface GlobalDatabaseItf
{
    public void addDevice(Device device);

    public void removeDevice(String deviceId);

    public void addArchitecture(Architecture architecture);

    public void removeArchitecture(String architectureName);

    public Component getComponent(int i);

    public int getComponentsSize();

    public List<Component> getComponents();

    public Device getDevice(int i);

    public int getDevicesSize();

    public String getResource(int i);

    public int getResourcesSize();
}
