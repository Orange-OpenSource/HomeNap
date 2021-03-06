/**
##############################################################################
# Copyright (C) 2004-2007 France Telecom R&D
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
##############################################################################
 * 
 */
package com.orange.homenap.globalcoordinator.upnp.holders;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.upnp.UPnPDevice;
import org.osgi.service.upnp.UPnPIcon;
import org.osgi.service.upnp.UPnPService;
import org.osgi.service.upnp.UPnPStateVariable;


/**
 * This class represnets an UPnP Device in a XML device decription
 * @author  Marius Legros TANKEU DE KUIGWA
 */
public class UPnPGenDevice implements UPnPDevice{
	
	final public static String UUID_PREFIX = "uuid:";
	/**
	 * the device properties(dvicName,manufacturer,model etc..)
	 * @uml.property  name="props"
	 */
	private Properties props;
	private String deviceUDN;
	/**
	 * the UPnP Device services
	 * @uml.property  name="services"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
    private UPnPGenService[] services;    
    /**
	 * The UPnP Device icons
	 * @uml.property  name="icons"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
    private UPnPGenIcon[] icons;
    /**
	 * The UPnP device embedded devices
	 * @uml.property  name="devices"
	 */
    private Vector devices=new Vector();
    private Vector _services=new Vector();
    /**
	 * The embedded devices same type map
	 * @uml.property  name="sameTypeDevices"
	 */
    private Map sameTypeDevices=new HashMap();    
    /**
	 * The embedded devices same type map
	 * @uml.property  name="sameTypeServices"
	 */
    private Map sameTypeServices=new HashMap();
	private BundleContext context;  
	private boolean isNotifierCreated=false;

	public UPnPGenDevice(UPnPGenService[] services, UPnPGenIcon[] icons, Properties props) {
		setProps(props);
        setServices(services);
        setIcons(icons);
        this.devices = new Vector();        
        this.deviceUDN = props.getProperty(UPnPDevice.UDN);
    }
	
	public UPnPGenDevice(UPnPGenService[] services, UPnPGenIcon[] icons, Properties props, BundleContext context) {
		setProps(props);
        setServices(services);
        setIcons(icons);
        this.devices = new Vector();        
        this.deviceUDN = props.getProperty(UPnPDevice.UDN);
        this.context=context;
        createNotifier();
    }
	
	 
	public UPnPGenDevice(BundleContext context) {
		this.devices = new Vector();
		this.props = new Properties();
		this.context=context;		
	}

	public UPnPService getService(String serviceId) {
		for(int i = 0; i < services.length; i++) {
            if (serviceId.equals(services[i].getId()))
            	return services[i];
        }
        return null;
	}

	/**
	 * @return  Returns the sameTypeDevices.
	 * @uml.property  name="sameTypeDevices"
	 */
	public Map getSameTypeDevices() {
		return sameTypeDevices;
	}

	/**
	 * @param sameTypeDevices  The sameTypeDevices to set.
	 * @uml.property  name="sameTypeDevices"
	 */
	public void setSameTypeDevices(Map sameType) {
		this.sameTypeDevices = sameType;
	}

	/**
	 * @return  Returns the services.
	 * @uml.property  name="services"
	 */
	public UPnPService[] getServices() {
		return services;
	}

	public UPnPIcon[] getIcons(String arg0) {
		return icons;
	}

	public Dictionary getDescriptions(String arg0) {
		return props;
	}

	/**
	 * @return  Returns the devices.
	 * @uml.property  name="devices"
	 */
	public Vector getDevices() {
		return devices;
	}

	/**
	 * @param devices  The devices to set.
	 * @uml.property  name="devices"
	 */
	public void setDevices(Vector devices) {
		this.devices = devices;
		Vector v=new Vector();
		for (Iterator iter = devices.iterator(); iter.hasNext();) {			
			UPnPGenDevice element = (UPnPGenDevice) iter.next();
			if(!v.contains(element.getProps().getProperty("deviceType"))) {
				v.addElement(element.getProps().getProperty("deviceType"));
			}
		}
		
		for (Iterator iter = v.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			Vector h=new Vector();
			for (Iterator ite = devices.iterator(); ite.hasNext();) {			
				UPnPGenDevice el = (UPnPGenDevice) ite.next();
				if(element.equals(el.getProps().getProperty("deviceType"))) {
					h.addElement(el);
				}
			}
			sameTypeDevices.put(element,h);
		}
	}

