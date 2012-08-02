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

package com.orange.homenap.globalcoordinator;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;
import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Service;
import org.osgi.framework.BundleContext;

import java.util.*;

public class HomeManager implements GlobalCoordinatorEvent
{
    // iPOJO requires
    private DeviceInfoDBItf deviceInfoDBItf;
    private ControlPointManagerItf controlPointManagerItf;
    private PlanItf planItf;

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
        System.out.println("Reconfiguration");

                // Size of plan

                int n = planItf.getDevicesSize();
                int m = planItf.getServicesSize();

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
                            Choco.mult(activeDevice[i], planItf.getDevice(i).getConsumptionOn()),
                            Choco.mult(Choco.minus(1, activeDevice[i]), planItf.getDevice(i).getConsumptionOff()));
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
                    if(planItf.getService(j).getUnmigratable())
                    {
                        for(int i = 0; i < n; i++)
                        {
                            List<Service> servicesOnDevice = planItf.getDevice(i).getServicesOnDevice();

                            for (int s = 0; s < servicesOnDevice.size(); s++)
                                if (servicesOnDevice.get(s).getName().equals(planItf.getService(j).getName()))
                                {
                                    model.addConstraint(Choco.eq(a[i][j], 1));

                                    break;
                                }
                        }
                    }
                }

                // Contraintes sur la consommation du Home
                IntegerVariable oldConsumption = Choco.constant(planItf.getPlanConsumption());

                model.addConstraint(Choco.lt(newConsumption, oldConsumption));

                // La QoR
                int r = planItf.getResourcesSize();

                // QoR des équipements
                IntegerVariable[][] qeResources = new IntegerVariable[n][r];

                for(int i = 0; i < n; i++)
                {
                    Map<String, Integer> map = planItf.getDevice(i).getResources();

                    for(int k = 0; k < r; k++)
                        if(map.containsKey((planItf.getResource(k))))
                            qeResources[i][k] = Choco.constant(map.get(planItf.getResource(k)));
                        else
                            qeResources[i][k] = Choco.constant(0);
                }

                // QoR des services
                IntegerVariable[][] qsResources = new IntegerVariable[m][r];

                for(int j = 0; j < m; j++)
                {
                    Map<String, Integer> map = planItf.getService(j).getResources();

                    for(int k = 0; k < r; k++)
                        if(map.containsKey((planItf.getResource(k))))
                            qsResources[j][k] = Choco.constant(map.get(planItf.getResource(k)));
                        else
                            qsResources[j][k] = Choco.constant(0);
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
                            temp = Choco.plus(temp, Choco.mult(servicesOnDevice[i][j], qsResourcesTranspose[k][j]));

                        sumQs[i][k] = temp;
                    }
                }

                // Contraintes sur la QoR des équipements
                for(int i = 0; i < n; i++)
                    for(int k = 0; k < r; k++)
                        model.addConstraint(Choco.geq(qeResources[i][k], sumQs[i][k]));

                //Our solver
                Solver s = new CPSolver();

                s.read(model);

                boolean success =  s.solve();

                //Print the values
                if (success)
                {
                    System.out.println("Solution found");

                    int[][] plan = planItf.createPlan();
                    int[][] delta = new int[n][m];

                    for (int j = 0; j < m; j++)
                        for (int i = 0; i < n; i++)
                        {
                            delta[i][j] = s.getVar(a[i][j]).getVal() - plan[i][j];

                            if(delta[i][j] == 1)
                                planItf.migrateService(planItf.getService(j), planItf.getDevice(i));
                        }

                    //migraterItf.migrateServices(delta);
                }
                else
                    System.out.println("No better solution found");
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
