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
 *  File Name   : TimerThread.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.powerstate;

import com.orange.homenap.powerstate.listener.NotifyingThread;

public class TimerThread extends NotifyingThread
{
    private final Object lock = new Object();
    private volatile boolean suspend = false, stop = false;

    public Integer timer;

    public TimerThread(Integer timer)
    {
        this.timer = timer;
    }

    public void doRun()
    {
        while(timer >= 0 && !stop)
        {
            if(suspend)
            {
                synchronized (lock)
                {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timer--;
        }
    }

    public void doStop()
    {
        stop = true;
    }

    public void doSuspend()
    {
        suspend = true;
    }

    public void doResume()
    {
        suspend = false;

        synchronized (lock)
        {
            lock.notifyAll();
        }
    }

    public boolean threadSuspended()
    {
        return suspend;
    }
}
