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
* File Name   : Resource.java
*
* Created     : 06/11/2012
* Author(s)   : Remi Druilhe
*
* Description :
*
*--------------------------------------------------------
*/

package com.orange.homenap.utils;

public class Resource
{
    private String name;
    private Integer value;

    public Resource() {}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Integer getValue() { return value; }

    public void setValue(Integer value) { this.value = value; }
}
