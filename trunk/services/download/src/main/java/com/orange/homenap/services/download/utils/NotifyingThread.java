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
 *  File Name   : NotifyingThread.java
 *
 *  Created     : 15/10/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.services.download.utils;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class NotifyingThread extends Thread
{
    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

    public final void addListener(final ThreadCompleteListener listener)
    {
        listeners.add(listener);
    }

    public final void removeListener(final ThreadCompleteListener listener)
    {
        listeners.remove(listener);
    }

    private final void notifyListeners()
    {
        for (ThreadCompleteListener listener : listeners)
            listener.notifyOfThreadComplete(this);
    }

    @Override
    public final void run()
    {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }

    public abstract void doRun();
}
