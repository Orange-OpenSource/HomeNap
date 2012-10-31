/*
 * --------------------------------------------------------
 *  Module Name : repository-manager
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
 *  File Name   : ServiceManagerEvent.java
 *
 *  Created     : 31/10/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.repositorymanager;

import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;

import java.io.*;

public class RepositoryManager implements RepositoryManagerItf
{
    // iPOJO properties
    public String repository;

    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;

    public String addBundleToRepository(String url)
    {
        File inputFile = new File(url.split(":")[1]);
        File outputFile = new File(repository + "/" + inputFile.getName());

        try {
            InputStream in = new FileInputStream(inputFile);
            OutputStream out = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0)
                out.write(buffer, 0, length);

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String httpUrlBundle = "http://" + deviceInfoItf.getDevice().getIp() + "/repository/" + outputFile.getName();
        
        return httpUrlBundle;
    }
    
    public void removeBundleFromRepository(String bundleName)
    {
        //TODO
    }
}
