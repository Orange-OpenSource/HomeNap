/*
 * --------------------------------------------------------
 * Module Name : local-executer
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
 * File Name   : LocalExecuterItf.java
 *
 * Created     : 14/11/2012
 * Author(s)   : Remi Druilhe
 *
 * Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.localmanager.localexecuter;

import com.orange.homenap.utils.Action;

import java.util.List;

public interface LocalExecuterItf
{
    public void migrate(List<Action> actions);
    
    public void start(List<Action> actions);

    public void stop(List<Action> actions);
}
