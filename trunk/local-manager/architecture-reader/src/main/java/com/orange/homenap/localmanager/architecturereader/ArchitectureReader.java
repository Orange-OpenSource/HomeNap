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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orange.homenap.localmanager.eventlistener.ArchitectureEvent;
import com.orange.homenap.localmanager.repositorymanager.RepositoryManagerItf;
import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchitectureReader implements ArchitectureReaderItf
{
    private ArchitectureEvent architectureEvent;
    private RepositoryManagerItf repositoryManagerItf;

    public void startService(String file)
    {
        Architecture architecture = getArchitectureFromJson(file);

        if (architecture != null)
        {
            List<Component> components = getComponentsFromArchitecture(file, architecture.getComponents());

            architectureEvent.startArchitecture(architecture, components);
        }
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

        Gson gson = new Gson();

        try {
            return gson.fromJson(json.toString(), Architecture.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Component> getComponentsFromArchitecture(String fileStr, List<String> strList)
    {
        List<Component> components = new ArrayList<Component>();
        Gson gson = new Gson();

        File file = new File(fileStr);
        String absolutePath = file.getAbsolutePath().replace(file.getName(), "");

        for(String str : strList)
        {
            StringBuilder json = new StringBuilder();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(absolutePath + str + ".json"));

                String strLine;

                while((strLine = reader.readLine()) != null)
                    json.append(strLine);

                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Component component = null;

            try {
                component = gson.fromJson(json.toString(), Component.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            String url = repositoryManagerItf.addBundleToRepository(component.getUrl());

            component.setUrl(url);

            components.add(component);
        }

        return components;
    }
}
