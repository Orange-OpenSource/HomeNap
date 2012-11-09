/*--------------------------------------------------------
* Module Name : API
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
*--------------------------------------------------------
* File Name   : Action.java
*
* Created     : 09/11/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.utils;

public class Action
{
    public static enum ActionName {MIGRATE, STOP, START}

    private ActionName actionName;
    private Device fromDevice;
    private Device toDevice;
    private Component component;
    
    public Action() {}

    public ActionName getActionName() { return actionName; }

    public void setActionName(ActionName actionName) { this.actionName = actionName; }

    public Device getFromDevice() { return fromDevice; }

    public void setFromDevice(Device fromDevice) { this.fromDevice = fromDevice; }

    public Device getToDevice() { return toDevice; }

    public void setToDevice(Device toDevice) { this.toDevice = toDevice; }

    public Component getComponent() { return component; }

    public void setComponent(Component component) { this.component = component; }
}
