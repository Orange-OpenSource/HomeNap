/*
 * --------------------------------------------------------
 * Module Name : api
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

package com.orange.homenap.utils;

public class Property
{
    private String name;
    //private String type;
    private Object value;

    public Property() {}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    /*public String getType() { return type; }

    public void setType(String type) { this.type = type; }*/

    public Object getValue() { return value; }

    public void setValue(Object value) { this.value = value; }
}
