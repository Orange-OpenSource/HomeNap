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
import org.osgi.service.upnp.*;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import java.util.Dictionary;
import java.util.Properties;
import org.osgi.framework.*;
import com.orange.homenap.globalcoordinator.upnp.holders.*;

    public class GlobalCoordinatorService implements IGlobalCoordinatorService {
	UPnPService service;
	Map map=new HashMap();
	BundleContext context;
	ServiceRegistration registration;
	GlobalCoordinatorServiceSkel skel;
	String deviceId;

	public GlobalCoordinatorService(UPnPService service,String deviceId,BundleContext context) {
		this.context=context;
		this.service=service;
		this.deviceId=deviceId;
	}
	public UPnPService getGenericService() {
		return service;
	}
	/**
	* OSGI Stub for UPnP action StopService
	*/
	public void stopService(String serviceId) throws Exception {
		Dictionary dict = null;
		dict = new Hashtable(1);
		dict.put(new String("ServiceId"), serviceId);
		Hashtable result=null;
		try {
		result=(Hashtable)service.getAction("StopService").invoke(dict);
		}
		catch (NullPointerException ex) {
			throw new UPnPGenException("The action is not availaible on the device: It may be an optional action...");
		}
	}
	/**
	* OSGI Stub for UPnP action Register
	*/
	public boolean register(String deviceInfo) throws Exception {
		Dictionary dict = null;
		dict = new Hashtable(1);
		dict.put(new String("DeviceInfo"), deviceInfo);
		Hashtable result=null;
		try {
		result=(Hashtable)service.getAction("Register").invoke(dict);
		}
		catch (NullPointerException ex) {
			throw new UPnPGenException("The action is not availaible on the device: It may be an optional action...");
		}
		boolean value0=false;
		try {
			value0=((Boolean)result.get("Success")).booleanValue();
		}
		catch (Exception ex) {
			int number = ((Integer)result.get("Success")).intValue();
			if(number == 1) value0 = true;
			else value0 = false;
		}
		return value0;
	}
	/**
	* OSGI Stub for UPnP action StartService
	*/
	public void startService(String serviceInfo, String serviceComponents, String deviceId) throws Exception {
		Dictionary dict = null;
		dict = new Hashtable(3);
		dict.put(new String("ServiceInfo"), serviceInfo);
		dict.put(new String("ServiceComponents"), serviceComponents);
		dict.put(new String("DeviceId"), deviceId);
		Hashtable result=null;
		try {
		result=(Hashtable)service.getAction("StartService").invoke(dict);
		}
		catch (NullPointerException ex) {
			throw new UPnPGenException("The action is not availaible on the device: It may be an optional action...");
		}
	}
	/**
	* OSGI Stub for UPnP action UnRegister
	*/
	public boolean unRegister(String deviceId) throws Exception {
		Dictionary dict = null;
		dict = new Hashtable(1);
		dict.put(new String("DeviceId"), deviceId);
		Hashtable result=null;
		try {
		result=(Hashtable)service.getAction("UnRegister").invoke(dict);
		}
		catch (NullPointerException ex) {
			throw new UPnPGenException("The action is not availaible on the device: It may be an optional action...");
		}
		boolean value0=false;
		try {
			value0=((Boolean)result.get("Success")).booleanValue();
		}
		catch (Exception ex) {
			int number = ((Integer)result.get("Success")).intValue();
			if(number == 1) value0 = true;
			else value0 = false;
		}
		return value0;
	}
}
