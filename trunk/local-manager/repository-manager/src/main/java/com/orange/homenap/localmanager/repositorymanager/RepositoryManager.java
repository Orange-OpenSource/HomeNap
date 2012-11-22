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
import java.net.URL;

public class RepositoryManager implements RepositoryManagerItf
{
    // iPOJO properties
    public String repository;

    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;

    public String addBundleToRepository(String url)
    {
        String fileName = url.split("/")[url.split("/").length - 1];

        String httpUrlBundle = url;

        if (!url.startsWith("http://" + deviceInfoItf.getDevice().getIp()))
        {
            try
            {
                InputStream in = null;

                if(url.startsWith("file:/"))
                    in = new FileInputStream(new File(url.replace("file:", "")));
                else if(url.startsWith("http://"))
                    in = (new URL(url)).openStream();

                OutputStream out = new FileOutputStream(new File(repository + "/" + fileName));

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

            httpUrlBundle = "http://" + deviceInfoItf.getDevice().getIp() + "/repository/" + fileName;
        }

        return httpUrlBundle;
    }

    public void removeBundleFromRepository(String bundleName)
    {
        //TODO
    }
}
