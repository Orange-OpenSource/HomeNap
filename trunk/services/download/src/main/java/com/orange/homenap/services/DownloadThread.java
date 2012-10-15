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
 *  File Name   : DownloadThread.java
 *
 *  Created     : 15/10/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.services;

import com.orange.homenap.services.utils.NotifyingThread;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends NotifyingThread
{
    private URL url;
    private String httpRange;
    private String outputFile;

    public DownloadThread(URL url, String httpRange, String outputFile)
    {
        this.url = url;
        this.httpRange = httpRange;
        this.outputFile = outputFile;
    }

    public void doRun()
    {
        //System.out.println("Starting download of " + url.toString() + " (" + httpRange + ")");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Range", "bytes=" + httpRange + "-");

            if(connection != null)
            {
                BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("/tmp/" + outputFile));

                byte[] buffer = new byte[65536];
                int c;

                while ((c = bis.read(buffer)) != -1 && !this.isInterrupted())
                    bos.write(buffer, 0, c);

                bos.flush();

                bis.close();
                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
