/*
 * --------------------------------------------------------
 * Module Name : local-executer
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
 * File Name   : LocalExecuter.java
 *
 * Created     : 14/11/2012
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.localexecuter;

import com.orange.homenap.localmanager.bundlemanager.BundleManagerItf;
import com.orange.homenap.localmanager.controlpointfactory.ControlPointFactoryItf;
import com.orange.homenap.localmanager.controlpointfactory.LocalManagerControlPointItf;
import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.localmanager.localdatabase.LocalDatabaseItf;
import com.orange.homenap.localmanager.repositorymanager.RepositoryManagerItf;
import com.orange.homenap.utils.Action;
import com.orange.homenap.utils.Component;

import java.util.*;

public class LocalExecuter implements LocalExecuterItf
{
    private ControlPointFactoryItf controlPointFactoryItf;
    private BundleManagerItf bundleManagerItf;
    private LocalDatabaseItf localDatabaseItf;
    private DeviceInfoItf deviceInfoItf;
    private RepositoryManagerItf repositoryManagerItf;

    public void migrate(List<Action> actions)
    {
        Map<String, List<Action>> devicesActions = new HashMap<String, List<Action>>();

        for(Action action : actions)
        {
            Component component = action.getComponent();

            System.out.println(action.getActionName() + " " + component.getName() + " to " + action.getToDevice().getId());

            deviceInfoItf.getDevice().getComponentsOnDevice().remove(component.getName());
            bundleManagerItf.stop(component.getName());

            Action newAction = new Action();

            newAction.setActionName(Action.ActionName.START);
            newAction.setComponent(localDatabaseItf.get(component.getName()));

            //System.out.println(localDatabaseItf.getProperties(component.getName()).size());

            if(localDatabaseItf.get(component.getName()).getState().equals(Component.State.STATEFUL))
                newAction.setProperties(localDatabaseItf.getProperties(component.getName()));

            if(devicesActions.containsKey(action.getToDevice().getId()))
                devicesActions.get(action.getToDevice().getId()).add(newAction);
            else 
            {
                List<Action> newActions = new ArrayList<Action>();
                newActions.add(newAction);
                
                devicesActions.put(action.getToDevice().getId(), newActions);
            }
        }

        for(Map.Entry<String, List<Action>> map : devicesActions.entrySet())
        {
            LocalManagerControlPointItf lmcpi = controlPointFactoryItf.createCP(map.getKey());

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
                lmcpi.actions(map.getValue());
        }
    }

    public void start(List<Action> actions)
    {
        for(Action action : actions)
        {
            Component component = action.getComponent();

            System.out.println(action.getActionName() + " " + component.getName());

            localDatabaseItf.put(action.getComponent().getName(), component);

            localDatabaseItf.put(action.getComponent().getName(), action.getProperties());

            deviceInfoItf.getDevice().getComponentsOnDevice().add(component.getName());

            bundleManagerItf.start(component.getUrl());

            String url = repositoryManagerItf.addBundleToRepository(component.getUrl());

            component.setUrl(url);
        }
    }

    public void stop(List<Action> actions)
    {
        for(Action action : actions)
        {
            System.out.println(action.getActionName() + " " + action.getComponent().getName());

            bundleManagerItf.stop(action.getComponent().getName());
            
            for(int i = 0; i < deviceInfoItf.getDevice().getComponentsOnDevice().size(); i++)
                if(deviceInfoItf.getDevice().getComponentsOnDevice().get(i).equals( action.getComponent().getName()))
                    deviceInfoItf.getDevice().getComponentsOnDevice().remove(i);
        }
    }
}
