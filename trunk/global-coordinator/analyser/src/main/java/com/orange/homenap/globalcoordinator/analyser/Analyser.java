/*
 * --------------------------------------------------------
 *  Module Name : analyser
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
 *  File Name   : Analyser.java
 *
 *  Created     : 06/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.globalcoordinator.analyser;

import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.globalcoordinator.optimizer.OptimizerItf;
import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Resource;

import java.util.HashMap;
import java.util.Map;

public class Analyser implements AnalyserItf
{
    private GlobalDatabaseItf globalDatabaseItf;
    private OptimizerItf optimizerItf;

    public void analyse()
    {
        Map<Device, Resource> resourcesAvailable = new HashMap<Device, Resource>();

        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();

        int currentConsumption = getPlanConsumption(n, m);

        System.out.println("Current consumption is about " + currentConsumption + " W");

        optimizerItf.optimize(currentConsumption);
    }

    private int getPlanConsumption(int n, int m)
    {
        int[][] plan = this.createPlan(n, m);

        int[] sumAj = new int[n];

        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                sumAj[i] = sumAj[i] + plan[i][j];

        int[] activeDevices = new int[n];

        for(int i = 0; i < n; i++)
        {
            if(sumAj[i] == 0)
                activeDevices[i] = 0;
            else
                activeDevices[i] = 1;
        }

        int[] devicesConsumption = new int[n];

        for(int i = 0; i < n; i++)
            devicesConsumption[i] = activeDevices[i] * globalDatabaseItf.getDevice(i).getConsumptionOn()
                    + (1 - activeDevices[i]) * globalDatabaseItf.getDevice(i).getConsumptionOff();

        int consumption = 0;

        for(int i = 0; i < n; i++)
            consumption = consumption + devicesConsumption[i];

        return consumption;
    }

/*    public Map<Device, Resource> getResourcesAvailable()
    {
        Map<Device, Resource> resourcesAvailable = new HashMap<Device, Resource>();

        for (int j = 0; j < globalDatabaseItf.getDevicesSize(); j++)
        {
            Device device = globalDatabaseItf.getDevice(j);
            int componentsOnDevice = device.getComponentsOnDevice().size();
            
            for(int i = 0 ; i < componentsOnDevice; i++)
            {
                Component component = componentsOnDevice.get(i);

                Iterator<Component> it = architecture.getComponent().iterator();

                while(it.hasNext())
                {
                    Component component = it.next();

                    for(Map.Entry<String, Integer> map : component.getResources().entrySet())
                    {
                        qorDeviceAvailable.put(map.getKey(),
                                qorDeviceAvailable.get(map.getKey()) - component.getResources().get(map.getKey()));
                    }
                }
            }
        }

        return resourcesAvailable;
    }*/

    private int[][] createPlan(int n, int m)
    {
        int[][] plan = new int[n][m];

        for (int j = 0; j < m; j++)
            for (int i = 0; i < n; i++)
                if (globalDatabaseItf.getDevice(i).getComponentsOnDevice().contains(globalDatabaseItf.getComponent(j).getName()))
                    plan[i][j] = 1;
                else
                    plan[i][j] = 0;

        return plan;
    }

    /*private void printPlan(int n, int m)
    {
        int[][] plan = this.createPlan(n, m);

        for(int i = 0; i < n; i++)
            System.out.print("\t" + globalDatabaseItf.getDevice(i).getId());

        System.out.println();

        for (int j = 0; j < m; j++)
        {
            System.out.print(globalDatabaseItf.getComponent(j).getName() + "\t");

            for (int i = 0; i < n; i++)
            {
                System.out.print(plan[i][j] + "\t");
            }

            System.out.println();
        }
    }*/
}
