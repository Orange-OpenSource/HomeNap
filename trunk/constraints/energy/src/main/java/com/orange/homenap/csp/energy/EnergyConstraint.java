package com.orange.homenap.csp.energy;

import choco.Choco;
import choco.Options;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import com.orange.homenap.csp.utils.CSPConstraint;
import com.orange.homenap.globalcoordinator.csp.CSPPluginManagerItf;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EnergyConstraint extends CSPConstraint
{
    private CSPPluginManagerItf cspPluginManagerItf;
    private GlobalDatabaseItf globalDatabaseItf;

    public void start()
    {
        cspPluginManagerItf.registerConstraint(this);
    }

    public void stop()
    {
        cspPluginManagerItf.unRegisterConstraint(this);
    }
    
    public Model addConstraint(Model model)
    {
        // Period between two events (in seconds)
        // TODO: learn from habit patterns of household.
        int periodBetweenEvents = 300;

        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();
        int r = globalDatabaseItf.getResourcesSize();

        int[][] qsResources = new int[m][r];

        for (int j = 0; j < m; j++)
        {
            Map<String, Integer> map = new HashMap<String, Integer>();

            for(Resource resource : globalDatabaseItf.getComponent(j).getResources())
                map.put(resource.getName(), resource.getValue());

            for (int k = 0; k < r; k++)
            {
                if(map.containsKey(globalDatabaseItf.getResource(k)))
                {
                    //System.out.println(globalDatabaseItf.getComponent(j).getName() + " " + globalDatabaseItf.getResource(k) + " " + map.get(globalDatabaseItf.getResource(k)));

                    //qsResources[j][k] = Choco.constant(resource.getValue());
                    qsResources[j][k] = map.get(globalDatabaseItf.getResource(k));
                }
                else
                {
                    //System.out.println(globalDatabaseItf.getComponent(j).getName() + " " + globalDatabaseItf.getResource(k) + " " + 0);

                    //qsResources[j][k] = Choco.constant(0);
                    qsResources[j][k] = 0;
                }

                //System.out.println(globalDatabaseItf.getResource(k) + " " + qsResources[j][k]);
            }
        }

        // Transposition of qsResources
        //IntegerVariable[][] qsResourcesTranspose = new IntegerVariable[r][m];
        int[][] qsResourcesTranspose = new int[r][m];

        for (int j = 0; j < m; j++)
            for (int k = 0; k < r; k++)
            {
                qsResourcesTranspose[k][j] = qsResources[j][k];

                //System.out.println(qsResourcesTranspose[k][j]);
            }

        // A device is active if it hosts at least one component,
        // so that the sum of its row is greater or equal to 1.
        IntegerExpressionVariable[] activeDevice = new IntegerExpressionVariable[n];

        for (int i = 0; i < n; i++)
            activeDevice[i] = Choco.ifThenElse(Choco.eq(Choco.sum(a[i]), 0), Choco.constant(0), Choco.constant(1));

        // Service j on device i
        /*IntegerExpressionVariable[][] servicesOnDevice = new IntegerExpressionVariable[n][m];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                servicesOnDevice[i][j] = Choco.ifThenElse(Choco.eq(a[i][j], 0), Choco.constant(0), Choco.constant(1));*/

        // Sum of required QoR on i
        IntegerExpressionVariable[][] sumQs = new IntegerExpressionVariable[n][r];

        // Fix: it seems there is a difference between both method used here but I don't know why :-/
        for (int i = 0; i < n; i++)
        {
            for (int k = 0; k < r; k++)
            {
                //IntegerExpressionVariable[] temp = new IntegerExpressionVariable[m];
                IntegerExpressionVariable temp = Choco.constant(0);

                for(int j = 0; j < m; j++)
                    temp = Choco.plus(temp, Choco.mult(a[i][j], qsResourcesTranspose[k][j]));
                //temp[k] = Choco.mult(a[i][j], qsResourcesTranspose[k][j]);

                sumQs[i][k] = temp;
                //sumQs[i][k] = Choco.sum(temp);
            }
        }

        // Active consumption for each device
        IntegerExpressionVariable[] activeConsumption = new IntegerExpressionVariable[n];

        for (int i = 0; i < n; i++)
        {
            Device device = globalDatabaseItf.getDevice(i);

            int deltaConsumption = device.getConsumptionOnMax() - device.getConsumptionOnMin();

            Integer cpuResource = null;

            for(Resource resource : device.getResources())
            {
                if(resource.getName().equals("CPU"))
                    cpuResource = resource.getValue();
            }

            activeConsumption[i] = Choco.plus(
                    Choco.div(
                            Choco.mult(deltaConsumption, sumQs[i][0]),
                            cpuResource),
                    device.getConsumptionOnMin());
        }

        // Consumption of each device
        IntegerExpressionVariable[] devicesConsumption = new IntegerExpressionVariable[n];

        for (int i = 0; i < n; i++)
        {
            devicesConsumption[i] = Choco.plus(
                    Choco.mult(activeDevice[i], activeConsumption[i]),
                    Choco.mult(Choco.minus(1, activeDevice[i]), globalDatabaseItf.getDevice(i).getConsumptionOff()));
        }

        IntegerExpressionVariable[] migrationConso = new IntegerExpressionVariable[m];
        IntegerExpressionVariable[] serviceMigration = new IntegerExpressionVariable[m];
        IntegerExpressionVariable[][] deltaMatrix = new IntegerExpressionVariable[m][n];

        int[][] plan = new int[n][m];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                if (globalDatabaseItf.getDevice(i).getComponentsOnDevice().contains(globalDatabaseItf.getComponent(j).getName()))
                    plan[i][j] = 1;
                else
                    plan[i][j] = 0;
            }
        }

        int migrationPeriod = 2;

        for (int j = 0; j < m; j++)
        {
            for (int i = 0; i < n; i++)
                deltaMatrix[j][i] = Choco.abs(Choco.minus(a[i][j], plan[i][j]));

            serviceMigration[j] = Choco.ifThenElse(Choco.eq(Choco.sum(deltaMatrix[j]), 0), Choco.constant(0), Choco.constant(1));

            IntegerExpressionVariable[] plusConso = new IntegerExpressionVariable[n];

            for (int i = 0; i < n; i++)
                plusConso[i] = Choco.ifThenElse(Choco.neq(deltaMatrix[j][i], 0), Choco.constant(globalDatabaseItf.getDevice(i).getConsumptionOnMin()), Choco.constant(0));

            migrationConso[j] = Choco.mult(serviceMigration[j],
                    Choco.mult(migrationPeriod,Choco.sum(plusConso)));
        }

        IntegerExpressionVariable migrationConsumption = Choco.sum(migrationConso);/*/
                int migrationConsumption = 1;//*/

        /**
         * Objective Function
         */

        //System.out.println("Objective function");

        // Consumption of the organized plan being optimized
        /*
        IntegerExpressionVariable newConsumption = Choco.plus(
        Choco.mult(Choco.sum(devicesConsumption), periodBetweenEvents),
        migrationConsumption);/*/
        IntegerExpressionVariable newConsumption = Choco.sum(devicesConsumption);//*/

        Integer consoSystemOff = 0;
        Integer consoSystemMax = 0;

        for(int i = 0; i < n; i++)
        {
            consoSystemOff += globalDatabaseItf.getDevice(i).getConsumptionOff();
            consoSystemMax += globalDatabaseItf.getDevice(i).getConsumptionOnMax();
        }

        IntegerVariable conso = Choco.makeIntVar("conso", consoSystemOff, consoSystemMax, Options.V_BOUND);

        model.addConstraint(Choco.eq(newConsumption, conso));

        // Constraint on current consumption: new consumption should not be greater than previous consumption
        IntegerVariable oldConsumption = Choco.constant(Math.round(getPlanConsumption(plan, n, m)));

        //*
        model.addConstraint(Choco.lt(
                Choco.plus(Choco.mult(newConsumption, periodBetweenEvents), migrationConsumption),
                Choco.mult(oldConsumption, periodBetweenEvents)));

        return model;
    }

    private int getPlanConsumption(int[][] plan, int n, int m)
    {
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
}