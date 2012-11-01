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
 *  File Name   : LocalManagerActionHandler.java
 *
 *  Created     : 28/06/2012
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

/**
 * LocalManagerActionHandler.java
 * 
 * generated by France Telecom UPnP Generator
 * 
 */

package com.orange.homenap.localmanager.upnp.services;


import com.orange.homenap.api.ILocalManagerService;

import java.util.Dictionary;

/**
	* null
	*/
public class LocalManagerActionHandler {
	public static Dictionary migrateService_invoke(Dictionary args, ILocalManagerService service) throws Exception {
		Dictionary resultDict = null;
		java.lang.String value0=null;
		try {
			value0=(java.lang.String)args.get("ServiceId");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		java.lang.String value1=null;
		try {
			value1=(java.lang.String)args.get("ToDeviceId");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		java.lang.String value2=null;
		try {
			value2=(java.lang.String)args.get("WakeUpAddress");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		service.migrateService(value0,value1,value2);
		return resultDict;
	}

}