/*
 * --------------------------------------------------------
 * Module Name : api
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

package com.orange.homenap.api;



    public interface IGlobalCoordinatorService {
	/**
	* Interface declaration for UPnP action: StopService
	*/
	public void stopService(String serviceId) throws Exception;
	/**
	* Interface declaration for UPnP action: Register
	*/
	public boolean register(String deviceInfo) throws Exception;
	/**
	* Interface declaration for UPnP action: StartService
	*/
	public void startService(String serviceInfo, String serviceComponents, String deviceId) throws Exception;
	/**
	* Interface declaration for UPnP action: UnRegister
	*/
	public boolean unRegister(String deviceId) throws Exception;
}
