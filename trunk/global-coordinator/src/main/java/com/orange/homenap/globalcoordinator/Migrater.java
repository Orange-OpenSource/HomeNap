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
* File Name   : Migrater.java
*
* Created     : 03/08/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.globalcoordinator;

import com.orange.homenap.utils.Device;
import com.orange.homenap.utils.Service;

public class Migrater implements MigraterItf
{
    // iPOJO requires
    private PlanItf planItf;
    private ControlPointManagerItf controlPointManagerItf;

    // iPOJO properties
    private boolean stateful;

    public void migrate(int[][] migrationPlan)
    {
        int n = migrationPlan.length;

        for (int i = 0; i < n; i++)
        {
            int m = migrationPlan[i].length;

            for (int j = 0; j < m; j++)
                if(migrationPlan[i][j] == 1)
                {
                    Service service = planItf.getService(j);
                    Device device = planItf.getDevice(i);

                    LocalManagerControlPointItf localManagerControlPointItf = controlPointManagerItf.createCP(device.getId());

                    localManagerControlPointItf.migrateService(service.getName(), device.getId(), device.getMac());
                }
        }
    }
}