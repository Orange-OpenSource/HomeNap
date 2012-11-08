/*
 * --------------------------------------------------------
 *  Module Name : global-coordinator-cp
 *  Version : 0.1-SNAPSHOT
 *
 *  Software Name : HomeNap
 *  Version : 0.1-SNAPSHOT
 *
 *  Copyright © 28/06/2012 – 28/06/2012 France Télécom
 *  This software is distributed under the Apache 2.0 license,
 *  the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 *  or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 *  File Name   : GlobalCoordinatorControlPointItf.java
 *
 *  Created     : 06/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.globalcoordinatorcp;

import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Device;

import java.util.Map;

public interface GlobalCoordinatorControlPointItf
{
    public void register();

    public void unRegister();

    public boolean startArchitecture(Architecture architecture);

    public void stopArchitecture(String name);

    public void updateDeviceState(String deviceId, Device.DeviceState state);
}
