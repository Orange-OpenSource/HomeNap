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


import org.osgi.service.upnp.*;
import java.util.Dictionary;
import com.orange.homenap.localmanager.upnp.holders.*;

    public class LocalManagerActionProperties {
	public static final String ACTIONSTOTAKE_NAME = "ActionsToTake";
	public static final String ACTIONSTOTAKE_ACTIONS_ARGUMENT_NAME = "Actions";
	public static final String ACTIONSTOTAKE_ACTIONS_RELATED_STATE_VARIABLE = "ARG_TYPE_Actions";

	public static UPnPAction [] createUPnPActions(final LocalManagerServiceSkel service) {
		return new UPnPAction [] {
			new UPnPGenAction(ACTIONSTOTAKE_NAME,
			service,
				new UPnPGenInArgument [] {
					new UPnPGenInArgument(
					ACTIONSTOTAKE_ACTIONS_ARGUMENT_NAME,
					ACTIONSTOTAKE_ACTIONS_RELATED_STATE_VARIABLE)
				},
				null) {
				public Dictionary invoke(Dictionary args) throws Exception {
					return LocalManagerActionHandler.actionsToTake_invoke(args, service.getImpl());
				}
			}
		};
	}
}
