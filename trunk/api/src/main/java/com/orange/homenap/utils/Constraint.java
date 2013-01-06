/*
 * --------------------------------------------------------
 *  Module Name : api
 *  Version : 0.1-SNAPSHOT
 *
 *  Software Name : HomeNap
 *  Version : 0.1-SNAPSHOT
 *
 *  Copyright © 28/06/2012 – 28/06/2012 France Télécom
 *  This software is distributed under the Apache 2.0 license,
 *  the text of which is available at http://www.apache.org/licenses/LICENSE-2.0.html
 *  or see the "LICENSE-2.0.txt" file for more details.
 *
 * --------------------------------------------------------
 *  File Name   : Constraint.java
 *
 *  Created     : 06/01/2013
 *  Author(s)   : Remi Druilhe
 *
 *  Description :
 *
 * --------------------------------------------------------
 */

package com.orange.homenap.utils;

public class Constraint
{
    private String name;
    private String value;

    public Constraint() {}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
}
