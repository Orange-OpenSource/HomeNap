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
* File Name   : Properties.java
*
* Created     : 28/06/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.olnc.made.homenap.utils;

import java.util.ArrayList;
import java.util.List;

public class Properties
{
    private String className;
    private List<Property> property;

    public Properties()
    {
        property = new ArrayList<Property>();
    }

    public String getClassName()
    {
        return className;
    }

    public List<Property> getProperty()
    {
        return property;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public void setProperty(List<Property> property)
    {
        this.property = property;
    }
}
