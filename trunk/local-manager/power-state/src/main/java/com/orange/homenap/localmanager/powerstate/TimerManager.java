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
 *  File Name   : TimerManager.java
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

public class TimerManager implements TimerManagerItf, ThreadCompleteListener
{
    private TimerThread thread;

    public TimerThread start(Integer duration)
    {
        thread = new TimerThread(duration);
        thread.addListener(this);
        thread.start();

        return thread;
    }

    public void stop()
    {
        if(thread.isAlive())
            thread.doStop();
    }

    public void suspend()
    {
        if(!thread.threadSuspended())
            thread.doSuspend();
    }

    public void resume()
    {
        if(thread.threadSuspended())
            thread.doResume();
    }

    public void notifyOfThreadComplete(Thread thread)
    {

    }
}
