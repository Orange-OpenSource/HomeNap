/*
 * --------------------------------------------------------
 *  Module Name : download
 *  Version : 1.0-SNAPSHOT
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
 *  File Name   : ServiceCoordinator.java
 *
 *  Created     : 15/10/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.services.download;

import com.orange.homenap.services.download.utils.NotifyingThread;
import com.orange.homenap.services.download.utils.ThreadCompleteListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ServiceCoordinator implements ThreadCompleteListener
{
    // iPOJO requires
    //private ConcatenateItf concatenateItf;

    // iPOJO properties
    public String url;
    public String httpRange;
    public List<String> fileList;

    // Global variables
    private NotifyingThread thread;

    private boolean suspend;
    private boolean complete;

    public void start()
    {
        System.out.println("Starting download");

        suspend = false;
        complete = false;

        URL urlURL = null;
        String outputFile = null;

        try {
            urlURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(url != null)
        {
            outputFile = url.toString().split("/")[url.toString().split("/").length - 1] + "." + httpRange;

            thread = new DownloadThread(urlURL, httpRange, outputFile);
            thread.addListener(this);

            System.out.println("Starting download of " + url + " (" + httpRange + ")");

            thread.start();
        }
        else
            System.out.println("URL: " + url + " not found");

        if(outputFile != null)
            fileList.add(outputFile);
    }

    public void stop()
    {
        if(!complete)
        {
            System.out.println("Stopping download");

            suspend = true;

            if(thread != null)
                if(thread.isAlive())
                    thread.interrupt();

            while(thread.isAlive());

            if(!fileList.isEmpty())
            {
                // Update http-range
                File file = new File("/tmp/" + fileList.get(fileList.size() - 1));

                if(file.exists())
                    httpRange = String.valueOf(new Long(httpRange) + file.length());
                else
                    System.out.println("File " + file.getName() + " doesn't exist. Unable to set httpRange property");
            }
        }
        else
        {
            System.out.println("Download complete");
        }
    }

    public void notifyOfThreadComplete(Thread thread)
    {
        if(!fileList.isEmpty() && !suspend)
        {
            // Update http-range
            File file = new File("/tmp/" + fileList.get(fileList.size() - 1));

            if(file.exists())
                httpRange = String.valueOf(file.length());
            else
                System.out.println("File " + file.getName() + " doesn't exist. Unable to set httpRange property");

            // Concatenate only if there is multiples parts
/*            try {
                concatenateItf.concatenate(fileList);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            complete = true;

            this.stop();
        }
    }
}
