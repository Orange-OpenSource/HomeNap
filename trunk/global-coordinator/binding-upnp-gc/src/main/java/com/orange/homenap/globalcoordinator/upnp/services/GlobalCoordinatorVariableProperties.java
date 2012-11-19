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


import org.osgi.service.upnp.*;
import com.orange.homenap.globalcoordinator.upnp.holders.*;

    public class GlobalCoordinatorVariableProperties {
	public static final String ARG_TYPE_SUCCESS_NAME = "ARG_TYPE_Success";
	public static final boolean ARG_TYPE_SUCCESS_SENDS_EVENTS     = false;
	public static final Class ARG_TYPE_SUCCESS_JAVA_DATA_TYPE     = boolean.class;
	public static final String ARG_TYPE_SUCCESS_UPNP_DATA_TYPE    = "boolean";
	public static final String [] ARG_TYPE_SUCCESS_ALLOWED_VALUES = null;
	public static final Object ARG_TYPE_SUCCESS_DEFAULT_VALUE     = null;
	public static final Number ARG_TYPE_SUCCESS_MINIMUM = null;
	public static final Number ARG_TYPE_SUCCESS_MAXIMUM = null;
	public static final Number ARG_TYPE_SUCCESS_STEP = null;

	public static final String ARG_TYPE_DEVICEINFO_NAME = "ARG_TYPE_DeviceInfo";
	public static final boolean ARG_TYPE_DEVICEINFO_SENDS_EVENTS     = false;
	public static final Class ARG_TYPE_DEVICEINFO_JAVA_DATA_TYPE     = String.class;
	public static final String ARG_TYPE_DEVICEINFO_UPNP_DATA_TYPE    = "string";
	public static final String [] ARG_TYPE_DEVICEINFO_ALLOWED_VALUES = null;
	public static final Object ARG_TYPE_DEVICEINFO_DEFAULT_VALUE     = null;
	public static final Number ARG_TYPE_DEVICEINFO_MINIMUM = null;
	public static final Number ARG_TYPE_DEVICEINFO_MAXIMUM = null;
	public static final Number ARG_TYPE_DEVICEINFO_STEP = null;

	public static final String ARG_TYPE_SERVICEID_NAME = "ARG_TYPE_ServiceId";
	public static final boolean ARG_TYPE_SERVICEID_SENDS_EVENTS     = false;
	public static final Class ARG_TYPE_SERVICEID_JAVA_DATA_TYPE     = String.class;
	public static final String ARG_TYPE_SERVICEID_UPNP_DATA_TYPE    = "string";
	public static final String [] ARG_TYPE_SERVICEID_ALLOWED_VALUES = null;
	public static final Object ARG_TYPE_SERVICEID_DEFAULT_VALUE     = null;
	public static final Number ARG_TYPE_SERVICEID_MINIMUM = null;
	public static final Number ARG_TYPE_SERVICEID_MAXIMUM = null;
	public static final Number ARG_TYPE_SERVICEID_STEP = null;

	public static final String ARG_TYPE_SERVICEINFO_NAME = "ARG_TYPE_ServiceInfo";
	public static final boolean ARG_TYPE_SERVICEINFO_SENDS_EVENTS     = false;
	public static final Class ARG_TYPE_SERVICEINFO_JAVA_DATA_TYPE     = String.class;
	public static final String ARG_TYPE_SERVICEINFO_UPNP_DATA_TYPE    = "string";
	public static final String [] ARG_TYPE_SERVICEINFO_ALLOWED_VALUES = null;
	public static final Object ARG_TYPE_SERVICEINFO_DEFAULT_VALUE     = null;
	public static final Number ARG_TYPE_SERVICEINFO_MINIMUM = null;
	public static final Number ARG_TYPE_SERVICEINFO_MAXIMUM = null;
	public static final Number ARG_TYPE_SERVICEINFO_STEP = null;

	public static final String ARG_TYPE_DEVICEID_NAME = "ARG_TYPE_DeviceId";
	public static final boolean ARG_TYPE_DEVICEID_SENDS_EVENTS     = false;
	public static final Class ARG_TYPE_DEVICEID_JAVA_DATA_TYPE     = String.class;
	public static final String ARG_TYPE_DEVICEID_UPNP_DATA_TYPE    = "uuid";
	public static final String [] ARG_TYPE_DEVICEID_ALLOWED_VALUES = null;
	public static final Object ARG_TYPE_DEVICEID_DEFAULT_VALUE     = null;
	public static final Number ARG_TYPE_DEVICEID_MINIMUM = null;
	public static final Number ARG_TYPE_DEVICEID_MAXIMUM = null;
	public static final Number ARG_TYPE_DEVICEID_STEP = null;

	public static final String ARG_TYPE_SERVICECOMPONENTS_NAME = "ARG_TYPE_ServiceComponents";
	public static final boolean ARG_TYPE_SERVICECOMPONENTS_SENDS_EVENTS     = false;
	public static final Class ARG_TYPE_SERVICECOMPONENTS_JAVA_DATA_TYPE     = String.class;
	public static final String ARG_TYPE_SERVICECOMPONENTS_UPNP_DATA_TYPE    = "string";
	public static final String [] ARG_TYPE_SERVICECOMPONENTS_ALLOWED_VALUES = null;
	public static final Object ARG_TYPE_SERVICECOMPONENTS_DEFAULT_VALUE     = null;
	public static final Number ARG_TYPE_SERVICECOMPONENTS_MINIMUM = null;
	public static final Number ARG_TYPE_SERVICECOMPONENTS_MAXIMUM = null;
	public static final Number ARG_TYPE_SERVICECOMPONENTS_STEP = null;

	public static UPnPStateVariable [] createUPnPStateVariables() {
		return new UPnPStateVariable [] {
			new UPnPGenStateVariable(ARG_TYPE_SUCCESS_NAME,
			ARG_TYPE_SUCCESS_SENDS_EVENTS,
			ARG_TYPE_SUCCESS_JAVA_DATA_TYPE,
			ARG_TYPE_SUCCESS_UPNP_DATA_TYPE,
			ARG_TYPE_SUCCESS_ALLOWED_VALUES,
			ARG_TYPE_SUCCESS_DEFAULT_VALUE,
			ARG_TYPE_SUCCESS_MINIMUM,
			ARG_TYPE_SUCCESS_MAXIMUM,
			ARG_TYPE_SUCCESS_STEP),
			new UPnPGenStateVariable(ARG_TYPE_DEVICEINFO_NAME,
			ARG_TYPE_DEVICEINFO_SENDS_EVENTS,
			ARG_TYPE_DEVICEINFO_JAVA_DATA_TYPE,
			ARG_TYPE_DEVICEINFO_UPNP_DATA_TYPE,
			ARG_TYPE_DEVICEINFO_ALLOWED_VALUES,
			ARG_TYPE_DEVICEINFO_DEFAULT_VALUE,
			ARG_TYPE_DEVICEINFO_MINIMUM,
			ARG_TYPE_DEVICEINFO_MAXIMUM,
			ARG_TYPE_DEVICEINFO_STEP),
			new UPnPGenStateVariable(ARG_TYPE_SERVICEID_NAME,
			ARG_TYPE_SERVICEID_SENDS_EVENTS,
			ARG_TYPE_SERVICEID_JAVA_DATA_TYPE,
			ARG_TYPE_SERVICEID_UPNP_DATA_TYPE,
			ARG_TYPE_SERVICEID_ALLOWED_VALUES,
			ARG_TYPE_SERVICEID_DEFAULT_VALUE,
			ARG_TYPE_SERVICEID_MINIMUM,
			ARG_TYPE_SERVICEID_MAXIMUM,
			ARG_TYPE_SERVICEID_STEP),
			new UPnPGenStateVariable(ARG_TYPE_SERVICEINFO_NAME,
			ARG_TYPE_SERVICEINFO_SENDS_EVENTS,
			ARG_TYPE_SERVICEINFO_JAVA_DATA_TYPE,
			ARG_TYPE_SERVICEINFO_UPNP_DATA_TYPE,
			ARG_TYPE_SERVICEINFO_ALLOWED_VALUES,
			ARG_TYPE_SERVICEINFO_DEFAULT_VALUE,
			ARG_TYPE_SERVICEINFO_MINIMUM,
			ARG_TYPE_SERVICEINFO_MAXIMUM,
			ARG_TYPE_SERVICEINFO_STEP),
			new UPnPGenStateVariable(ARG_TYPE_DEVICEID_NAME,
			ARG_TYPE_DEVICEID_SENDS_EVENTS,
			ARG_TYPE_DEVICEID_JAVA_DATA_TYPE,
			ARG_TYPE_DEVICEID_UPNP_DATA_TYPE,
			ARG_TYPE_DEVICEID_ALLOWED_VALUES,
			ARG_TYPE_DEVICEID_DEFAULT_VALUE,
			ARG_TYPE_DEVICEID_MINIMUM,
			ARG_TYPE_DEVICEID_MAXIMUM,
			ARG_TYPE_DEVICEID_STEP),
			new UPnPGenStateVariable(ARG_TYPE_SERVICECOMPONENTS_NAME,
			ARG_TYPE_SERVICECOMPONENTS_SENDS_EVENTS,
			ARG_TYPE_SERVICECOMPONENTS_JAVA_DATA_TYPE,
			ARG_TYPE_SERVICECOMPONENTS_UPNP_DATA_TYPE,
			ARG_TYPE_SERVICECOMPONENTS_ALLOWED_VALUES,
			ARG_TYPE_SERVICECOMPONENTS_DEFAULT_VALUE,
			ARG_TYPE_SERVICECOMPONENTS_MINIMUM,
			ARG_TYPE_SERVICECOMPONENTS_MAXIMUM,
			ARG_TYPE_SERVICECOMPONENTS_STEP)
		};
	}
}
