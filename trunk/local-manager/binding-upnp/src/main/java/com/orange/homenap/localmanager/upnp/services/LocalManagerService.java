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
import org.osgi.service.upnp.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import java.util.Dictionary;

import org.osgi.framework.*;
import com.orange.homenap.localmanager.upnp.holders.*;

    public class LocalManagerService implements ILocalManagerService {
	UPnPService service;
	Map map=new HashMap();
	BundleContext context;
	ServiceRegistration registration;
	LocalManagerServiceSkel skel;
	String deviceId;

	public LocalManagerService(UPnPService service,String deviceId,BundleContext context) {
		this.context=context;
		this.service=service;
		this.deviceId=deviceId;
	}
	public UPnPService getGenericService() {
		return service;
	}
	/**
	* OSGI Stub for UPnP action ActionsToTake
	*/
	public void actionsToTake(String actions) throws Exception {
		Dictionary dict = null;
		dict = new Hashtable(1);
		dict.put(new String("Actions"), actions);
		Hashtable result=null;
		try {
		result=(Hashtable)service.getAction("ActionsToTake").invoke(dict);
		}
		catch (NullPointerException ex) {
			throw new UPnPGenException("The action is not availaible on the device: It may be an optional action...");
		}
	}
}
