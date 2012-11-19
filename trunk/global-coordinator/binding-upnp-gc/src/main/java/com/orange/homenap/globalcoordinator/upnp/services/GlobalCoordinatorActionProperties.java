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
import java.util.Dictionary;
import com.orange.homenap.globalcoordinator.upnp.holders.*;

    public class GlobalCoordinatorActionProperties {
	public static final String STOPSERVICE_NAME = "StopService";
	public static final String STOPSERVICE_SERVICEID_ARGUMENT_NAME = "ServiceId";
	public static final String STOPSERVICE_SERVICEID_RELATED_STATE_VARIABLE = "ARG_TYPE_ServiceId";

	public static final String REGISTER_NAME = "Register";
	public static final String REGISTER_DEVICEINFO_ARGUMENT_NAME = "DeviceInfo";
	public static final String REGISTER_DEVICEINFO_RELATED_STATE_VARIABLE = "ARG_TYPE_DeviceInfo";
	public static final String REGISTER_SUCCESS_ARGUMENT_NAME = "Success";
	public static final String REGISTER_SUCCESS_RELATED_STATE_VARIABLE = "ARG_TYPE_Success";

	public static final String STARTSERVICE_NAME = "StartService";
	public static final String STARTSERVICE_SERVICEINFO_ARGUMENT_NAME = "ServiceInfo";
	public static final String STARTSERVICE_SERVICEINFO_RELATED_STATE_VARIABLE = "ARG_TYPE_ServiceInfo";
	public static final String STARTSERVICE_SERVICECOMPONENTS_ARGUMENT_NAME = "ServiceComponents";
	public static final String STARTSERVICE_SERVICECOMPONENTS_RELATED_STATE_VARIABLE = "ARG_TYPE_ServiceComponents";
	public static final String STARTSERVICE_DEVICEID_ARGUMENT_NAME = "DeviceId";
	public static final String STARTSERVICE_DEVICEID_RELATED_STATE_VARIABLE = "ARG_TYPE_DeviceId";

	public static final String UNREGISTER_NAME = "UnRegister";
	public static final String UNREGISTER_DEVICEID_ARGUMENT_NAME = "DeviceId";
	public static final String UNREGISTER_DEVICEID_RELATED_STATE_VARIABLE = "ARG_TYPE_DeviceId";
	public static final String UNREGISTER_SUCCESS_ARGUMENT_NAME = "Success";
	public static final String UNREGISTER_SUCCESS_RELATED_STATE_VARIABLE = "ARG_TYPE_Success";

	public static UPnPAction [] createUPnPActions(final GlobalCoordinatorServiceSkel service) {
		return new UPnPAction [] {
			new UPnPGenAction(STOPSERVICE_NAME,
			service,
				new UPnPGenInArgument [] {
					new UPnPGenInArgument(
					STOPSERVICE_SERVICEID_ARGUMENT_NAME,
					STOPSERVICE_SERVICEID_RELATED_STATE_VARIABLE)
				},
				null) {
				public Dictionary invoke(Dictionary args) throws Exception {
					return GlobalCoordinatorActionHandler.stopService_invoke(args, service.getImpl());
				}
			},
			new UPnPGenAction(REGISTER_NAME,
			service,
				new UPnPGenInArgument [] {
					new UPnPGenInArgument(
					REGISTER_DEVICEINFO_ARGUMENT_NAME,
					REGISTER_DEVICEINFO_RELATED_STATE_VARIABLE)
				},
				new UPnPGenOutArgument [] {
					new UPnPGenOutArgument(
					REGISTER_SUCCESS_ARGUMENT_NAME,
					REGISTER_SUCCESS_RELATED_STATE_VARIABLE)
				}) {
				public Dictionary invoke(Dictionary args) throws Exception {
					return GlobalCoordinatorActionHandler.register_invoke(args, service.getImpl());
				}
			},
			new UPnPGenAction(STARTSERVICE_NAME,
			service,
				new UPnPGenInArgument [] {
					new UPnPGenInArgument(
					STARTSERVICE_SERVICEINFO_ARGUMENT_NAME,
					STARTSERVICE_SERVICEINFO_RELATED_STATE_VARIABLE),
					new UPnPGenInArgument(
					STARTSERVICE_SERVICECOMPONENTS_ARGUMENT_NAME,
					STARTSERVICE_SERVICECOMPONENTS_RELATED_STATE_VARIABLE),
					new UPnPGenInArgument(
					STARTSERVICE_DEVICEID_ARGUMENT_NAME,
					STARTSERVICE_DEVICEID_RELATED_STATE_VARIABLE)
				},
				null) {
				public Dictionary invoke(Dictionary args) throws Exception {
					return GlobalCoordinatorActionHandler.startService_invoke(args, service.getImpl());
				}
			},
			new UPnPGenAction(UNREGISTER_NAME,
			service,
				new UPnPGenInArgument [] {
					new UPnPGenInArgument(
					UNREGISTER_DEVICEID_ARGUMENT_NAME,
					UNREGISTER_DEVICEID_RELATED_STATE_VARIABLE)
				},
				new UPnPGenOutArgument [] {
					new UPnPGenOutArgument(
					UNREGISTER_SUCCESS_ARGUMENT_NAME,
					UNREGISTER_SUCCESS_RELATED_STATE_VARIABLE)
				}) {
				public Dictionary invoke(Dictionary args) throws Exception {
					return GlobalCoordinatorActionHandler.unRegister_invoke(args, service.getImpl());
				}
			}
		};
	}
}
