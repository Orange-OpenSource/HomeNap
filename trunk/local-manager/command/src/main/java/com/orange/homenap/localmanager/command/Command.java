/*
 * --------------------------------------------------------
 *  Module Name : command
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
 *  File Name   : Command.java
 *
 *  Created     : 05/11/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.command;

import com.orange.homenap.localmanager.architecturereader.ArchitectureReaderItf;
import org.apache.felix.ipojo.annotations.*;
import org.apache.felix.service.command.Descriptor;

@Component(immediate = true)
@Instantiate
@Provides(specifications = Command.class)
public class Command
{
    @ServiceProperty(name = "osgi.command.scope", value = "startservice")
    String scope;

    @ServiceProperty(name = "osgi.command.function", value = "{}")
    String[] function = new String[] { "startService" };

    @Requires
    private ArchitectureReaderItf architectureReaderItf;

    @Descriptor("startservice")
    public void startService(String architectureFile)
    {
        System.out.println("Starting " + architectureFile);

        architectureReaderItf.startService(architectureFile);
    }
}
