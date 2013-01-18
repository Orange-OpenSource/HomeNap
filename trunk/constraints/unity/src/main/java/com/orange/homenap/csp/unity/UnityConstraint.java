/*--------------------------------------------------------
* Module Name : unity
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
* File Name   : UnityConstraint.java
*
* Created     : 01/01/2013
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.csp.unity;

import choco.Choco;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerVariable;
import com.orange.homenap.csp.utils.CSPConstraint;
import com.orange.homenap.globalcoordinator.csp.CSPPluginManagerItf;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;

import java.util.Iterator;

public class UnityConstraint extends CSPConstraint
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
        Iterator<IntegerVariable> it = model.getIntVarIterator();

        while(it.hasNext())
        {
            IntegerVariable intVar = it.next();

            System.out.println(intVar.pretty());
        }

        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();

        IntegerVariable[][] aTranspose = new IntegerVariable[m][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                aTranspose[j][i] = a[i][j];

        Constraint[] serviceUnity = new Constraint[m];

        for (int j = 0; j < m; j++)
            serviceUnity[j] = Choco.eq(Choco.sum(aTranspose[j]), 1);

        model.addConstraints(serviceUnity);

        return model;
    }
}
