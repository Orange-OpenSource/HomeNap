/*
 * --------------------------------------------------------
 * Module Name : binding-upnp-gc
 * Version : 0.1-SNAPSHOT
 *
 * Software Name : $projectName
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

package com.orange.homenap.globalcoordinator.upnp.services;


import com.orange.homenap.api.IGlobalCoordinatorService;
import com.orange.homenap.globalcoordinator.upnp.holders.*;
import com.orange.homenap.globalcoordinator.upnp.services.*;

    public class GlobalCoordinatorServiceSkel extends UPnPGenService{
	public static final String ID      = "urn:upnp-org:serviceId:GlobalCoordinator.1";
	public static final String TYPE    = "urn:schemas-upnp-org:service:GlobalCoordinator:1";
	public static final String VERSION = "1";
	//		Reference to some implementation of functionality which implements IGlobalCoordinator
	private IGlobalCoordinatorService impl;
	/* List of evented state variables
	 * 
	 * For a list of all state variables (evented and non-evented ones), use: 
	 *    UPnPStateVariable [] stateVariables = this.getStateVariables();
	 * 
	 * For updating a statevariable, use: 
	 *    stateVariable.setValue(newValue);
	 * Example: 
	 *    target.setValue(new java.lang.Boolean(0));
	 * 
	 * If the updated state variable is evented (i.e. sendsEvents() returns true),
	 * an event is automatically generated. 
	 */ 
	public GlobalCoordinatorServiceSkel(IGlobalCoordinatorService impl) {
		super(ID,TYPE);
		this.impl=impl;
		setStateVariables(GlobalCoordinatorVariableProperties.createUPnPStateVariables());
		setActions(GlobalCoordinatorActionProperties.createUPnPActions(this));
	}
	public GlobalCoordinatorServiceSkel() {
		super(ID,TYPE);
		setStateVariables(GlobalCoordinatorVariableProperties.createUPnPStateVariables());
		setActions(GlobalCoordinatorActionProperties.createUPnPActions(this));
	}
	public IGlobalCoordinatorService getImpl() {
		return impl;
	}
	public void setImpl(IGlobalCoordinatorService impl) {
		this.impl=impl;
	}
}
