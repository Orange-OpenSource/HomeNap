package com.orange.services;

import com.orange.services.utils.NotifyingThread;

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
