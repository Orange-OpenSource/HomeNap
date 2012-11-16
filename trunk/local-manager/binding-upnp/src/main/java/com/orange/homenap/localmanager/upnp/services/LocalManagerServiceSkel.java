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

package com.orange.homenap.localmanager.upnp.services;


import com.orange.homenap.api.ILocalManagerService;
import com.orange.homenap.localmanager.upnp.holders.*;

public class LocalManagerServiceSkel extends UPnPGenService{
	public static final String ID      = "urn:upnp-org:serviceId:LocalManager.1";
	public static final String TYPE    = "urn:schemas-upnp-org:service:LocalManager:1";
	public static final String VERSION = "1";
	//		Reference to some implementation of functionality which implements ILocalManager
	private ILocalManagerService impl;
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
	public LocalManagerServiceSkel(ILocalManagerService impl) {
		super(ID,TYPE);
		this.impl=impl;
		setStateVariables(LocalManagerVariableProperties.createUPnPStateVariables());
		setActions(LocalManagerActionProperties.createUPnPActions(this));
	}
	public LocalManagerServiceSkel() {
		super(ID,TYPE);
		setStateVariables(LocalManagerVariableProperties.createUPnPStateVariables());
		setActions(LocalManagerActionProperties.createUPnPActions(this));
	}
	public ILocalManagerService getImpl() {
		return impl;
	}
	public void setImpl(ILocalManagerService impl) {
		this.impl=impl;
	}
}
