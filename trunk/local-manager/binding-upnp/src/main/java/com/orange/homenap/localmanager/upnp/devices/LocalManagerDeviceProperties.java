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

package com.orange.homenap.localmanager.upnp.devices;


import org.osgi.service.upnp.*;
import java.util.Properties;
import java.util.Random;

    public class LocalManagerDeviceProperties {
	public final static String UDN               = "LocalManager";
	public static final String TYPE              = "urn:schemas-upnp-org:device:LocalManager:1";
	public static final String MANUFACTURER      = "Orange";
	public static final String MODEL_NAME        = "LocalManager";
	public static final String FRIENDLY_NAME     = "UPnP Local Manager";
	public static final String MANUFACTURER_URL  = "remi.druilhe@orange.com";
	public static final String MODEL_DESCRIPTION = "Local Manager";
	public static final String MODEL_NUMBER      = "0.1";
	public static final String MODEL_URL         = "/";
	public static final String SERIAL_NUMBER     = "1234267890";
	public static final String UPC               = "3141592";
	public static final String PRESENTATION_URL               = "null";

	public static Properties createProperties(Properties props) {
		Random randomGen = new Random();

	        int random = randomGen.nextInt(100);

		props.put("service.pid", UDN);
		props.put(UPnPDevice.UPNP_EXPORT, "");
		props.put(
		        org.osgi.service
		        	.device.Constants.DEVICE_CATEGORY,
		    	new String[]{UPnPDevice.DEVICE_CATEGORY}
		    );
		props.put(UPnPDevice.UDN, UDN + random);
		props.put(UPnPDevice.TYPE, LocalManagerDeviceProperties.TYPE);
		props.put(UPnPDevice.MANUFACTURER, LocalManagerDeviceProperties.MANUFACTURER);
		props.put(UPnPDevice.MODEL_NAME, LocalManagerDeviceProperties.MODEL_NAME);
		props.put(UPnPDevice.FRIENDLY_NAME, LocalManagerDeviceProperties.FRIENDLY_NAME);
		props.put(UPnPDevice.MANUFACTURER_URL, LocalManagerDeviceProperties.MANUFACTURER_URL);
		props.put(UPnPDevice.MODEL_DESCRIPTION, LocalManagerDeviceProperties.MODEL_DESCRIPTION);
		props.put(UPnPDevice.MODEL_NUMBER, LocalManagerDeviceProperties.MODEL_NUMBER);
		props.put(UPnPDevice.MODEL_URL, LocalManagerDeviceProperties.MODEL_URL);
		props.put(UPnPDevice.SERIAL_NUMBER, LocalManagerDeviceProperties.SERIAL_NUMBER);
		props.put(UPnPDevice.UPC, LocalManagerDeviceProperties.UPC);
		props.put(UPnPDevice.PRESENTATION_URL, LocalManagerDeviceProperties.PRESENTATION_URL);
		return props;
	}
}
