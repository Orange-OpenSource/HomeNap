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
* File Name   : HomeManager.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.olnc.made.homenap.globalcoordinator;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;
import com.olnc.made.homenap.utils.Device;
import com.olnc.made.homenap.utils.Service;
import org.osgi.framework.BundleContext;

import java.util.*;

public class HomeManager implements GlobalCoordinatorEvent
{
    // iPOJO requires
    private DeviceInfoDBItf deviceInfoDBItf;
    private ControlPointManagerItf controlPointManagerItf;

    // iPOJO properties
    private boolean stateful;

    // iPOJO injection
    private BundleContext bundleContext;

    public HomeManager(BundleContext bundleContext)
    {
        this.bundleContext = bundleContext;
    }

    public void start()
    {

    }

    public void stop()
    {

    }

    public void optimize()
        {
            System.out.println("Starting Choco");

            Map<String, Integer> qorD1 = new HashMap<String, Integer>();
            Map<String, Integer> qorD2 = new HashMap<String, Integer>();
            Map<String, Integer> qorD3 = new HashMap<String, Integer>();

            qorD1.put("CPU", 10000);
            qorD1.put("RAM", 4000);
            qorD1.put("ROM", 500);

            qorD2.put("CPU", 7000);
            qorD2.put("RAM", 2000);
            qorD2.put("ROM", 1000);

            qorD3.put("CPU", 5000);
            qorD3.put("RAM", 1000);
            qorD3.put("ROM", 250);
    /*/
            /*qorD1.put("CPU", 10000);
            qorD1.put("RAM", 4000);
            qorD1.put("ROM", 5000);

            qorD2.put("CPU", 9000);
            qorD2.put("RAM", 2000);
            qorD2.put("ROM", 1000);

            qorD3.put("CPU", 5000);
            qorD3.put("RAM", 1000);
            qorD3.put("ROM", 2500);
    */

            Device d1 = new Device("D1", qorD1, 2, 50);
            Device d2 = new Device("D2", qorD2, 5, 40);
            Device d3 = new Device("D3", qorD3, 4, 25);

            Map<String, Integer> qorS1 = new HashMap<String, Integer>();
            Map<String, Integer> qorS2 = new HashMap<String, Integer>();
            Map<String, Integer> qorS3 = new HashMap<String, Integer>();
            Map<String, Integer> qorS4 = new HashMap<String, Integer>();
            Map<String, Integer> qorS5 = new HashMap<String, Integer>();
            Map<String, Integer> qorS6 = new HashMap<String, Integer>();
            Map<String, Integer> qorS7 = new HashMap<String, Integer>();

    //*
            qorS1.put("CPU", 500);
            qorS1.put("RAM", 400);
            qorS1.put("ROM", 50);

            qorS2.put("CPU", 600);
            qorS2.put("RAM", 200);
            qorS2.put("ROM", 100);

            qorS3.put("CPU", 50);
            qorS3.put("RAM", 250);
            qorS3.put("ROM", 15);

            qorS4.put("CPU", 5000);
            qorS4.put("RAM", 450);
            qorS4.put("ROM", 65);

            qorS5.put("CPU", 3000);
            qorS5.put("RAM", 750);
            qorS5.put("ROM", 100);

            qorS6.put("CPU", 2500);
            qorS6.put("RAM", 500);
            qorS6.put("ROM", 25);

            qorS7.put("CPU", 1000);
            qorS7.put("RAM", 100);
            qorS7.put("ROM", 50);

    /*/
            qorS1.put("CPU", 10);
            qorS1.put("RAM", 40);
            qorS1.put("ROM", 50);

            qorS2.put("CPU", 60);
            qorS2.put("RAM", 20);
            qorS2.put("ROM", 10);

            qorS3.put("CPU", 50);
            qorS3.put("RAM", 25);
            qorS3.put("ROM", 15);

            qorS4.put("CPU", 50);
            qorS4.put("RAM", 45);
            qorS4.put("ROM", 65);

            qorS5.put("CPU", 30);
            qorS5.put("RAM", 75);
            qorS5.put("ROM", 10);

            qorS6.put("CPU", 25);
            qorS6.put("RAM", 50);
            qorS6.put("ROM", 25);

            qorS7.put("CPU", 10);
            qorS7.put("RAM", 10);
            qorS7.put("ROM", 50);
    //*/

            Service s1 = new Service("S1", 1, qorS1);
            Service s2 = new Service("S2", 0, qorS2);
            Service s3 = new Service("S3", 1, qorS3);
            Service s4 = new Service("S4", 0, qorS4);
            Service s5 = new Service("S5", 0, qorS5);
            Service s6 = new Service("S6", 1, qorS6);
            Service s7 = new Service("S7", 1, qorS7);

            d1.addService(s1);
            d1.addService(s2);
            d1.addService(s3);
            d1.addService(s4);
            d2.addService(s5);
            d2.addService(s6);
            d3.addService(s7);

            List<Device> devices = new ArrayList<Device>();

            devices.add(d1);
            devices.add(d2);
            devices.add(d3);

            // Creation de la liste des services
            List<Service> services = new ArrayList<Service>();

            for(int i = 0; i < devices.size(); i++)
                services.addAll(devices.get(i).getServicesOnDevice());

            int n = devices.size();
            int m = services.size();

            // Creation du plan de placement et initialisation à 0
            int[][] plan = new int[n][m];
            int[] sumAj = new int[n];

            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < m; j++)
                {
                    plan[i][j] = 0;
                }

