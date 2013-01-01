/*--------------------------------------------------------
* Module Name : resources
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
* File Name   : ResourcesConstraint.java
*
* Created     : 30/12/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.csp.resources;

import choco.Choco;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import com.orange.homenap.csp.utils.CSPConstraint;
import com.orange.homenap.globalcoordinator.constraintmanager.ConstraintManagerItf;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.utils.Resource;

import java.util.HashMap;
import java.util.Map;

public class ResourcesConstraint extends CSPConstraint
{
    private ConstraintManagerItf constraintManagerItf;
    private GlobalDatabaseItf globalDatabaseItf;

    public void start()
    {
        constraintManagerItf.registerConstraint(this);
    }

    public void stop()
    {
        constraintManagerItf.unRegisterConstraint(this);
    }

    public Model addConstraint(Model model)
    {
        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();
        int r = globalDatabaseItf.getResourcesSize();

        // QoR of devices
        // Total resources on devices

        int[][] qeResources = new int[n][r];

        for (int i = 0; i < n; i++)
        {
            Map<String, Integer> map = new HashMap<String, Integer>();

            for(Resource resource : globalDatabaseItf.getDevice(i).getResources())
                map.put(resource.getName(), resource.getValue());

            for (int k = 0; k < r; k++)
            {
                if(map.containsKey(globalDatabaseItf.getResource(k)))
                {
                    //System.out.println(globalDatabaseItf.getDevice(i).getId() + " " + globalDatabaseItf.getResource(k) + " " + map.get(globalDatabaseItf.getResource(k)));

                    qeResources[i][k] = map.get(globalDatabaseItf.getResource(k));
                }
                else
                {
                    //System.out.println(globalDatabaseItf.getDevice(i).getId() + " " + globalDatabaseItf.getResource(k) + " " + 0);

                    qeResources[i][k] = 0;
                }

                //System.out.println(globalDatabaseItf.getResource(k) + " " + qeResources[i][k]);
            }
        }

        // QoR of services
        //IntegerVariable[][] qsResources = new IntegerVariable[m][r];
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

        // Constraints on devices QoR: a device cannot hold more services than its resources can support
        for (int i = 0; i < n; i++)
            for (int k = 0; k < r; k++)
                model.addConstraint(Choco.geq(qeResources[i][k], sumQs[i][k]));

        return model;
    }
}