	/**
	 * @return  Returns the icons.
	 * @uml.property  name="icons"
	 */
	public UPnPGenIcon[] getIcons() {
		return icons;
	}

	/**
	 * @param icons  The icons to set.
	 * @uml.property  name="icons"
	 */
	public void setIcons(UPnPGenIcon[] icons) {
		this.icons = icons;
	}

	/**
	 * @return  Returns the props.
	 * @uml.property  name="props"
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * @param props  The props to set.
	 * @uml.property  name="props"
	 */
	public void setProps(Properties props) {
		this.props = props;
		if(props.getProperty("deviceType")!=null) {
			this.props.setProperty("deviceName",Utilities.getNameFromType(props.getProperty("deviceType")));
		}
		this.deviceUDN = props.getProperty(UPnPDevice.UDN);
		createNotifier();
	}

	/**
	 * @param services  The services to set.
	 * @uml.property  name="services"
	 */
	public void setServices(UPnPService[] servs) {
		this.services = new UPnPGenService[servs.length];		
        for (int i = 0; i < servs.length; i++) {
			this.services[i]=new UPnPGenService(servs[i]);
		}
		for (int i = 0; i < services.length; i++) {
			_services.add((UPnPService)services[i]);
		}
		Vector v=new Vector();
		for (int i = 0; i < services.length; i++) {
			UPnPGenService element = (UPnPGenService) services[i];
			if(!v.contains(element.getType())) {
				v.addElement(element.getType());
			}
		}		
		
		for (Iterator iter = v.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			Vector h=new Vector();
			for (int i = 0; i < services.length; i++) {			
				UPnPGenService el = (UPnPGenService) services[i];
				if(element.equals(el.getType())) {
					h.addElement(el);
				}
			}
			sameTypeServices.put(element,h);
		}
		isNotifierCreated=false;
		createNotifier();
	}
	
	public void addService(UPnPService service) {
        _services.add(service);        
        services=new UPnPGenService[_services.size()];
        UPnPService[] servs = (UPnPService[]) _services.toArray(new UPnPService[0]);
        for (int i = 0; i < servs.length; i++) {
			services[i]=new UPnPGenService(servs[i]);
		}        
        isNotifierCreated=false;
        createNotifier();
	}
	/**
	 * @param services  Returns the sameTypeServices
	 * @uml.property  name="sameTypeServices"
	 */
	public Map getSameTypeServices() {
		return sameTypeServices;
	}
	
	/**
	 * @param services  The sameTypeServices to set.
	 * @uml.property  name="sameTypeServices"
	 */	
	public void setSameTypeServices(Map sameTypeServices) {
		this.sameTypeServices = sameTypeServices;
	}
	
	public void addDevice(UPnPDevice device) {
        devices.add(device);
        System.out.println("Registering embedded Device " + device.getDescriptions("en").toString());
        context.registerService((UPnPDevice.class).getName(),
                device, device.getDescriptions("en"));
    }

	private void createNotifier() {
		if(!this.isNotifierCreated) {
			UPnPGenNotifier notifier;
			UPnPService [] services = getServices();  	    	
			if(deviceUDN!=null && services != null && context!=null && services.length > 0) {
				for(int i=0; i<services.length; i++) {
					notifier = new UPnPGenNotifier(deviceUDN , services[i],context);
					UPnPStateVariable [] stateVariables = services[i].getStateVariables();
					
					if(stateVariables != null && stateVariables.length > 0) {
						for(int j=0; j<stateVariables.length; j++) {
							((UPnPGenStateVariable) stateVariables[j]).setNotifier(notifier);	
						}
					}
				}
				this.isNotifierCreated=true;
			}
		}
	}
	public ServiceRegistration expose() {
		this.props.put(UPnPDevice.UPNP_EXPORT, "");
		this.props.put(
				org.osgi.service
				.device.Constants.DEVICE_CATEGORY,
				new String[]{UPnPDevice.DEVICE_CATEGORY}
		);
		return context.registerService(UPnPDevice.class.getName(),this,props);
	}
}


