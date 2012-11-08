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

package com.orange.homenap.globalcoordinator.migrater;

import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.globalcoordinator.printer.PrinterItf;
import com.orange.homenap.globalcoordinator.upnpcpmanager.ControlPointManagerItf;
import com.orange.homenap.globalcoordinator.upnpcpmanager.LocalManagerControlPointItf;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Device;

public class Migrater implements MigraterItf
{
    // iPOJO requires
    private GlobalDatabaseItf globalDatabaseItf;
    private ControlPointManagerItf controlPointManagerItf;
    private PrinterItf printerItf;

    public void migrate(int[][] migrationPlan)
    {
        printerItf.print(migrationPlan);

        /*int n = migrationPlan.length;

        for (int i = 0; i < n; i++)
        {
            int m = migrationPlan[i].length;

            for (int j = 0; j < m; j++)
                if(migrationPlan[i][j] == 1)
                {
                    Component component = globalDatabaseItf.getComponent(j);
                    Device device = globalDatabaseItf.getDevice(i);

                    LocalManagerControlPointItf localManagerControlPointItf = controlPointManagerItf.createCP(device.getId());

                    localManagerControlPointItf.migrateService(component.getName(), device.getId(), device.getMac());
                }
        }*/
    }
}
