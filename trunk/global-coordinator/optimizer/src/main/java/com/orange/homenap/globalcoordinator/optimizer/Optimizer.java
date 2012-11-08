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
import choco.Options;
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

    public void optimize(int currentConsumption, int[][] planInt)
    {
        System.out.println("Optimization started");

        // Size of matrix
        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();

        int periodBetweenEvents = 300;

        /**
         * Model
         */

        Model model = new CPModel();

        // Matrix creation
        // Variable aij = 1 if service j is on device i, 0 else
        // This is the only variable that should be used to reduce energy consumption
        IntegerVariable[][] a = new IntegerVariable[n][m];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                a[i][j] = Choco.makeIntVar("a" + i + j, new int[]{0, 1});

                model.addVariable(a[i][j]);
            }
        }

        // A device is active if it hosts at least one service
        IntegerExpressionVariable[] activeDevice = new IntegerExpressionVariable[n];

        for (int i = 0; i < n; i++)
            activeDevice[i] = Choco.ifThenElse(Choco.eq(Choco.sum(a[i]), 0), Choco.constant(0), Choco.constant(1));

        // QoR of devices
        // Number of resources available on devices
        int r = globalDatabaseItf.getResourcesSize();

        IntegerVariable[][] qeResources = new IntegerVariable[n][r];

        for (int i = 0; i < n; i++)
        {
            //TODO a surveiller.

            //Map<String, Integer> map = globalDatabaseItf.getDevice(i).getResources();
            List<Resource> resources = globalDatabaseItf.getDevice(i).getResources();

            Iterator<Resource> it = resources.iterator();

            for (int k = 0; k < r; k++)
                while(it.hasNext())
                {
                    Resource resource = it.next();

                    if(resource.getName().equals(globalDatabaseItf.getResource(k)))
                        qeResources[i][k] = Choco.constant(resource.getValue());
                    else
                        qeResources[i][k] = Choco.constant(0);
                }

            /*for (int k = 0; k < r; k++)
                if (map.containsKey((planItf.getResource(k))))
                    qeResources[i][k] = Choco.constant(map.get(planItf.getResource(k)));
                else
                    qeResources[i][k] = Choco.constant(0);*/
        }

        // QoR of services
        IntegerVariable[][] qsResources = new IntegerVariable[m][r];

        for (int j = 0; j < m; j++)
        {
            //Map<String, Integer> map = planItf.getService(j).getResources();
            List<Resource> resources = globalDatabaseItf.getComponent(j).getResources();

            Iterator<Resource> it = resources.iterator();

            for (int k = 0; k < r; k++)
            {
                while(it.hasNext())
                {
                    Resource resource = it.next();

                    if(resource.getName().equals(globalDatabaseItf.getResource(k)))
                        qsResources[j][k] = Choco.constant(resource.getValue());
                    else
                        qsResources[j][k] = Choco.constant(0);
                }
            }

            /*for (int k = 0; k < r; k++)
                if (map.containsKey((planItf.getResource(k))))
                    qsResources[j][k] = Choco.constant(map.get(planItf.getResource(k)));
                else
                    qsResources[j][k] = Choco.constant(0);*/
        }

        // Transposition of qsResources
        IntegerVariable[][] qsResourcesTranspose = new IntegerVariable[r][m];

        for (int j = 0; j < m; j++)
            for (int k = 0; k < r; k++)
                qsResourcesTranspose[k][j] = qsResources[j][k];

        // Service j on device i
        /*IntegerExpressionVariable[][] servicesOnDevice = new IntegerExpressionVariable[n][m];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                servicesOnDevice[i][j] = Choco.ifThenElse(Choco.eq(a[i][j], 0), Choco.constant(0), Choco.constant(1));*/

        // Sum of required QoR on i
        IntegerExpressionVariable[][] sumQs = new IntegerExpressionVariable[n][r];

        for (int i = 0; i < n; i++)
        {
            for (int k = 0; k < r; k++)
            {
                IntegerExpressionVariable temp = null;

                for(int j = 0; j < m; j++)
                    temp = Choco.plus(temp, Choco.mult(a[i][j], qsResourcesTranspose[k][j]));
                    //temp = Choco.plus(temp, Choco.mult(a[i][j], Choco.constant(20)));

                sumQs[i][k] = temp;
            }
        }

        // Active consumption for each device
        IntegerExpressionVariable[] activeConsumption = new IntegerExpressionVariable[n];

        for (int i = 0; i < n; i++)
        {
            //System.out.println(cpuUsage);

            List<Resource> resources = globalDatabaseItf.getDevice(i).getResources();

            Iterator<Resource> it = resources.iterator();

            Integer cpuResource = null;

            while(it.hasNext())
            {
                Resource resource = it.next();

                if(resource.getName().equals("CPU"))
                    cpuResource = resource.getValue();
            }

            activeConsumption[i] = Choco.plus(
                    Choco.div(
                            Choco.mult(globalDatabaseItf.getDevice(i).getConsumptionOnMax() - globalDatabaseItf.getDevice(i).getConsumptionOnMin(),
                                    sumQs[i][0]),
                            cpuResource),
                    globalDatabaseItf.getDevice(i).getConsumptionOnMin());
        }

        // Consumption of each device
        IntegerExpressionVariable[] devicesConsumption = new IntegerExpressionVariable[n];

        for (int i = 0; i < n; i++)
        {
            devicesConsumption[i] = Choco.plus(
                    Choco.mult(activeDevice[i], activeConsumption[i]),
                    Choco.mult(Choco.minus(1, activeDevice[i]), globalDatabaseItf.getDevice(i).getConsumptionOff()));
        }

        // Period between two events (in seconds)
        // TODO: change to machine learning

        // Consumption of migrations
        //*
        IntegerExpressionVariable[] migrationConso = new IntegerExpressionVariable[m];
        IntegerExpressionVariable[] serviceMigration = new IntegerExpressionVariable[m];
        IntegerExpressionVariable[][] deltaMatrix = new IntegerExpressionVariable[m][n];

        int migrationPeriod = 2;

        for (int j = 0; j < m; j++)
        {
            for (int i = 0; i < n; i++)
                deltaMatrix[j][i] = Choco.abs(Choco.minus(a[i][j], planInt[i][j]));

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

        IntegerVariable conso = Choco.makeIntVar("conso", 1, 1000000);

        model.addConstraint(Choco.eq(newConsumption, conso));

        /**
         * Constraints
         */

        //System.out.println("Constraints");

        // Constraint on matrix's rows: one value per row
        IntegerVariable[][] aTranspose = new IntegerVariable[m][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                aTranspose[j][i] = a[i][j];

        Constraint[] serviceUnity = new Constraint[m];

        for (int j = 0; j < m; j++)
            serviceUnity[j] = Choco.eq(Choco.sum(aTranspose[j]), 1);

        model.addConstraints(serviceUnity);

        //TODO fix
        // Constraints on devices QoR: a device cannot hold more services than its resources can support
        for (int i = 0; i < n; i++)
            for (int k = 0; k < r; k++)
                model.addConstraint(Options.E_DECOMP, Choco.geq(qeResources[i][k], sumQs[i][k]));

        // Mobility's management: a non migratable service -> aij = 1
        for (int j = 0; j < m; j++)
        {
            //System.out.println("Component " + globalDatabaseItf.getComponent(j).getName() + " is " + globalDatabaseItf.getComponent(j).getMigrability());

            if (globalDatabaseItf.getComponent(j).getMigrability().equals(Component.Migrability.STATIC))
            {
                for (int i = 0; i < n; i++)
                {
                    List<String> componentsOnDevice = globalDatabaseItf.getDevice(i).getComponentsOnDevice();

                    for (int s = 0; s < componentsOnDevice.size(); s++)
                        if (componentsOnDevice.get(s).equals(globalDatabaseItf.getComponent(j).getName()))
                        {
                            model.addConstraint(Choco.eq(a[i][j], 1));

                            break;
                        }
                }
            }
        }

        // Constraint on current consumption: new consumption should not be greater than previous consumption
        IntegerVariable oldConsumption = Choco.constant(Math.round(currentConsumption));

        //*
        model.addConstraint(Choco.lt(
                Choco.plus(Choco.mult(newConsumption, periodBetweenEvents), migrationConsumption),
                Choco.mult(oldConsumption, periodBetweenEvents)));
        /*/
        model.addConstraint(Choco.lt(newConsumption, oldConsumption));//*/

        // Model parameters
        model.setDefaultExpressionDecomposition(true);

        /**
         * Solver
         */

        //System.out.println("Solver");

        Solver s = new CPSolver();

        s.read(model);

        // Solve and print values
        if (s.minimize(s.getVar(conso), false))
        {
            System.out.println("Solution found: " + s.getVar(conso).getVal());

            int[][] delta = new int[n][m];

            for (int j = 0; j < m; j++)
                for (int i = 0; i < n; i++)
                    delta[i][j] = s.getVar(a[i][j]).getVal() - planInt[i][j];

            //migraterItf.migrate(delta);
        }
        else
        {
            System.out.println("No better solution found");
        }
    }
}