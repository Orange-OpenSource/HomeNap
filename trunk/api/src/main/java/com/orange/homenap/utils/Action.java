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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Action
{
    public static enum ActionName {MIGRATE, STOP, START, REGISTER, UNREGISTER}

    private ActionName actionName;
    private String fromDevice;
    private Device toDevice;
    private Component component;
    private Map<String, Object> properties = new HashMap<String, Object>();
    //private List<Property> properties = new ArrayList<Property>();
    
    public Action() {}

    public ActionName getActionName() { return actionName; }

    public void setActionName(ActionName actionName) { this.actionName = actionName; }

    public String getFromDevice() { return fromDevice; }

    public void setFromDevice(String fromDevice) { this.fromDevice = fromDevice; }

    public Device getToDevice() { return toDevice; }

    public void setToDevice(Device toDevice) { this.toDevice = toDevice; }

    public Component getComponent() { return component; }

    public void setComponent(Component component) { this.component = component; }

    /*public List<Property> getProperties() { return properties; }

    public void setProperties(List<Property> properties) { this.properties = properties; }*/

    public Map<String, Object> getProperties() { return properties; }

    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}
