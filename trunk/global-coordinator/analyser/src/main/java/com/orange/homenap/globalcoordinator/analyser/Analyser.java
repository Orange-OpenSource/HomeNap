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
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

        if (n != 0 && m != 0)
        {
            int currentConsumption = getPlanConsumption(n, m);

            printPlan(n, m);

            System.out.println("Current consumption is about " + currentConsumption + " W");

            optimizerItf.optimize(currentConsumption);
        }
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

        float[] devicesConsumption = new float[n];

        for(int i = 0; i < n; i++)
        {
            Device device = globalDatabaseItf.getDevice(i);

            float cpuUsage = getResourceUsage(device, "CPU");

            float activeConsumption = (device.getConsumptionOnMax() - device.getConsumptionOnMin()) * cpuUsage
                                + device.getConsumptionOnMin();

            devicesConsumption[i] = activeDevices[i] * activeConsumption
                    + (1 - activeDevices[i]) * device.getConsumptionOff();
        }

        float consumption = 0;

        for(int i = 0; i < n; i++)
            consumption = consumption + devicesConsumption[i];

        return (int) consumption;
    }

    private float getResourceUsage(Device device, String resourceName)
    {
        float usage = 0f;
        int resourceUsed = 0;
        int resourceDevice = 0;

        Iterator<Resource> iterator = device.getResources().iterator();

        while(iterator.hasNext())
        {
            Resource deviceResource = iterator.next();

            if(deviceResource.getName().equals(resourceName))
                resourceDevice= deviceResource.getValue();
        }

        for(int j = 0 ; j < device.getComponentsOnDevice().size(); j++)
        {
            String componentName = device.getComponentsOnDevice().get(j);

            // TODO: component is not registred inside the DB when GC appears.
            Iterator<Resource> it = globalDatabaseItf.getComponentByName(componentName).getResources().iterator();

            while(it.hasNext())
            {
                Resource componentResource = it.next();

                if(componentResource.getName().equals(resourceName))
                    resourceUsed += componentResource.getValue();
            }
        }

        usage = (float) resourceUsed / resourceDevice;

        return usage;
    }

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

    private void printPlan(int n, int m)
    {
        int[][] plan = this.createPlan(n, m);

        for(int i = 0; i < n; i++)
            System.out.print("\t" + globalDatabaseItf.getDevice(i).getIp());

        System.out.println();

        for (int j = 0; j < m; j++)
        {
            String name = globalDatabaseItf.getComponent(j).getName();

            if(name.toCharArray().length >= 9)
                name = name.substring(0, 9) + "...";
            
            System.out.print(name + "\t");

            System.out.printf("%1d  %7.2f   %7.1f   %4dms   %4dms%n", 5, 1000F, 20000F, 1000, 1250);
            System.out.printf("%1d  %7.2f   %7.1f   %4dms   %4dms%n", 6, 300F, 700F, 200, 950);

            for (int i = 0; i < n; i++)
            {
                System.out.print(plan[i][j] + "\t");
            }

            System.out.println();
        }
    }
}
