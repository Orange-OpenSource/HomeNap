/*
 * --------------------------------------------------------
 *  Module Name : wakeonlan
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
 *  File Name   : WakeOnLan.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.services.wol;

/**
 * Inspired from http://www.jibble.org/wake-on-lan/ (2011)
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WakeOnLan implements WakeOnLanItf
{
    // iPOJO properties
    public String broadcast;
    public Integer port;

    public boolean wakeUp(String mac)
    {
        byte[] macBytes = getMacBytes(mac);
        byte[] bytes = new byte[6 + 16 * macBytes.length];

        for (int i = 0; i < 6; i++)
            bytes[i] = (byte) 0xff;

        for (int i = 6; i < bytes.length; i += macBytes.length)
            System.arraycopy(macBytes, 0, bytes, i, macBytes.length);

        try {
            InetAddress address = InetAddress.getByName(broadcast);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Wake-on-LAN " + mac + " packet sent.");

        return true;
    }

    public void setBroadcastAddress(String broadcast)
    {
        this.broadcast = broadcast;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException
    {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");

        if (hex.length != 6)
            throw new IllegalArgumentException("Invalid MAC address.");

        try {
            for (int i = 0; i < 6; i++)
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }

        return bytes;
    }
}
