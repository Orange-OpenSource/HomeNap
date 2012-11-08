/*
 * --------------------------------------------------------
 *  Module Name : power-state
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
 *  File Name   : PowerStateEvent.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.powerstate;

import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

import java.io.*;
import java.util.Dictionary;
import java.util.Properties;

public class PowerStateEvent
{
    // iPOJO properties
    public String stateFile;

    // iPOJO Publish-Subscribe
    public Publisher publisher;

    public void start()
    {
        File file = new File(stateFile);
        String stateEvent = null;

        while (true)
        {
            if(file.exists())
            {
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);

                    String line;

                    while((line = br.readLine()) != null)
                    {
                        if(line.equals("resume") && !stateEvent.equals("resume"))
                        {
                            Dictionary e = new Properties();
                            e.put("state-event", "resume");
                            publisher.send(e);

                            stateEvent = "resume";
                        }
                        else if(line.equals("suspend") && !stateEvent.equals("suspend"))
                        {
                            Dictionary e = new Properties();
                            e.put("state-event", "suspend");
                            publisher.send(e);

                            stateEvent = "suspend";
                        }
                        else if(line.equals("hibernate") && !stateEvent.equals("hibernate"))
                        {
                            Dictionary e = new Properties();
                            e.put("state-event", "hibernate");
                            publisher.send(e);

                            stateEvent = "hibernate";
                        }
                        else if(line.equals("thaw") && !stateEvent.equals("thaw"))
                        {
                            Dictionary e = new Properties();
                            e.put("state-event", "thaw");
                            publisher.send(e);

                            stateEvent = "thaw";
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
