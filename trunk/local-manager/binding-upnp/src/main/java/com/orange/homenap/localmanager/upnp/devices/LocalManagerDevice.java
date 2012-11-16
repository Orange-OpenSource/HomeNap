/*
 * --------------------------------------------------------
 * Module Name : binding-upnp
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
 * File Name   : ${NAME}
 *
 * Created     :
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.upnp.devices;


import java.util.Vector;
import java.util.Iterator;
import java.util.Properties;

import com.orange.homenap.api.ILocalManagerService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPService;
import com.orange.homenap.localmanager.upnp.services.*;
import com.orange.homenap.localmanager.upnp.holders.*;

public class LocalManagerDevice {
    UPnPDevice device;
    Vector registrations=new Vector();

    LocalManagerService localManager1;


    public LocalManagerDevice(UPnPDevice device,BundleContext context) {
        this.device=device;
        String [] udns = (String[]) device.getDescriptions(null).get(UPnPDevice.CHILDREN_UDN);
        String id = (String) device.getDescriptions(null).get(UPnPDevice.ID);
        if(udns!=null) {
            for (int i = 0; i < udns.length; i++) {
                try {
                    ServiceReference ref=context.getServiceReferences(UPnPDevice.class.getName(),"(UDN=\""+udns[i]+"\")")[0];
                    UPnPDevice dev=(UPnPDevice) context.getService(ref);
                } catch (InvalidSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        UPnPService [] services = device.getServices();
        if(services!=null) {
            for(int j=0; j<services.length; j++) {
                UPnPService service = (UPnPService) services[j];
                String serviceType = service.getType();
                String serviceId = service.getId();
                if(serviceType.equalsIgnoreCase("urn:schemas-upnp-org:service:LocalManager:1")) {
                    if(serviceId.equalsIgnoreCase("urn:upnp-org:serviceId:LocalManager.1")) {
                        localManager1 = new LocalManagerService(device.getService(serviceId),id,context);
                        registrations.add(context.registerService(new String[]{ILocalManagerService.class.getName(),LocalManagerService.class.getName()},localManager1,null));
                    }
                    else
                    if(localManager1==null) {
                        localManager1 = new LocalManagerService(device.getService(serviceId),id,context);
                        registrations.add(context.registerService(new String[]{ILocalManagerService.class.getName(),LocalManagerService.class.getName()},localManager1,null));
                    }
                }
            }
        }
        registrations.add(context.registerService(LocalManagerDevice.class.getName(),this,device.getDescriptions(null)));
    }

    public UPnPDevice getGenericDevice() {
        return device;
    }
    public String getId() {
        return (String) device.getDescriptions(null).get(UPnPDevice.ID);
    }
    public LocalManagerService getLocalManager1() {
        return localManager1;
    }

    public void dispose() {
        for (Iterator iter = registrations.iterator(); iter.hasNext();) {
            ServiceRegistration element = (ServiceRegistration) iter.next();
            element.unregister();
        }
        registrations=new Vector();
    }

    public static ServiceRegistration expose(BundleContext context, ILocalManagerService localmanager0, UPnPDevice [] devices) {
        if(context==null||localmanager0==null) {
            try {
                throw new UPnPGenException("Cannot expose a device with a null context or without all required services!!!");
            } catch (UPnPGenException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        } else {
            UPnPGenDevice dev= new UPnPGenDevice(context);
            dev.addService(new LocalManagerServiceSkel(localmanager0));
            if(devices!=null) {
                for (int i = 0; i < devices.length; i++) {
                    dev.addDevice(devices[i]);
                }
            }
            Properties props=new Properties();
            dev.setProps(LocalManagerDeviceProperties.createProperties(props));
            return context.registerService(UPnPDevice.class.getName(),dev,dev.getProps());
        }
    }

    public static ServiceRegistration expose(BundleContext context, String uuid, ILocalManagerService localmanager0, UPnPDevice [] devices) {
        if(context==null||localmanager0==null) {
            try {
                throw new UPnPGenException("Cannot expose a device with a null context or without all required services!!!");
            } catch (UPnPGenException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        } else {
            UPnPGenDevice dev= new UPnPGenDevice(context);
            dev.addService(new LocalManagerServiceSkel(localmanager0));
            if(devices!=null) {
                for (int i = 0; i < devices.length; i++) {
                    dev.addDevice(devices[i]);
                }
            }
            Properties props=new Properties();
            props=LocalManagerDeviceProperties.createProperties(props);
            props.put(UPnPDevice.UDN, UPnPGenDevice.UUID_PREFIX + uuid);
            dev.setProps(props);
            return context.registerService(UPnPDevice.class.getName(),dev,dev.getProps());
        }
    }
}
