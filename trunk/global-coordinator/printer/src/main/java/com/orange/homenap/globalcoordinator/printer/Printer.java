/*--------------------------------------------------------
* Module Name : printer
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
* File Name   : Printer.java
*
* Created     : 08/11/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.globalcoordinator.printer;

import com.orange.homenap.globalcoordinator.globaldatabase.GlobalDatabaseItf;

public class Printer implements PrinterItf
{
    private GlobalDatabaseItf globalDatabaseItf;

    public void print(int[][] plan)
    {
        int n = plan.length;
        int m = plan[0].length;

        System.out.println("------------------------------");

        StringBuffer header = new StringBuffer("%-30s |");
        String[] devices = new String[n + 1];
        devices[0] = "";

        for(int i = 0; i + 1 < n + 1; i++)
        {
            header.append("%15s ");

            devices[i + 1] = globalDatabaseItf.getDevice(i).getId();
        }

        header.append("%n");

        System.out.printf(header.toString(), devices);

        for (int j = 0; j < m; j++)
        {
            StringBuffer data = new StringBuffer("%30s |");

            String line[] = new String[n + 1];

            line[0] = globalDatabaseItf.getComponent(j).getName();

            for (int i = 0; i < n; i++)
            {
                data.append("%10s      ");

                line[i + 1] = String.valueOf(plan[i][j]);
            }

            data.append("%n");

            System.out.printf(data.toString(), line);
        }

        System.out.println("------------------------------");
    }
}
