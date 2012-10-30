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
 *  File Name   : PowerStateManager.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.powerstate;

import com.orange.homenap.localmanager.powerstate.listener.ThreadCompleteListener;
import org.apache.felix.ipojo.handlers.event.publisher.Publisher;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

public class PowerStateManager implements PowerStateManagerItf, ThreadCompleteListener
{
    // iPOJO requires
    private TimerManagerItf timerManagerItf;

    // iPOJO properties
    private Integer timer;
    private Integer grace;

    // iPOJO Publish-Subscribe
    private Publisher publisher;

    // Global variables
    private boolean preparingSuspend;
    private boolean reset;
    private boolean lock;
    private Integer lockAsked;
    private TimerThread thread;

    public void start()
    {
        System.out.println("Starting timer. Sleep in " + timer + " seconds.");

        this.preparingSuspend = false;
        this.reset = false;
        this.lock = false;
        this.lockAsked = 0;

        thread = timerManagerItf.start(timer);
        thread.addListener(this);
    }

    public void stop()
    {
        timerManagerItf.stop();
    }

    public void resetTimer()
    {
        this.resetTimer(this.timer);
    }

    public void resetTimer(Integer timer)
    {
        preparingSuspend = false;
        reset = true;

        timerManagerItf.stop();

        System.out.println("Resetting timer. Sleep in " + timer + " seconds.");

        thread = timerManagerItf.start(timer);
        thread.addListener(this);
    }

    public void suspendStateChange()
    {
        System.out.println("Suspending timer.");

        lockAsked++;

        timerManagerItf.suspend();
    }

    public void releaseStateChange()
    {
        lockAsked--;

        if((lockAsked == 0))
        {
            timerManagerItf.resume();
            lock = false;
        }

        System.out.println("Releasing timer.");
    }

    public void notifyOfThreadComplete(Thread lastThread)
    {
        if(!preparingSuspend && !reset)
        {
            Dictionary e = new Properties();

            publisher.send(e);

            System.out.println("Entering grace period.");

            preparingSuspend = true;

            thread = new TimerThread(grace);
            thread.addListener(this);
            thread.start();
        }
        else if (preparingSuspend && !reset)
        {
            while (lock);

            System.out.println("Suspending !");

            preparingSuspend = false;

            Runtime runtime = Runtime.getRuntime();

            try {
                runtime.exec("pm-suspend");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        reset = false;
    }
}
