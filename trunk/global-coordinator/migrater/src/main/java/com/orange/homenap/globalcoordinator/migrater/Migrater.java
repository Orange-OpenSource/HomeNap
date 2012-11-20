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

import com.orange.homenap.globalcoordinator.executer.ExecuterItf;
import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;
import com.orange.homenap.globalcoordinator.printer.PrinterItf;
import com.orange.homenap.utils.Action;
import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Component;
import com.orange.homenap.utils.Device;

import java.util.*;

public class Migrater implements MigraterItf
{
    // iPOJO requires
    private GlobalDatabaseItf globalDatabaseItf;
    private ExecuterItf executerItf;
    private PrinterItf printerItf;

    public void migrate(int[][] migrationPlan)
    {
        printerItf.print(migrationPlan);

        int n = migrationPlan.length;
        int m = migrationPlan[0].length;

        List<Action> actions = new ArrayList<Action>();

        for (int j = 0; j < m; j++)
        {
            Component component = globalDatabaseItf.getComponent(j);
            int checkMigration = 0;

            Action action = new Action();

            action.setActionName(Action.ActionName.MIGRATE);
            action.setComponent(component);
            
            for (int i = 0; i < n; i++)
            {
                Device device = globalDatabaseItf.getDevice(i);
                
                switch (migrationPlan[i][j])
                {
                    case 1:
                        action.setToDevice(device);
                        device.getComponentsOnDevice().add(component.getName());
                        break;
                    case -1:
                        action.setFromDevice(device.getId());
                        device.getComponentsOnDevice().remove(component.getName());
                        break;
                    default:
                        break;
                }

                checkMigration += migrationPlan[i][j];
            }

            if(checkMigration != 0)
            {
                System.out.println("Problem during migration!");
                break;
            }

            actions.add(action);
        }

        Map<String, List<Action>> actionsPerDevice = new HashMap<String, List<Action>>();

        Iterator<Action> it = actions.iterator();
        
        while(it.hasNext())
        {
            Action action = it.next();

            if(actionsPerDevice.containsKey(action.getFromDevice()))
            {
                actionsPerDevice.get(action.getFromDevice()).add(action);
            }
            else
            {
                List<Action> tmp = new ArrayList<Action>();

                tmp.add(action);
                
                actionsPerDevice.put(action.getFromDevice(), tmp);
            }
        }

        executerItf.executeActions(actionsPerDevice);

        //LocalManagerControlPointItf localManagerControlPointItf = controlPointManagerItf.createCP(device.getId());

        //localManagerControlPointItf.migrateService(component.getName(), device.getId(), device.getMac());
    }
}
