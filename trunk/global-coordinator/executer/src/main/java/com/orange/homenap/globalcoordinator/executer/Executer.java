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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Executer implements ExecuterItf
{
    private ControlPointFactoryItf controlPointFactoryItf;

    public void executeActions(Map<String, List<Action>> actions)
    {
        if(actions.get("VirtualDevice") != null)
        {
            for(Action action : actions.get("VirtualDevice"))
            {
                if(actions.containsKey(action.getToDevice().getId()))
                {
                    action.setActionName(Action.ActionName.START);

                    actions.get(action.getToDevice().getId()).add(action);
                }
                else
                {
                    List<Action> tmp = new ArrayList<Action>();

                    action.setActionName(Action.ActionName.START);

                    tmp.add(action);

                    actions.put(action.getToDevice().getId(), tmp);
                }
            }

            actions.remove("VirtualDevice");
        }

        for(Map.Entry<String, List<Action>> entry : actions.entrySet())
        {
            List<Action> actionList = entry.getValue();

            LocalManagerControlPointItf lmcpi = controlPointFactoryItf.createCP(entry.getKey());

            while (!lmcpi.deviceExist())
            {
                System.out.println("Control Point is not ready. Waiting 1 sec.");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(lmcpi.deviceExist())
            //if(lmcpi != null)
                lmcpi.actions(actionList);
        }
    }
}
