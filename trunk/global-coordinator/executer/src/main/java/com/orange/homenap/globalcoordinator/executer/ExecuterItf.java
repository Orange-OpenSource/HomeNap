/*
 * --------------------------------------------------------
 * Module Name : executer
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
 * File Name   : ExecuterItf.java
 *
 * Created     : 09/11/2012
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.globalcoordinator.executer;

import com.orange.homenap.utils.Action;
import com.orange.homenap.utils.Device;

import java.util.List;
import java.util.Map;

public interface ExecuterItf
{
    public void executeActions(Map<Device, List<Action>> actions);
}
