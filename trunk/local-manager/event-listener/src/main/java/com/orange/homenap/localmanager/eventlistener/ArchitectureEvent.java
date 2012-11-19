/*
 * --------------------------------------------------------
 *  Module Name : event-listener
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
 *  File Name   : ComponentEvent.java
 *
 *  Created     : 06/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.eventlistener;

import com.orange.homenap.utils.Architecture;
import com.orange.homenap.utils.Component;

import java.util.List;

public interface ArchitectureEvent
{
    public void startArchitecture(Architecture architecture, List<Component> components);

    public void stopArchitecture(String name);

    public void architectureStarted(Architecture architecture);

    public void architectureStopped(Architecture architecture);
}
