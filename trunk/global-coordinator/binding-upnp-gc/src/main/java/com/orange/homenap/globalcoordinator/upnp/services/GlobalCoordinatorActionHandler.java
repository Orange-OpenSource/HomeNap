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
import java.util.Hashtable;
import java.util.Dictionary;

    public class GlobalCoordinatorActionHandler {
	public static Dictionary stopService_invoke(Dictionary args, IGlobalCoordinatorService service) throws Exception {
		Dictionary resultDict = null;
		String value0=null;
		try {
			value0=(String)args.get("ServiceId");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		service.stopService(value0);
		return resultDict;
	}

	public static Dictionary register_invoke(Dictionary args, IGlobalCoordinatorService service) throws Exception {
		Dictionary resultDict = null;
		String value0=null;
		try {
			value0=(String)args.get("DeviceInfo");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		boolean returnValue = 		service.register(value0);
		resultDict = new Hashtable(1);
		resultDict.put(new String("Success"), new Boolean(returnValue));
		return resultDict;
	}

	public static Dictionary startService_invoke(Dictionary args, IGlobalCoordinatorService service) throws Exception {
		Dictionary resultDict = null;
		String value0=null;
		try {
			value0=(String)args.get("ServiceInfo");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		String value1=null;
		try {
			value1=(String)args.get("ServiceComponents");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		String value2=null;
		try {
			value2=(String)args.get("DeviceId");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		service.startService(value0,value1,value2);
		return resultDict;
	}

	public static Dictionary unRegister_invoke(Dictionary args, IGlobalCoordinatorService service) throws Exception {
		Dictionary resultDict = null;
		String value0=null;
		try {
			value0=(String)args.get("DeviceId");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		boolean returnValue = 		service.unRegister(value0);
		resultDict = new Hashtable(1);
		resultDict.put(new String("Success"), new Boolean(returnValue));
		return resultDict;
	}

}
