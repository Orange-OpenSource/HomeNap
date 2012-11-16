/*
 * --------------------------------------------------------
 * Module Name : executer
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
 * File Name   : Executer.java
 *
 * Created     : 09/11/2012
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.globalcoordinator.executer;

import com.orange.homenap.localmanager.controlpointfactory.ControlPointFactoryItf;
import com.orange.homenap.localmanager.controlpointfactory.LocalManagerControlPointItf;
import com.orange.homenap.utils.Action;

import java.util.List;
import java.util.Map;

public class Executer implements ExecuterItf
{
    private ControlPointFactoryItf controlPointFactoryItf;

    public void executeActions(Map<String, List<Action>> actions)
    {
        for(Map.Entry<String, List<Action>> entry : actions.entrySet())
        {
            LocalManagerControlPointItf lmcpi = controlPointFactoryItf.createCP(entry.getKey());

            if(lmcpi != null)
                lmcpi.actions(entry.getValue());
        }
    }
}
