/*
 * --------------------------------------------------------
 *  Module Name : local-manager
 *  Version : 0.1-SNAPSHOT
 *
 *  Software Name : HomeNap
 *  Version : 0.1-SNAPSHOT
 *
 *  Copyright © 28/06/2012 – 28/06/2012 France Télécom
 *  This software is distributed under the Apache 2.0 license,
 *  the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 *  or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 *  File Name   : StateFileManager.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.migrationservice;

import com.orange.homenap.localmanager.json.GsonServiceItf;
import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.utils.Service;
import com.orange.homenap.utils.StatefulComponent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StateFileManager implements StateFileManagerItf
{
    // iPOJO requires
    private LocalDatabaseItf localDatabaseItf;
    private GsonServiceItf gsonServiceItf;

    // iPOJO properties
    private String directory;

    public void start()
    {
        System.out.println("Creating directory " + directory);

        // Creation of temporaries directories
        if (!(new File(directory)).exists())
        {
            boolean result = (new File(directory)).mkdir();

            if(!result)
                System.out.println("Unable to create" + directory + "directory");
        }
    }

    public void stop()
    {
        System.out.println("Removing directory " + directory);

        // Deleting temporaries files and directories
        if ((new File(directory)).exists())
        {
            try {
                delete(new File(directory));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void delete(File f) throws IOException
    {
        if (f.isDirectory())
        {
            for (File c : f.listFiles())
                delete(c);
        }

        if (!f.delete())
            System.out.println("Unable to remove " + directory + " directory");
    }

    public void save(String bundleName)
    {
        Service service = localDatabaseItf.get(localDatabaseItf.getServiceId(bundleName));

        if (service.getServiceState().equals(Service.ServiceState.STATEFUL))
        {
            System.out.println("Saving state to file " + directory + "/" + bundleName + ".json");

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "/" + bundleName + ".json"));

                writer.write(gsonServiceItf.toJson(service.getComponents()));

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load(String bundleName)
    {
        Service service = localDatabaseItf.get(localDatabaseItf.getServiceId(bundleName));

        if (service.getServiceState().equals(Service.ServiceState.STATEFUL))
        {
            if (stateFileExist(bundleName))
            {
                Map<String, StatefulComponent> components = new HashMap<String, StatefulComponent>();
                StringBuilder json = new StringBuilder();

                System.out.println("Loading state from file " + directory + "/" + bundleName + ".json");

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(directory + "/" + bundleName + ".json"));

                    String strLine;

                    while((strLine = reader.readLine()) != null)
                        json.append(strLine);

                    reader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                components = gsonServiceItf.fromJson(json.toString(), Map.class);

                service.setComponents(components);
            }
            else
            {
                System.out.println(directory + "/" + bundleName + ".json does not exist. Starting from beginning");
            }
        }
    }

    private boolean stateFileExist(String bundleName)
    {
        return (new File(directory + "/" + bundleName + ".json")).exists();
    }
}
