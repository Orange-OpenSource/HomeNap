/*
 * --------------------------------------------------------
 *  Module Name : architecture-reader
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
 *  File Name   : ArchitectureReader.java
 *
 *  Created     : 05/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.architecturereader;

import com.orange.homenap.localmanager.eventlistener.ArchitectureEvent;
import com.orange.homenap.localmanager.json.GsonServiceItf;
import com.orange.homenap.utils.Architecture;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ArchitectureReader implements ArchitectureReaderItf
{
    private GsonServiceItf gsonServiceItf;
    private ArchitectureEvent architectureEvent;

    public void startService(String file)
    {
        //TODO: should be set inside migration-service and only when this is not an optional architecture
        System.setProperty("org.apache.felix.ipojo.handler.auto.primitive", "com.orange.homenap.localmanager.migrationservice.handler:migration-handler");

        Architecture architecture = getArchitectureFromJson(file);

        architectureEvent.startArchitecture(architecture);
    }

    public void stopService(String name)
    {
        architectureEvent.stopArchitecture(name);
    }

    private Architecture getArchitectureFromJson(String jsonFile)
    {
        StringBuilder json = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));

            String strLine;

            while((strLine = reader.readLine()) != null)
                json.append(strLine);

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gsonServiceItf.fromJson(json.toString(), Architecture.class);
    }
}
