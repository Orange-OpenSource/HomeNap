/*--------------------------------------------------------
* Module Name : migrability
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
* File Name   : MigrabilityConstraint.java
*
* Created     : 30/12/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.csp.migrability;

import choco.Choco;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerVariable;
import com.orange.homenap.csp.utils.CSPConstraint;
import com.orange.homenap.globalcoordinator.csp.CSPPluginManagerItf;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Constraint;

import java.util.List;

public class MigrabilityConstraint extends CSPConstraint
{
    private CSPPluginManagerItf cspPluginManagerItf;
    private GlobalDatabaseItf globalDatabaseItf;

    private String keyword;

    public void start()
    {
        cspPluginManagerItf.registerConstraint(this);
    }

    public void stop()
    {
        cspPluginManagerItf.unRegisterConstraint(this);
    }

    public Model addConstraint(Model model, IntegerVariable[][] a)
    {
        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();

        // Mobility's management: a non migratable service -> aij = 1
        for (int j = 0; j < m; j++)
        {
            //System.out.println("Component " + globalDatabaseItf.getComponents(j).getName() + " is " + globalDatabaseItf.getComponents(j).getMigrability());

            Component component = globalDatabaseItf.getComponent(j);
            Constraint constraint = null;

            for(Constraint tmp : component.getConstraints())
            {
                if(tmp.getName().equals(keyword))
                {
                    constraint = tmp;
                    break;
                }
            }

            boolean migratable = Boolean.valueOf(constraint.getValue());

            if (!migratable)
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

        return model;
    }
}
