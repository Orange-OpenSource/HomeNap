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

package com.orange.homenap.globalcoordinator.optimizer;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.globalcoordinator.migrater.MigraterItf;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Resource;

import java.util.*;

public class Optimizer implements OptimizerItf
{
    // iPOJO requires
    private GlobalDatabaseItf globalDatabaseItf;
    private MigraterItf migraterItf;

    public void optimize(int currentConsumption)
    {
        System.out.println("Optimization started");

        // Size of matrix
        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();

        /**
         * Model
         */

        Model model = new CPModel();

        // aij = 1 if service j is executed on device i else 0
        IntegerVariable[][] a = new IntegerVariable[n][m];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                a[i][j] = Choco.makeIntVar("a" + i + j, new int[]{0, 1});

                model.addVariable(a[i][j]);
            }
        }

        // A device is active once it execute at least one service
        IntegerExpressionVariable[] activeDevice = new IntegerExpressionVariable[n];

        for(int i = 0; i < n; i++)
            activeDevice[i] = Choco.ifThenElse(Choco.eq(Choco.sum(a[i]), 0), Choco.constant(0) , Choco.constant(1));

        // Consumption of each device
        IntegerExpressionVariable[] deviceConsumption = new IntegerExpressionVariable[n];

        for (int i = 0; i < n; i++)
        {
            deviceConsumption[i] = Choco.plus(
                    Choco.mult(activeDevice[i], globalDatabaseItf.getDevice(i).getConsumptionOnMin()),
                    Choco.mult(Choco.minus(1, activeDevice[i]), globalDatabaseItf.getDevice(i).getConsumptionOff()));
        }

        // Consumption of the new deployment plan
        IntegerExpressionVariable newConsumption = Choco.sum(deviceConsumption);

        /**
         * Constraints
         */

        // Constraint on row : only one value per row
        IntegerVariable[][] aTranspose = new IntegerVariable[m][n];

        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                aTranspose[j][i] = a[i][j];

        Constraint[] serviceUnity = new Constraint[m];

        for(int j = 0; j < m; j++)
            serviceUnity[j] = Choco.eq(Choco.sum(aTranspose[j]), 1);

        model.addConstraints(serviceUnity);

        //TODO à revoir
        // Management of mobility: fix values of unmigratable services to aij = 1
        for(int j = 0; j < m; j++)
        {
            if(globalDatabaseItf.getComponent(j).getMigrability().equals(Component.Migrability.MIGRATABLE))
            {
                for(int i = 0; i < n; i++)
                {
                    List<String> componentsOnDevice = globalDatabaseItf.getDevice(i).getComponentsOnDevice();

                    for (int s = 0; s < componentsOnDevice.size(); s++)
                    {
                        if (componentsOnDevice.get(s).equals(globalDatabaseItf.getComponent(j).getName()))
                        {
                            model.addConstraint(Choco.eq(a[i][j], 1));

                            break;
                        }
                    }
                }
            }
        }

        // Constraint on home consumption
        IntegerVariable oldConsumption = Choco.constant(currentConsumption);

        model.addConstraint(Choco.lt(newConsumption, oldConsumption));

        // Quality of resources (QoR)
        int r = globalDatabaseItf.getResourcesSize();

        // QoR of devices
        IntegerVariable[][] qeResources = new IntegerVariable[n][r];

        for(int i = 0; i < n; i++)
        {
            List<Resource> resources = globalDatabaseItf.getDevice(i).getResources();

            for(int k = 0; k < r; k++)
            {
                Iterator<Resource> it = resources.iterator();

                while(it.hasNext())
                {
                    Resource resource = it.next();

                    if(resource.getName().equals(globalDatabaseItf.getResource(k)))
                        qeResources[i][k] = Choco.constant(resource.getValue());
                    else
                        qeResources[i][k] = Choco.constant(0);
                }
            }
        }

        // QoR of services
        IntegerVariable[][] qsResources = new IntegerVariable[m][r];

        for(int j = 0; j < m; j++)
        {
            List<Resource> resources = globalDatabaseItf.getComponent(j).getResources();

            for(int k = 0; k < r; k++)
            {
                Iterator<Resource> it = resources.iterator();

                while(it.hasNext())
                {
                    Resource resource = it.next();

                    if(resource.getName().equals(globalDatabaseItf.getResource(k)))
                        qsResources[j][k] = Choco.constant(resource.getValue());
                    else
                        qsResources[j][k] = Choco.constant(0);
                }
            }
        }

        // Transpose of qsResources
        IntegerVariable[][] qsResourcesTranspose = new IntegerVariable[r][m];

        for(int j = 0; j < m; j++)
            for(int k = 0; k < r; k++)
                qsResourcesTranspose[k][j] = qsResources[j][k];

        // Service j on device i
        IntegerExpressionVariable[][] servicesOnDevice = new IntegerExpressionVariable[n][m];

        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                servicesOnDevice[i][j] = Choco.ifThenElse(Choco.eq(a[i][j], 0), Choco.constant(0), Choco.constant(1));

        // Sum of QoR required on device i
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

        // Constraint on QoR of devices
        for(int i = 0; i < n; i++)
            for(int k = 0; k < r; k++)
                model.addConstraint(Choco.geq(qeResources[i][k], sumQs[i][k]));

        //Our solver
        Solver s = new CPSolver();

        s.read(model);

        boolean success =  s.solve();

        if (success)
        {
            System.out.println("Solution found");

            int[][] plan = createPlan(globalDatabaseItf.getDevicesSize(), globalDatabaseItf.getComponentsSize());
            int[][] delta = new int[n][m];

            for (int j = 0; j < m; j++)
                for (int i = 0; i < n; i++)
                    delta[i][j] = s.getVar(a[i][j]).getVal() - plan[i][j];

            // TODO: wait a given period before starting migration. Avoid migrating on each event, especially when events are close

            migraterItf.migrate(delta);
        }
        else
            System.out.println("No better solution found");
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
}