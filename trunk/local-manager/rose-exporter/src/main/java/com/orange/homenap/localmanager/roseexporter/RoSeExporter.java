/*
 * --------------------------------------------------------
 * Module Name : rose-exporter
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : HomeNap
 * Version : 0.1-SNAPSHOT
 *
 * Copyright © 28/06/2012 – 31/12/2013 France Télécom
 * This software is distributed under the Apache 2.0 license,
 * the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 * or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 * File Name   : RoSeExporter.java
 *
 * Created     : 03/12/2012
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.roseexporter;

import com.google.gson.Gson;
import org.osgi.framework.BundleContext;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.EndpointListener;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Map;

public class RoSeExporter implements EndpointListener
{
    private BundleContext bundleContext;
    private String broadcastAddress;
    private String localAddr;
    
    private Map<String, EndpointDescription> endpointDescriptionMap;

    public RoSeExporter(BundleContext bc)
    {
        bundleContext = bc;

        try {
            for (final Enumeration< NetworkInterface > interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();)
            {
                final NetworkInterface cur = interfaces.nextElement();

                if (cur.isLoopback() || cur.isVirtual())
                    continue;

                if (cur.getName().startsWith("eth"))
                {
                    for (final InterfaceAddress addr : cur.getInterfaceAddresses())
                    {
                        final InetAddress inet_addr = addr.getAddress();

                        if (!(inet_addr instanceof Inet4Address))
                            continue;

                        localAddr = inet_addr.getHostAddress();
                        broadcastAddress = addr.getBroadcast().getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void endpointAdded(EndpointDescription endpointDescription, String s)
    {
        System.out.println("Endpoint added");

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            Gson gson = new Gson();

            String message = gson.toJson(endpointDescription);
            InetAddress broadcastAddr = InetAddress.getByName(broadcastAddress);
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, broadcastAddr, 9191);

            socket.send(packet);

            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endpointRemoved(EndpointDescription endpointDescription, String s)
    {
        //TODO
    }
}