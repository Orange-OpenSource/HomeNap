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

import java.util.Dictionary;

public class LocalManagerActionHandler {
	public static Dictionary actionsToTake_invoke(Dictionary args, ILocalManagerService service) throws Exception {
		Dictionary resultDict = null;
		String value0=null;
		try {
			value0=(String)args.get("Actions");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		service.actionsToTake(value0);
		return resultDict;
	}

}