                sumAj[i] = 0;
            }

            // Remplissage du plan
            int jMax = 0;

            for (int i = 0; i < n; i++)
            {
                List<Service> servicesOnDevice = devices.get(i).getServicesOnDevice();

                for (int j = jMax; j < jMax + servicesOnDevice.size(); j++)
                {
                    plan[i][j] = 1;
                }

                jMax = jMax + servicesOnDevice.size();
            }

            for(int i = 0; i < n; i++)
                for(int j = 0; j < m; j++)
                    sumAj[i] = sumAj[i] + plan[i][j];

            int[] previousActiveDevice = new int[n];

            for(int i = 0; i < n; i++)
            {
                if(sumAj[i] == 0)
                    previousActiveDevice[i] = 0;
                else
                    previousActiveDevice[i] = 1;
            }

            int[] previousConsumption = new int[n];

            for(int i = 0; i < n; i++)
                previousConsumption[i] = previousActiveDevice[i] * devices.get(i).getConsumptionOn()
                        + (1 - previousActiveDevice[i]) * devices.get(i).getConsumptionOff();

            int actualConsumption = 0;

            for(int i = 0; i < n; i++)
                actualConsumption = actualConsumption + previousConsumption[i];

            System.out.println("Previous consumption : " + actualConsumption);

            System.out.println("Previous deployment plan :");

            for (int j = 0; j < m; j++)
            {
                for (int i = 0; i < n; i++)
                {
                    System.out.print(plan[i][j] + "\t");
                }

                System.out.println();
            }

            /**
             * Model
             */

            Model model = new CPModel();

            // Variable aij = 1 si le service j est sur l'équipement i, 0 sinon
            IntegerVariable[][] a = new IntegerVariable[n][m];

            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < m; j++)
                {
                    a[i][j] = Choco.makeIntVar("a" + i + j, new int[]{0, 1});

                    model.addVariable(a[i][j]);
                }
            }

            // Un équipement est actif dès lors qu'il héberge au moins un service
            IntegerExpressionVariable[] activeDevice = new IntegerExpressionVariable[n];

            for(int i = 0; i < n; i++)
                activeDevice[i] = Choco.ifThenElse(Choco.eq(Choco.sum(a[i]), 0), Choco.constant(0) , Choco.constant(1));

            // Consumption of each device
            IntegerExpressionVariable[] deviceConsumption = new IntegerExpressionVariable[n];

            for (int i = 0; i < n; i++)
            {
                deviceConsumption[i] = Choco.plus(
                        Choco.mult(activeDevice[i], devices.get(i).getConsumptionOn()),
                        Choco.mult(Choco.minus(1, activeDevice[i]), devices.get(i).getConsumptionOff()));

                //System.out.println(deviceConsumption[i]);
            }

            // Consumption of the new deployment plan
            IntegerExpressionVariable newConsumption = Choco.sum(deviceConsumption);

            /**
             * Constraints
             */

            // Contraintes sur la ligne : une seule valeur par ligne
            IntegerVariable[][] aTranspose = new IntegerVariable[m][n];

            for(int i = 0; i < n; i++)
                for(int j = 0; j < m; j++)
                    aTranspose[j][i] = a[i][j];

            Constraint[] serviceUnity = new Constraint[m];

            for(int j = 0; j < m; j++)
                serviceUnity[j] = Choco.eq(Choco.sum(aTranspose[j]), 1);

            model.addConstraints(serviceUnity);

            // Gestion de la mobilité : on fixe les valeurs des services non migrables à aij = 1
            for(int j = 0; j < m; j++)
            {
                if(services.get(j).getMigrability() == 0)
                {
                    for(int i = 0; i < n; i++)
                    {
                        boolean serviceOnDevice = false;

                        Iterator<Service> iterator = devices.get(i).getServicesOnDevice().iterator();

                        while (iterator.hasNext())
                        {
                            if(iterator.next().getName().equals(services.get(j).getName()))
                                serviceOnDevice = true;
                        }

                        if(serviceOnDevice)
                            model.addConstraint(Choco.eq(a[i][j], 1));
                    }
                }
            }

            // Contraintes sur la consommation du Home
            IntegerVariable oldConsumption = Choco.constant(actualConsumption);

            model.addConstraint(Choco.lt(newConsumption, oldConsumption));

            /*for (int j = 0; j < m; j++)
            {
                for (int i = 0; i < n; i++)
                    System.out.print(a[i][j] + "\t");

                System.out.println();
            }*/

            // La QoR
            int r = 3; // resources number (eg. CPU, RAM, ROM)

            // QoR des équipements
            IntegerVariable[][] qeResources = new IntegerVariable[n][r];

            for(int i = 0; i < n; i++)
            {
                List<Integer> resources = new ArrayList<Integer>();
                int k = 0;

                for(Map.Entry<String, Integer> map : devices.get(i).getQorDeviceMax().entrySet())
                {
                    qeResources[i][k] = Choco.constant(map.getValue());

                    k++;
                }
            }

            // QoR des services
            IntegerVariable[][] qsResources = new IntegerVariable[m][r];

            for(int j = 0; j < m; j++)
            {
                List<Integer> resources = new ArrayList<Integer>();
                int k = 0;

                for(Map.Entry<String, Integer> map : services.get(j).getQorService().entrySet())
                {
                    qsResources[j][k] = Choco.constant(map.getValue());

                    k++;
                }
            }

            // Transposée de qsResources
            IntegerVariable[][] qsResourcesTranspose = new IntegerVariable[r][m];

            for(int j = 0; j < m; j++)
                for(int k = 0; k < r; k++)
                    qsResourcesTranspose[k][j] = qsResources[j][k];

            // Service j on device i
            IntegerExpressionVariable[][] servicesOnDevice = new IntegerExpressionVariable[n][m];

            for(int i = 0; i < n; i++)
                for(int j = 0; j < m; j++)
                    servicesOnDevice[i][j] = Choco.ifThenElse(Choco.eq(a[i][j], 0), Choco.constant(0), Choco.constant(1));

            // Somme des QoR requises sur i
            IntegerExpressionVariable[][] sumQs = new IntegerExpressionVariable[n][r];

            for (int i = 0; i < n; i++)
            {
                for(int k = 0; k < r; k++)
                {
                    IntegerExpressionVariable temp = Choco.constant(0);

                    for(int j = 0; j < m; j++)
                    {
                        temp = Choco.plus(temp, Choco.mult(servicesOnDevice[i][j], qsResourcesTranspose[k][j]));

                        /*System.out.println("Temp[" + k + "] + Delta[" + i + "][" + j + "] x R[" + k + "][" + j + "]");

                        System.out.println(temp);*/
                    }

                    sumQs[i][k] = temp;

                    //System.out.println(sumQs[i][k] + "\n");

                }
            }

            // Contraintes sur la QoR des équipements
            for(int i = 0; i < n; i++)
                for(int k = 0; k < r; k++)
                {
                    model.addConstraint(Choco.geq(qeResources[i][k], sumQs[i][k]));

                    //System.out.println(Choco.geq(qeResources[i][k], sumQs[i][k]));
                }

            //Our solver
            Solver s = new CPSolver();

            s.read(model);

            boolean success =  s.solve();

            //Print the values
            if (success)
            {
                System.out.println("Solution :");

                for (int j = 0; j < m; j++)
                {
                    for (int i = 0; i < n; i++)
                        System.out.print(s.getVar(a[i][j]).getVal() + "\t");

                    System.out.println();
                }
            }
            else
            {
                System.out.println("No better solution found");
            }
        }

    public void newDevice(Device device)
    {
        System.out.println("GC: new device " + device.getId());

        Collection<Device> devices = deviceInfoDBItf.values();

        Iterator<Device> it = devices.iterator();

        boolean migration = false;
        String serviceId = null;

/*        while (it.hasNext())
        {
            Device oldDevice = it.next();

            for(Map.Entry<String, Service.ServiceState> oldE : oldDevice.getServicesState().entrySet())
            {
                for(Map.Entry<String, Service.ServiceState> e : device.getServicesState().entrySet())
                {
                    if(e.getKey().equals(oldE.getKey()))
                    {
                        if(e.getValue().equals(Service.ServiceState.STARTED))
                        {
                            if(device.getConsumption() < oldDevice.getConsumption())
                            {
                                migration = true;
                                serviceId = e.getKey();
                            }
                        }
                    }
                }
            }
        }*/

        if (migration = true)
        {
            LocalManagerControlPointItf localManagerControlPointItf = controlPointManagerItf.createCP(device.getId());

            localManagerControlPointItf.migrateService(serviceId, device.getId(), device.getMac());
        }
    }

    public void servicesStateChange(Device device)
    {
        //TODO
    }

    public void deviceStateChange(Device device)
    {
        System.out.println("GC: " + device.getId() + " going to " + device.getDeviceState() + " mode");

        Collection<Device> devices = deviceInfoDBItf.values();

        Iterator<Device> it = devices.iterator();

        boolean migration = false;
        String serviceId = null;
        
        Device newDevice = null;

        while (it.hasNext())
        {
            Device otherDevice = it.next();

            if (!device.getId().equals(otherDevice.getId()))
            {
                for (Map.Entry<String, Service.ServiceState> e : device.getServicesState().entrySet())
                {
                    //System.out.println(e.getValue().getClass() + " | " + Service.ServiceState.STARTED.getClass());

                    //TODO: fix problem with enum type...
                    if (Service.ServiceState.STARTED.name().equals(e.getValue()))
                    {
                        if (otherDevice.getServicesState().containsKey(e.getKey()))
                        {
                            if (otherDevice.getServicesState().get(e.getKey()).equals(Service.ServiceState.UNINSTALLED))
                            {
                                if (otherDevice.getConsumptionOn() < device.getConsumptionOn())
                                {
                                    newDevice = otherDevice;
                                    serviceId = e.getKey();
                                }
                            }
                        }
                    }
                }
            }
        }

        if (newDevice != null)
        {
            LocalManagerControlPointItf localManagerControlPointItf = controlPointManagerItf.createCP(newDevice.getId());

            localManagerControlPointItf.migrateService(serviceId, newDevice.getId(), newDevice.getMac());
        }
        else
        {
            //localManagerControlPointItf.stopStateChange();
        }
    }

    public void goodbyeDevice(Device device)
    {
        //TODO: process bye bye

        controlPointManagerItf.removeCP(device.getId());
    }
}
