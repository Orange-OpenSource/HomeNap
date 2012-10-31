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

import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Service;

public interface GlobalDatabaseItf
{
    public void addDevice(Device device);

    public void addService(Service service, String deviceId);

    public void removeDevice(String deviceId);

    public void removeService(String serviceId);

    public Service getService(int i);

    public int getServicesSize();

    public Device getDevice(int i);

    public int getDevicesSize();

    public String getResource(int i);

    public int getResourcesSize();

    public int[][] createPlan();

    public void printPlan();

    public int getPlanConsumption();

    /*public void migrateService(Service service, Device device);*/
}
