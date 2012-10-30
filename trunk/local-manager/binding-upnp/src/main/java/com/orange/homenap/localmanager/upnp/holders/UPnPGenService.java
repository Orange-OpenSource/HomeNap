/*
 * --------------------------------------------------------
 *  Module Name : local-manager-upnp
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
 *  File Name   : UPnPGenService.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */
package com.orange.homenap.localmanager.upnp.holders;

import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPService;
import org.osgi.service.upnp.UPnPStateVariable;


/**
 * this class represents a service for a UPnP Device
 * @author  Marius Legros TANKEU DE KUIGWA
 */
public class UPnPGenService implements UPnPService{
	/**
	 * @uml.property  name="id"
	 */
	private String               id;
	/**
	 * @uml.property  name="type"
	 */
	private String               type;
	/**
	 * @uml.property  name="version"
	 */
	private String               version;
	/**
	 * @uml.property  name="controlURL"
	 */
	private String               controlURL;
	/**
	 * @uml.property  name="eventSubUrl"
	 */
	private String               eventSubUrl;	
	/**
	 * @uml.property  name="serviceName"
	 */
	private String               serviceName;
	/**
	 * @uml.property  name="actions"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private UPnPAction[]        actions;
	/**
	 * @uml.property  name="stateVariables"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private UPnPStateVariable[] stateVariables;
	
	public UPnPGenService(String id, String type) {
		this.id = id;
		this.type = type;
	}
	public UPnPGenService(String id, String type, String controlUrl, String eventSubUrl, UPnPGenAction [] actions, UPnPGenStateVariable [] stateVariables) { 
		this.id = id;
		this.type = type;
		this.actions=actions;
		this.stateVariables=stateVariables;
		this.controlURL=controlUrl;
		this.eventSubUrl=eventSubUrl;
		this.serviceName=Utilities.getNameFromType(type);		
		this.version = Utilities.getVersion(id);
	}
	public UPnPGenService(UPnPService service) { 
		this.id = service.getId();
		this.type = service.getType();
		this.actions=service.getActions();
		this.stateVariables=service.getStateVariables();			
		this.serviceName=Utilities.getNameFromType(service.getType());		
		this.version = Utilities.getVersion(service.getId());
	}
	
	/**
	 * @return  Returns the actions.
	 * @uml.property  name="actions"
	 */
	public UPnPAction[] getActions() {
		return actions;
	}
	/**
	 * @param actions  The actions to set.
	 * @uml.property  name="actions"
	 */
	public void setActions(UPnPAction[] actions) {
		this.actions = actions;
	}
	/**
	 * @return  Returns the id.
	 * @uml.property  name="id"
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id  The id to set.
	 * @uml.property  name="id"
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return  Returns the stateVariables.
	 * @uml.property  name="stateVariables"
	 */
	public UPnPStateVariable[] getStateVariables() {
		return stateVariables;
	}
	/**
	 * @param stateVariables  The stateVariables to set.
	 * @uml.property  name="stateVariables"
	 */
	public void setStateVariables(UPnPStateVariable[] stateVariables) {
		this.stateVariables = stateVariables;
	}
	/**
	 * @return  Returns the type.
	 * @uml.property  name="type"
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type  The type to set.
	 * @uml.property  name="type"
	 */
	public void setType(String type) {
		this.type = type;
		this.serviceName=Utilities.getNameFromType(type);
	}
//	public String getVersion() {
//		return version;
//	}
//	public void setVersion(String version) {
//		this.version = version;
//	}
	public UPnPAction getAction(String id) {
		for(int i = 0; i < actions.length; i++) {
			if (id.equals(actions[i].getName()))
				return actions[i];
		}
		return null;
	}
	public UPnPStateVariable getStateVariable(String id) {
		for(int i = 0; i < stateVariables.length; i++) {
			if (id.equals(stateVariables[i].getName()))
				return stateVariables[i];
		}
		return null;
	}

	/**
	 * @return  Returns the controlURL.
	 * @uml.property  name="controlURL"
	 */
	public String getControlURL() {
		return controlURL;
	}

	/**
	 * @param controlURL  The controlURL to set.
	 * @uml.property  name="controlURL"
	 */
	public void setControlURL(String controlURL) {
		this.controlURL = controlURL;
	}

	/**
	 * @return  Returns the eventSubUrl.
	 * @uml.property  name="eventSubUrl"
	 */
	public String getEventSubUrl() {
		return eventSubUrl;
	}

	/**
	 * @param eventSubUrl  The eventSubUrl to set.
	 * @uml.property  name="eventSubUrl"
	 */
	public void setEventSubUrl(String eventSubUrl) {
		this.eventSubUrl = eventSubUrl;
	}

	/**
	 * @return  Returns the serviceName.
	 * @uml.property  name="serviceName"
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName  The serviceName to set.
	 * @uml.property  name="serviceName"
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public boolean isHolderNeeded() {
		for (int i = 0; i < actions.length; i++) {
			if(((UPnPGenAction) actions[i]).getOutArguments()!=null) {
			if(((UPnPGenAction) actions[i]).getOutArguments().length>1) return true;
			}
		}
		return false;
	}
	
	public boolean isListenerNeeded() {
		boolean isEvented=false;
		UPnPGenStateVariable[] vars=(UPnPGenStateVariable[]) getStateVariables();
		for (int j = 0; j < vars.length; j++) {
			isEvented|=vars[j].sendsEvents();
		}
		return isEvented;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}

