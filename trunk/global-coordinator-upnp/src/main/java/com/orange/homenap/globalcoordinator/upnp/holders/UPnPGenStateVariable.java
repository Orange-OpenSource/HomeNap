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

import java.beans.PropertyChangeEvent;
import org.osgi.service.upnp.UPnPStateVariable;

/**
 * This class represents a state variable for a UPnP service
 * @author  Marius Legros TANKEU DE KUIGWA
 */
public class UPnPGenStateVariable implements UPnPStateVariable{
	/**
	 * @uml.property  name="name"
	 */
	private String name;
    private boolean sendsEvents;
    /**
	 * @uml.property  name="javaDataType"
	 */
    private Class javaDataType;
    private String upnpDataType;
    /**
	 * @uml.property  name="allowedValues"
	 */
    private String[] allowedValues;
    /**
	 * @uml.property  name="defaultValue"
	 */
    private Object defaultValue;
    /**
	 * @uml.property  name="minimum"
	 */
    private Number minimum;
    /**
	 * @uml.property  name="maximum"
	 */
    private Number maximum;
    /**
	 * @uml.property  name="step"
	 */
    private Number step;

    /**
	 * @uml.property  name="value"
	 */
    private Object value;
	private UPnPGenNotifier notifier;

//    private _IUPnPNotifier notifier;

    public UPnPGenStateVariable(String name, 
    		boolean sendsEvents, 
			Class javaDataType, 
			String upnpDataType, 
			String [] allowedValues, 
			Object defaultValue, 
			Number minimum, 
			Number maximum, 
			Number step) { 

    	this.name = name;
    	this.sendsEvents = sendsEvents;
    	this.javaDataType = javaDataType;
    	this.upnpDataType = upnpDataType;
    	this.allowedValues = allowedValues;
    	this.defaultValue = defaultValue;
    	this.minimum = minimum;
    	this.maximum = maximum;
    	this.step = step;

    	value = defaultValue;
    }

    /**
	 * @return  Returns the value.
	 * @uml.property  name="value"
	 */
    public Object getValue() {
    	return value;
    }

    /**
	 * @param value  The value to set.
	 * @uml.property  name="value"
	 */
    public void setValue(Object newValue) {
    	Object oldValue = value;
    	value = newValue;

    	if(sendsEvents == true && notifier != null && oldValue != newValue) {
    		notifier.propertyChange(new PropertyChangeEvent(this, name, oldValue, newValue));
    	}
    }

    /**
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
    public String getName() {
        return name;
    }

    public boolean sendsEvents() {
        return sendsEvents;
    }

    /**
	 * @return  Returns the javaDataType.
	 * @uml.property  name="javaDataType"
	 */
    public Class getJavaDataType() {
        return javaDataType;
    }

    public String getUPnPDataType() {
        return upnpDataType;
    }

    /**
	 * @return  Returns the allowedValues.
	 * @uml.property  name="allowedValues"
	 */
    public String[] getAllowedValues() {
        return allowedValues;
    }

    /**
	 * @return  Returns the defaultValue.
	 * @uml.property  name="defaultValue"
	 */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
	 * @return  Returns the minimum.
	 * @uml.property  name="minimum"
	 */
    public Number getMinimum() {
        return minimum;
    }

    /**
	 * @return  Returns the maximum.
	 * @uml.property  name="maximum"
	 */
    public Number getMaximum() {
        return maximum;
    }
    
    /**
	 * @return  Returns the step.
	 * @uml.property  name="step"
	 */
    public Number getStep() {
        return step;
    }    
    
    /**
	 * @param notifier  The notifier to set.
	 * @uml.property  name="notifier"
	 */
    public void setNotifier(UPnPGenNotifier notifier){
        this.notifier = notifier;
    }
}

