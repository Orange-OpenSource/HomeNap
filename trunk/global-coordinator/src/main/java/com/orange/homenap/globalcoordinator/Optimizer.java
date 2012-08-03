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

public class Optimizer implements OptimizerItf
{
    // iPOJO requires
    private ControlPointManagerItf controlPointManagerItf;
    private PlanItf planItf;
    private MigraterItf migraterItf;

    // iPOJO properties
    private boolean stateful;

    public void optimize()
    {
        System.out.println("Reconfiguration started");

        // Size of matrix
        int n = planItf.getDevicesSize();
        int m = planItf.getServicesSize();

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
                    Choco.mult(activeDevice[i], planItf.getDevice(i).getConsumptionOn()),
                    Choco.mult(Choco.minus(1, activeDevice[i]), planItf.getDevice(i).getConsumptionOff()));
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

        // Management of mobility: fix values of unmigratable services to aij = 1
        for(int j = 0; j < m; j++)
        {
            if(planItf.getService(j).getUnmigratable())
            {
                for(int i = 0; i < n; i++)
                {
                    List<Service> servicesOnDevice = planItf.getDevice(i).getServicesOnDevice();

                    for (int s = 0; s < servicesOnDevice.size(); s++)
                        if (servicesOnDevice.get(s).getId().equals(planItf.getService(j).getId()))
                        {
                            model.addConstraint(Choco.eq(a[i][j], 1));

                            break;
                        }
                }
            }
        }

        // Constraint on home consumption
        IntegerVariable oldConsumption = Choco.constant(planItf.getPlanConsumption());

        model.addConstraint(Choco.lt(newConsumption, oldConsumption));

        // Quality of resources (QoR)
        int r = planItf.getResourcesSize();

        // QoR of devices
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

        // QoR of services
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

            int[][] plan = planItf.createPlan();
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
}