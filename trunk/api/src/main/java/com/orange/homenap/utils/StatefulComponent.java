/*--------------------------------------------------------
* Module Name : API
* Version : 0.1-SNAPSHOT
*
* Software Name : HomeNap
* Version : 0.1-SNAPSHOT
*
* Copyright © 28/06/2012 – 28/06/2012 France Télécom
* This software is distributed under the Apache 2.0 license,
* the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
* or see the "LICENSE-2.0.txt" file for more details.
*
*--------------------------------------------------------
* File Name   : StatefulComponent.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.utils;

import java.util.HashMap;
import java.util.Map;

public class StatefulComponent
{
    private String componentName;
    private Map<String, Object> properties;
    
    public StatefulComponent()
    {
        this.properties = new HashMap<String, Object>();
    }
    
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public String getComponentName() { return this.componentName; }
    
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    
    public Map<String, Object> getProperties() { return this.properties; }
}
