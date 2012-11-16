/*
 * --------------------------------------------------------
 *  Module Name : upnp-manager
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
 *  File Name   : LMDevice.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.upnpdevicemanager;

import com.google.gson.Gson;
import com.orange.homenap.api.ILocalManagerService;
import com.orange.homenap.localmanager.bundlemanager.BundleManagerItf;
import com.orange.homenap.localmanager.deviceinfo.DeviceInfoItf;
import com.orange.homenap.localmanager.eventlistener.MigrationEvent;
import com.orange.homenap.localmanager.upnp.devices.LocalManagerDevice;
import com.orange.homenap.utils.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LMDevice implements ILocalManagerService
{
    // iPOJO requires
    private DeviceInfoItf deviceInfoItf;
    private MigrationEvent migrationEvent;
    private BundleManagerItf bundleManagerItf;

    // iPOJO injection
    private BundleContext bundleContext;

    // Global variables
    private ServiceRegistration serviceRegistration;

    public LMDevice(BundleContext bc)
    {
        this.bundleContext = bc;
    }

    public void start()
    {
        // Expose this device via UPnP
        serviceRegistration = LocalManagerDevice.expose(bundleContext, this, null);

        deviceInfoItf.setUDN((String) serviceRegistration.getReference().getProperty("UPnP.device.UDN"));

        System.out.println("My ID is: " + deviceInfoItf.getDevice().getId());
    }

    public void stop()
    {
        System.out.println("Stopping Local Manager Device");

        serviceRegistration.unregister();
    }

    public void start(String bundleUrl, String migrationState) throws Exception
    {
        bundleManagerItf.start(bundleUrl);
    }

    public void actionsToTake(String actions) throws Exception
    {
        System.out.println("Receiving actions to take");

        Gson gson = new Gson();

        System.out.println(actions);

        //TODO: to change in order to remove Actions.class
        //Type listType = new TypeToken<List<Action>>(){}.getType();

        Actions actionList = gson.fromJson(actions, Actions.class);
        //ActionList actionList = gson.fromJson(actions, ActionList.class);

        Iterator<Action> itAction = actionList.getActions().iterator();

        /*while(itAction.hasNext())
        {
            Action action = itAction.next();
            List<Property> properties = action.getProperties();

            Iterator<Property> itProperty = properties.iterator();

            while(itProperty.hasNext())
            {
                Property property = itProperty.next();

                System.out.println(property.getValue());

                property.setValue(getProperties(property.getValue(), property.getType()));
            }
        }*/

        migrationEvent.actionsToTake(actionList);
        //migrationEvent.actionsToTake(actionList);
    }

    private Object getProperties(Object object, String typeStr)
    {
        System.out.println("Inside " + typeStr);

        Class classe = typeConverter(typeStr);
        
        Object result = null;

        Gson gson = new Gson();

        if(typeStr.contains("java.util.ArrayList"))
        {
            List<Property> propertyList = gson.fromJson(object.toString(), List.class);

            Iterator<Property> it = propertyList.iterator();

            while(it.hasNext())
                System.out.println(it.next());

            //List<Property> list = (ArrayList) property.getValue();

            /*Iterator<Property> it = propertyList.iterator();

            while(it.hasNext())
                System.out.println(it.next().getValue());*/

            //result = getProperties(property.getValue(), property.getType());
        }
        else
        {
            Property property = gson.fromJson(object.toString(), Property.class);
            result = gson.fromJson(object.toString(), classe);
        }

/*        Gson gson = new Gson();

        System.out.println(object.toString());

        Object result = gson.fromJson(object.toString(), classe);*/

/*        System.out.println(result.toString());

        if(result instanceof List)
        {
            System.out.println("It's a list");

            Property property = (Property) object;

            result = getProperties(property.getValue(), property.getType());
        }*/

        return result;
    }

    private Class typeConverter(String typeStr)
    {
        Class classe = null;

        if(typeStr.contains("java.lang.String"))
            classe = String.class;
        else if(typeStr.contains("java.lang.Integer"))
            classe = Integer.class;
        else if(typeStr.contains("java.util.List"))
            classe = List.class;
        else if(typeStr.contains("java.util.ArrayList"))
            classe = ArrayList.class;
        else if(typeStr.contains("com.orange.homenap.utils.Resource"))
            classe = Resource.class;
        else if(typeStr.contains("com.orange.homenap.utils.Device"))
            classe = Device.class;
        else if(typeStr.contains("com.orange.homenap.utils.Property"))
            classe = Property.class;
        else if(typeStr.contains("com.orange.homenap.utils.Component"))
            classe = Component.class;
        else if(typeStr.contains("com.orange.homenap.utils.Architecture"))
            classe = Architecture.class;
        else if(typeStr.contains("com.orange.homenap.utils.Action"))
            classe = Action.class;
        else if(typeStr.contains("com.orange.homenap.utils.Actions"))
            classe = Actions.class;

        return classe;
    }
}
