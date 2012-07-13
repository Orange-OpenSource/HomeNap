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
 *  File Name   : DeviceInfo.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.olnc.made.homenap.localmanager;

import com.google.gson.Gson;
import com.olnc.made.homenap.utils.Device;

import java.io.*;

public class DeviceInfo implements DeviceInfoItf
{
    // iPOJO properties
    private String deviceInfoFile;
    private boolean stateful;

    // Global variables
    private Device device;

    public void start()
    {
        // Get device information from device.json file
        StringBuilder json = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(deviceInfoFile));

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

        this.device = gson.fromJson(json.toString(), Device.class);

        /*// Get MAC address
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface netint : Collections.list(nets))
            {
                if(!netint.getDisplayName().equals("lo") && !netint.getDisplayName().startsWith("vmnet"))
                {
                    byte[] macBytes = netint.getHardwareAddress();

                    String mac = String.format("%02X", macBytes[0]) + ":" +
                            String.format("%02X", macBytes[1]) + ":" +
                            String.format("%02X", macBytes[2]) + ":" +
                            String.format("%02X", macBytes[3]) + ":" +
                            String.format("%02X", macBytes[4]) + ":" +
                            String.format("%02X", macBytes[5]);

                    this.mac = mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }*/
    }

    public void setUDN(String udn)
    {
        device.setId(udn);
    }

    public Device getDevice()
    {
        return this.device;
    }

    private int getBogoMips()
    {
        int bogomips = 0;

        try {
            Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo | grep bogomips | sed 's/[^0-9. ]*//g'");

            InputStream in = process.getInputStream();

            int c;

            while((c = in.read()) != -1)
            {
                bogomips = c;
            }

            in .close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bogomips;
    }

    private int getRam()
    {
        int ram = 0;

        try {
            Process process = Runtime.getRuntime().exec("free -t | grep Mem | sed 's/[^0-9]//g'");

            InputStream in = process.getInputStream();

            int c;

            while((c = in.read()) != -1)
            {
                ram = c;
            }

            in .close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ram;
    }

    //TODO: see oshi -> https://github.com/dblock/oshi or sigar -> http://sourceforge.net/projects/sigar/files/
}
