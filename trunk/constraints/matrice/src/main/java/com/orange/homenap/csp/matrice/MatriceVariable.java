/*--------------------------------------------------------
* Module Name : model
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
* File Name   : Model.java
*
* Created     : 30/12/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.csp.matrice;

import choco.Choco;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerVariable;
import com.orange.homenap.csp.utils.CSPVariable;
import com.orange.homenap.globalcoordinator.constraintmanager.ConstraintManagerItf;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;

public class MatriceVariable extends CSPVariable
{
    private GlobalDatabaseItf globalDatabaseItf;
    private ConstraintManagerItf constraintManagerItf;

    public void start()
    {
        constraintManagerItf.registerVariable(this);
    }

    public void stop()
    {
        constraintManagerItf.unRegisterVariable(this);
    }

    public Model addVariable(Model model)
    {
        int n = globalDatabaseItf.getDevicesSize();
        int m = globalDatabaseItf.getComponentsSize();

        IntegerVariable[][] a = new IntegerVariable[n][m];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
            {
                a[i][j] = Choco.makeIntVar("a" + i + j, 0, 1);

                model.addVariable(a[i][j]);
            }

        return  model;
    }
}
